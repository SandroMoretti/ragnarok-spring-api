package sandromoretti.ragnarokspringapi.service;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sandromoretti.ragnarokspringapi.config.DonationConfig;
import sandromoretti.ragnarokspringapi.entity.Donation;
import sandromoretti.ragnarokspringapi.entity.User;
import sandromoretti.ragnarokspringapi.repository.DonationRepository;
import sandromoretti.ragnarokspringapi.repository.UserRepository;
import sandromoretti.ragnarokspringapi.request.MercadoPagoWebHookNotification;
import sandromoretti.ragnarokspringapi.response.NewDonationResponse;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class DonationService {
    Logger logger = LoggerFactory.getLogger(DonationService.class);

    @Autowired
    DonationRepository donationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    public ResponseEntity<NewDonationResponse> newDonation(Donation donation){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        donation.setUser((User) authentication.getPrincipal());

        switch(donation.getService()){
            case "mercado_pago":{
                try {
                    PreferenceItemRequest itemRequest =
                            PreferenceItemRequest.builder()
                                    .title(calculateCashsPerDonationAmount(donation.getAmount()) + "x CashPoints")
                                    .description("Cash due to donate to our private server <3")
                                    .categoryId("games")
                                    .quantity(1)
                                    .currencyId("BRL")
                                    .unitPrice(new BigDecimal(donation.getAmount()))
                                    .build();
                    List<PreferenceItemRequest> items = new ArrayList<>();
                    items.add(itemRequest);
                    PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                            .items(items).build();

                    PreferenceClient client = new PreferenceClient();

                    Preference preference = client.create(preferenceRequest);



                    donation.setServiceOrderId(preference.getId());
                }catch(MPException ex){
                    ex.printStackTrace();
                }catch(MPApiException ex){
                    ex.printStackTrace();
                }
                break;
            }
            default:
                return new ResponseEntity<>(new NewDonationResponse(null, "Invalid payment type"), HttpStatus.CONFLICT);
        }

        // forbidden fields - auto set fields to default value
        donation.setCreated_at(new Timestamp(System.currentTimeMillis()));
        donation.setCompleted(0);
        donation.setStatus("pending");
        donation.setPaid_at(null);
        donation.setCompleted_at(null);

        //Donation saved_donation = this.donationRepository.save(donation);
        return new ResponseEntity<>(new NewDonationResponse(donation, "SUCCESS"), HttpStatus.OK);
    }

    public void processMercadoPagoPaymentNotification(MercadoPagoWebHookNotification notification){
        Donation donation = this.donationRepository.getDonationByServiceAndServiceOrderId("mercado_pago", notification.getDataId() + "");
        this.logger.debug("fetching donation");
        try{
            PaymentClient paymentClient = new PaymentClient();
            Payment payment = paymentClient.get(notification.getDataId());
            if(donation == null){
                this.logger.debug("donation not exist for now");
                donation = new Donation();

                donation.setCreated_at(new Timestamp(System.currentTimeMillis()));
                donation.setCompleted(0);

                donation.setPaid_at(null);
                donation.setCompleted_at(null);
                donation.setService("mercado_pago");
                donation.setServiceOrderId(notification.getDataId()+"");

                User user = userRepository.findByEmail(payment.getPayer().getEmail());
                donation.setUser(user);
                donation.setAmount(payment.getTransactionAmount().intValue());
            }

            donation.setStatus(payment.getStatus());
            if(payment.getStatus().equals("approved")){
                donation.setPaid(1);
                donation.setPaid_at(new Timestamp(System.currentTimeMillis()));
            }

            this.donationRepository.save(donation);
        }catch(MPException ex){
            ex.printStackTrace();
        }catch(MPApiException ex){
            ex.printStackTrace();
        }
    }

    private int calculateCashsPerDonationAmount(double amount){
        return (int) Math.round(DonationConfig.CASH_PER_AMOUNT * amount);
    }
}
