package sandromoretti.ragnarokspringapi.repository;

import org.springframework.data.repository.CrudRepository;
import sandromoretti.ragnarokspringapi.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
    User findByUserid(String userid);
}
