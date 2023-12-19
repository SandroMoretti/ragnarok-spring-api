package sandromoretti.ragnarokspringapi.response;

import sandromoretti.ragnarokspringapi.entity.Donation;

public class NewDonationResponse {
    private String message;
    private Donation donation;

    public NewDonationResponse(Donation donation, String message){
        this.donation = donation;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Donation getDonation() {
        return donation;
    }

    public void setDonation(Donation donation) {
        this.donation = donation;
    }
}
