package mjyuu.vocaloidshop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash; // ✅ Store hashed password (not plain)

    private String name;

    @Column(nullable = false)
    @Builder.Default
    private boolean isAdmin = false;

    // Optional profile fields
    private String nickname;

    private java.time.LocalDate birthday;

    private java.time.LocalDateTime birthdayLastUpdatedAt;

    // Cart
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;

    // Orders
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;
}
