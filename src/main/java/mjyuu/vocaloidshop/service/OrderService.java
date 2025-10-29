package mjyuu.vocaloidshop.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mjyuu.vocaloidshop.entity.*;
import mjyuu.vocaloidshop.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public void placeOrder(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("장바구니가 비어있습니다.");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        int totalAmount = 0;

        for (CartItem cart : cartItems) {
            Product product = cart.getProduct();

            // Check stock
            if (product.getStockQuantity() < cart.getQuantity()) {
                throw new RuntimeException("재고 부족: " + product.getName());
            }

            // Deduct stock
            product.setStockQuantity(product.getStockQuantity() - cart.getQuantity());

            // Create order item
            OrderItem item = OrderItem.builder()
                    .product(product)
                    .price(product.getPrice())
                    .quantity(cart.getQuantity())
                    .build();

            orderItems.add(item);
            totalAmount += item.getPrice() * item.getQuantity();
        }

        // Save Order
        Order order = Order.builder()
                .user(user)
                .orderedAt(LocalDateTime.now())
                .totalAmount(totalAmount)
                .items(orderItems)
                .build();

        // Link back
        orderItems.forEach(i -> i.setOrder(order));

        orderRepository.save(order);

        // Clear cart
        cartItemRepository.deleteAll(cartItems);
    }
}
