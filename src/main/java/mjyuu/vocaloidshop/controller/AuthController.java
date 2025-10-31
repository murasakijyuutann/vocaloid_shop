package mjyuu.vocaloidshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mjyuu.vocaloidshop.dto.AuthRequestDTO;
import mjyuu.vocaloidshop.dto.AuthResponseDTO;
import mjyuu.vocaloidshop.dto.UserInfoDTO;
import mjyuu.vocaloidshop.dto.RegisterRequestDTO;
import mjyuu.vocaloidshop.dto.UpdateProfileDTO;
import mjyuu.vocaloidshop.entity.User;
import mjyuu.vocaloidshop.repository.UserRepository;
import mjyuu.vocaloidshop.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다");
        }

        java.time.LocalDate birth = null;
        if (dto.getBirthday() != null && !dto.getBirthday().isBlank()) {
            try { birth = java.time.LocalDate.parse(dto.getBirthday()); } catch (Exception ignored) {}
        }

        String nickname = (dto.getNickname() != null && !dto.getNickname().isBlank()) ? dto.getNickname() : null;

        User user = User.builder()
                .email(dto.getEmail())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .name(nickname != null ? nickname : "New User")
                .nickname(nickname)
                .birthday(birth)
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getId(), user.getEmail());
        return ResponseEntity.ok(new AuthResponseDTO(token, user.getEmail(), user.getName()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("등록되지 않은 사용자입니다"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail());
        return ResponseEntity.ok(new AuthResponseDTO(token, user.getEmail(), user.getName()));
    }

    // 🆕 Add this
    @GetMapping("/me")
    public ResponseEntity<UserInfoDTO> getCurrentUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        UserInfoDTO dto = UserInfoDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
        .isAdmin(user.isAdmin())
        .nickname(user.getNickname())
        .birthday(user.getBirthday())
                .build();

        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/me")
    public ResponseEntity<UserInfoDTO> updateCurrentUser(Authentication authentication,
                                                         @RequestBody UpdateProfileDTO update) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        // Update nickname freely
        if (update.getNickname() != null) {
            String nick = update.getNickname().isBlank() ? null : update.getNickname();
            user.setNickname(nick);
            // do not auto-sync name to avoid surprising header change; can be changed later
        }

        // Update birthday with once-per-year rule
        if (update.getBirthday() != null) {
            java.time.LocalDate newBirth = null;
            if (!update.getBirthday().isBlank()) {
                try { newBirth = java.time.LocalDate.parse(update.getBirthday()); } catch (Exception ignored) {}
            }
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            java.time.LocalDateTime last = user.getBirthdayLastUpdatedAt();
            boolean canUpdate = last == null || last.plusYears(1).isBefore(now) || last.plusYears(1).isEqual(now);
            if (!canUpdate) {
                return ResponseEntity.badRequest().body(
                    UserInfoDTO.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .name(user.getName())
                        .isAdmin(user.isAdmin())
                        .nickname(user.getNickname())
                        .birthday(user.getBirthday())
                        .build()
                );
            }
            user.setBirthday(newBirth);
            user.setBirthdayLastUpdatedAt(now);
        }

        userRepository.save(user);

        UserInfoDTO dto = UserInfoDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .isAdmin(user.isAdmin())
                .nickname(user.getNickname())
                .birthday(user.getBirthday())
                .build();
        return ResponseEntity.ok(dto);
    }
}
