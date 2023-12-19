package sandromoretti.ragnarokspringapi.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sandromoretti.ragnarokspringapi.request.MercadoPagoWebHookNotification;
import sandromoretti.ragnarokspringapi.service.DonationService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name="Integrations", description = "Integrations api")
@RestController
@RequestMapping(path="/integrations")
public class IntegrationsController {
    Logger logger = LoggerFactory.getLogger(IntegrationsController.class);

    @Autowired
    private DonationService donationService;

    @PostMapping(path="/mercado_pago")
    public void mercado_pago_notifications(@RequestBody @Valid MercadoPagoWebHookNotification notification){
        this.logger.debug("MERCADO PAGO NOTIFICATION -> INIT");
        this.logger.debug(notification.toString());
        if(notification.getType().equals("payment") && notification.getData() != null){
            donationService.processMercadoPagoPaymentNotification(notification);
        }
        this.logger.debug("MERCADO PAGO NOTIFICATION -> END");
    }
}
