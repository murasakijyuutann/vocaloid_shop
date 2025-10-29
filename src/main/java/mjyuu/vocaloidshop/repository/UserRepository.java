package mjyuu.vocaloidshop.repository;

import mjyuu.vocaloidshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
