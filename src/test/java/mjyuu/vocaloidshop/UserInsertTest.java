package mjyuu.vocaloidshop;

import mjyuu.vocaloidshop.entity.User;
import mjyuu.vocaloidshop.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserInsertTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void insertTestUsers() {
        userRepository.save(User.builder().email("miku@example.com").name("初音ミク").build());
        userRepository.save(User.builder().email("rin@example.com").name("鏡音リン").build());
        userRepository.save(User.builder().email("len@example.com").name("鏡音レン").build());

        long count = userRepository.count();
        assertEquals(3, count); // import static org.junit.jupiter.api.Assertions.assertEquals;
    }
}
