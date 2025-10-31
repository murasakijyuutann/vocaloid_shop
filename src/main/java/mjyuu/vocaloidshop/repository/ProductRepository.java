package mjyuu.vocaloidshop.repository;

import mjyuu.vocaloidshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Page<Product> findByNameContainingIgnoreCase(String q, Pageable pageable);
	Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
	Page<Product> findByNameContainingIgnoreCaseAndCategoryId(String q, Long categoryId, Pageable pageable);
}
