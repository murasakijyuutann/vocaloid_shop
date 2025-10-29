package mjyuu.vocaloidshop.repository;

import mjyuu.vocaloidshop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
