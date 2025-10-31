package mjyuu.vocaloidshop.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mjyuu.vocaloidshop.dto.ProductRequestDTO;
import mjyuu.vocaloidshop.dto.ProductResponseDTO;
import mjyuu.vocaloidshop.entity.Category;
import mjyuu.vocaloidshop.entity.Product;
import mjyuu.vocaloidshop.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    // 🟢 Use manual mapping for full control over fields
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> ProductResponseDTO.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .imageUrl(product.getImageUrl())
                        .categoryName(product.getCategory().getName()) // ✅ Category name
                        .build())
                .collect(Collectors.toList());
    }

        public Page<ProductResponseDTO> search(String q, Long categoryId, String sort, String dir, int page, int size) {
                Sort.Direction direction = "desc".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC;
                String sortBy = (sort == null || sort.isBlank()) ? "name" : sort;
                Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), Sort.by(direction, sortBy));

                Page<Product> result;
                boolean hasQ = q != null && !q.isBlank();
                boolean hasCat = categoryId != null;

                if (hasQ && hasCat) {
                        result = productRepository.findByNameContainingIgnoreCaseAndCategoryId(q, categoryId, pageable);
                } else if (hasQ) {
                        result = productRepository.findByNameContainingIgnoreCase(q, pageable);
                } else if (hasCat) {
                        result = productRepository.findByCategoryId(categoryId, pageable);
                } else {
                        result = productRepository.findAll(pageable);
                }

                return result.map(product -> ProductResponseDTO.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .price(product.getPrice())
                                .imageUrl(product.getImageUrl())
                                .categoryName(product.getCategory().getName())
                                .build());
        }

    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));

        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .categoryName(product.getCategory().getName())
                .build();
    }

    @Transactional
    public ProductResponseDTO addProduct(ProductRequestDTO dto) {
        Category category = categoryService.getOrCreateByName(dto.getCategoryName());

        Product product = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .stockQuantity(dto.getStockQuantity())
                .imageUrl(dto.getImageUrl())
                .category(category)
                .build();

        Product saved = productRepository.save(product);

        return ProductResponseDTO.builder()
                .id(saved.getId())
                .name(saved.getName())
                .price(saved.getPrice())
                .imageUrl(saved.getImageUrl())
                .categoryName(saved.getCategory().getName())
                .build();
    }

    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("수정할 상품이 존재하지 않습니다."));

        Category category = categoryService.getOrCreateByName(dto.getCategoryName());

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setImageUrl(dto.getImageUrl());
        product.setCategory(category);

        Product updated = productRepository.save(product);

        return ProductResponseDTO.builder()
                .id(updated.getId())
                .name(updated.getName())
                .price(updated.getPrice())
                .imageUrl(updated.getImageUrl())
                .categoryName(updated.getCategory().getName())
                .build();
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
