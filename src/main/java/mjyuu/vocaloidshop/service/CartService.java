package mjyuu.vocaloidshop.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mjyuu.vocaloidshop.dto.AddToCartRequestDTO;
import mjyuu.vocaloidshop.dto.CartItemResponseDTO;
import mjyuu.vocaloidshop.entity.CartItem;
import mjyuu.vocaloidshop.entity.Product;
import mjyuu.vocaloidshop.entity.User;
import mjyuu.vocaloidshop.repository.CartItemRepository;
import mjyuu.vocaloidshop.repository.ProductRepository;
import mjyuu.vocaloidshop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void addToCart(AddToCartRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자 없음"));
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("상품 없음"));

        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(user.getId(), product.getId())
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + dto.getQuantity());
                    return existing;
                })
                .orElse(CartItem.builder()
                        .user(user)
                        .product(product)
                        .quantity(dto.getQuantity())
                        .build());

        cartItemRepository.save(cartItem);
    }

    public List<CartItemResponseDTO> getUserCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        return cartItemRepository.findByUser(user).stream()
                .map(item -> CartItemResponseDTO.builder()
                        .cartItemId(item.getId()) // ✅ include ID
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .price(item.getProduct().getPrice())
                        .quantity(item.getQuantity())
                        .totalPrice(item.getProduct().getPrice() * item.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

        @Transactional
        public void decrementCartItem(Long cartItemId) {
                CartItem item = cartItemRepository.findById(cartItemId)
                                .orElseThrow(() -> new RuntimeException("장바구니 항목 없음"));

                int currentQty = item.getQuantity();
                if (currentQty > 1) {
                        item.setQuantity(currentQty - 1);
                        cartItemRepository.save(item);
                } else {
                        cartItemRepository.deleteById(cartItemId);
                }
        }
}
