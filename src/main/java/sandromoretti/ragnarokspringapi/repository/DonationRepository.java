package sandromoretti.ragnarokspringapi.repository;

import org.springframework.data.repository.CrudRepository;
import sandromoretti.ragnarokspringapi.entity.Donation;

public interface DonationRepository extends CrudRepository<Donation, Integer> {
    Donation getDonationByServiceAndServiceOrderId(String service, String service_order_id);
}
