package mjyuu.vocaloidshop.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mjyuu.vocaloidshop.entity.*;
import mjyuu.vocaloidshop.repository.AddressRepository;
import mjyuu.vocaloidshop.repository.CartItemRepository;
import mjyuu.vocaloidshop.repository.OrderRepository;
import mjyuu.vocaloidshop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;

    @Transactional
    public void placeOrder(Long userId, Long addressId) {
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
    Order.OrderBuilder orderBuilder = Order.builder()
                .user(user)
                .orderedAt(LocalDateTime.now())
                .totalAmount(totalAmount)
        .items(orderItems)
        .status(OrderStatus.PAYMENT_RECEIVED);

    if (addressId != null) {
        Address a = addressRepository.findById(addressId).orElseThrow(() -> new RuntimeException("주소 없음"));
        if (!a.getUser().getId().equals(user.getId())) throw new RuntimeException("권한 없음");
        orderBuilder
            .shipRecipientName(a.getRecipientName())
            .shipLine1(a.getLine1())
            .shipLine2(a.getLine2())
            .shipCity(a.getCity())
            .shipState(a.getState())
            .shipPostalCode(a.getPostalCode())
            .shipCountry(a.getCountry())
            .shipPhone(a.getPhone());
    }

    Order order = orderBuilder.build();

        // Link back
        orderItems.forEach(i -> i.setOrder(order));

        orderRepository.save(order);

        // Clear cart
        cartItemRepository.deleteAll(cartItems);
    }

    public List<Order> listUserOrders(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));
        return orderRepository.findByUserOrderByOrderedAtDesc(user);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus next) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문 없음"));

        // Basic transition guardrail: allow forward moves and cancel anytime
        if (next == OrderStatus.CANCELED) {
            order.setStatus(OrderStatus.CANCELED);
            return order;
        }

        // Define a simple forward-only sequence
        OrderStatus current = order.getStatus();
        if (current == null) {
            order.setStatus(OrderStatus.PAYMENT_RECEIVED);
            current = OrderStatus.PAYMENT_RECEIVED;
        }

        // Accept any forward step (including skipping ahead), but not backward
        int idxCurrent = indexOf(current);
        int idxNext = indexOf(next);
        if (idxNext < idxCurrent && next != OrderStatus.CANCELED) {
            throw new RuntimeException("상태를 이전 단계로 되돌릴 수 없습니다.");
        }

        order.setStatus(next);
        return order;
    }

    private int indexOf(OrderStatus s) {
        // Maintain the sequence order here
        return switch (s) {
            case PAYMENT_RECEIVED -> 0;
            case PROCESSING -> 1;
            case PREPARING -> 2;
            case READY_FOR_DELIVERY -> 3;
            case IN_DELIVERY -> 4;
            case DELIVERED -> 5;
            case CANCELED -> 6; // terminal
        };
    }

    public List<Order> listAllOrders() {
        return orderRepository.findAll();
    }
}
