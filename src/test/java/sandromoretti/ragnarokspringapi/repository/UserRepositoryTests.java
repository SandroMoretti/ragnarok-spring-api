package sandromoretti.ragnarokspringapi.repository;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import sandromoretti.ragnarokspringapi.entity.User;

import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testAddUser(){
        User user = new User();
        user.setUserid("test");
        user.setEmail("test@test.com");
        user.setUser_pass("test123");

        User saved_user = userRepository.save(user);

        assertThat(saved_user, Matchers.notNullValue());
        assertThat(saved_user.getAccount_id(), Matchers.greaterThan(0));
    }
}
