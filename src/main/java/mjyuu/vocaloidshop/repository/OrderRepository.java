package mjyuu.vocaloidshop.repository;

import mjyuu.vocaloidshop.entity.Order;
import mjyuu.vocaloidshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByUserOrderByOrderedAtDesc(User user);
}
