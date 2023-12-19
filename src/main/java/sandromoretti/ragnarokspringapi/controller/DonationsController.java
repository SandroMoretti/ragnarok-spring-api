package sandromoretti.ragnarokspringapi.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sandromoretti.ragnarokspringapi.config.GroupsConfig;
import sandromoretti.ragnarokspringapi.entity.Donation;
import sandromoretti.ragnarokspringapi.entity.User;
import sandromoretti.ragnarokspringapi.response.NewDonationResponse;
import sandromoretti.ragnarokspringapi.service.DonationService;

@Tag(name="Donations", description = "Donations api")
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
@RestController
@RequestMapping(path="/donations")
public class DonationsController {
    @Autowired
    DonationService donationService;

    @PostMapping(path="")
    public ResponseEntity<NewDonationResponse> createDonation(@RequestBody @Valid Donation donation){
        return this.donationService.newDonation(donation);
    }

    /*
    @PostMapping(path="")
    public ResponseEntity<NewDonationResponse> getDonationPreferenceMercadoPago(@RequestBody @Valid Donation donation){
        return this.donationService.newDonation(donation);
    }
     */
}
