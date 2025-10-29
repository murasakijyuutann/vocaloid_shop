package mjyuu.vocaloidshop.repository;

import mjyuu.vocaloidshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
