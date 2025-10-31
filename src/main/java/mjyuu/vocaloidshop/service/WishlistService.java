package mjyuu.vocaloidshop.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mjyuu.vocaloidshop.dto.WishlistItemResponseDTO;
import mjyuu.vocaloidshop.entity.Product;
import mjyuu.vocaloidshop.entity.User;
import mjyuu.vocaloidshop.entity.WishlistItem;
import mjyuu.vocaloidshop.repository.ProductRepository;
import mjyuu.vocaloidshop.repository.UserRepository;
import mjyuu.vocaloidshop.repository.WishlistItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistItemRepository wishlistItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public List<WishlistItemResponseDTO> list(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return wishlistItemRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(w -> WishlistItemResponseDTO.builder()
                        .id(w.getId())
                        .productId(w.getProduct().getId())
                        .productName(w.getProduct().getName())
                        .imageUrl(w.getProduct().getImageUrl())
                        .price(w.getProduct().getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void add(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();
        if (wishlistItemRepository.existsByUserAndProduct(user, product)) return;
        WishlistItem item = WishlistItem.builder()
                .user(user)
                .product(product)
                .createdAt(LocalDateTime.now())
                .build();
        wishlistItemRepository.save(item);
    }

    @Transactional
    public void remove(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();
        wishlistItemRepository.findByUserAndProduct(user, product)
                .ifPresent(wishlistItemRepository::delete);
    }
}
