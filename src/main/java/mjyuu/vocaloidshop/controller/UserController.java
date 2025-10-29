package mjyuu.vocaloidshop.controller;

import lombok.RequiredArgsConstructor;
import mjyuu.vocaloidshop.entity.User;
import mjyuu.vocaloidshop.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PostMapping("/seed")
    public String seedUsers() {
        userRepository.save(User.builder().email("miku@example.com").name("初音ミク").build());
        userRepository.save(User.builder().email("rin@example.com").name("鏡音リン").build());
        userRepository.save(User.builder().email("len@example.com").name("鏡音レン").build());
        return "Users seeded!";
    }

    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }
}
