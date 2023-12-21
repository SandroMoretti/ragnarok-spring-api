package sandromoretti.ragnarokspringapi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import sandromoretti.ragnarokspringapi.entity.User;
import sandromoretti.ragnarokspringapi.entity.UserActionToken;

public interface UserActionTokenRepository extends CrudRepository<UserActionToken, String> {
    @Query(value = "SELECT * FROM user_action_token WHERE token=?1 AND  user_id = ?2 AND used=0 AND expires_at > now()", nativeQuery = true)
    UserActionToken findValidTokenFromUser(String token, int user_id);
}
