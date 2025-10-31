package mjyuu.vocaloidshop.controller;

import lombok.RequiredArgsConstructor;
import mjyuu.vocaloidshop.dto.OrderItemResponseDTO;
import mjyuu.vocaloidshop.dto.OrderResponseDTO;
import mjyuu.vocaloidshop.entity.Order;
import mjyuu.vocaloidshop.entity.OrderStatus;
import mjyuu.vocaloidshop.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place/{userId}")
    public ResponseEntity<Void> placeOrder(@PathVariable Long userId,
                                           @RequestParam(required = false) Long addressId) {
        orderService.placeOrder(userId, addressId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> listUserOrders(@PathVariable Long userId) {
        List<Order> orders = orderService.listUserOrders(userId);
    List<OrderResponseDTO> result = orders.stream().map(o -> OrderResponseDTO.builder()
                .id(o.getId())
                .orderedAt(o.getOrderedAt())
                .totalAmount(o.getTotalAmount())
        .status(o.getStatus() != null ? o.getStatus().name() : null)
        .shipRecipientName(o.getShipRecipientName())
        .shipLine1(o.getShipLine1())
        .shipLine2(o.getShipLine2())
        .shipCity(o.getShipCity())
        .shipState(o.getShipState())
        .shipPostalCode(o.getShipPostalCode())
        .shipCountry(o.getShipCountry())
        .shipPhone(o.getShipPhone())
                .items(o.getItems().stream().map(i -> OrderItemResponseDTO.builder()
                        .id(i.getId())
                        .productId(i.getProduct().getId())
                        .productName(i.getProduct().getName())
                        .price(i.getPrice())
                        .quantity(i.getQuantity())
                        .build()).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // Admin (or internal) endpoints - secure via role in SecurityConfig if roles are added
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> listAll() {
        List<Order> orders = orderService.listAllOrders();
        List<OrderResponseDTO> result = orders.stream().map(o -> OrderResponseDTO.builder()
                .id(o.getId())
                .orderedAt(o.getOrderedAt())
                .totalAmount(o.getTotalAmount())
                .status(o.getStatus() != null ? o.getStatus().name() : null)
                .shipRecipientName(o.getShipRecipientName())
                .shipLine1(o.getShipLine1())
                .shipLine2(o.getShipLine2())
                .shipCity(o.getShipCity())
                .shipState(o.getShipState())
                .shipPostalCode(o.getShipPostalCode())
                .shipCountry(o.getShipCountry())
                .shipPhone(o.getShipPhone())
                .items(o.getItems().stream().map(i -> OrderItemResponseDTO.builder()
                        .id(i.getId())
                        .productId(i.getProduct().getId())
                        .productName(i.getProduct().getName())
                        .price(i.getPrice())
                        .quantity(i.getQuantity())
                        .build()).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDTO> updateStatus(@PathVariable Long orderId,
                                                         @RequestParam String status) {
        OrderStatus next = parseStatus(status);
        if (next == null) {
            return ResponseEntity.badRequest().build();
        }
        Order updated = orderService.updateOrderStatus(orderId, next);
        OrderResponseDTO dto = OrderResponseDTO.builder()
                .id(updated.getId())
                .orderedAt(updated.getOrderedAt())
                .totalAmount(updated.getTotalAmount())
                .status(updated.getStatus() != null ? updated.getStatus().name() : null)
                .shipRecipientName(updated.getShipRecipientName())
                .shipLine1(updated.getShipLine1())
                .shipLine2(updated.getShipLine2())
                .shipCity(updated.getShipCity())
                .shipState(updated.getShipState())
                .shipPostalCode(updated.getShipPostalCode())
                .shipCountry(updated.getShipCountry())
                .shipPhone(updated.getShipPhone())
                .items(updated.getItems().stream().map(i -> OrderItemResponseDTO.builder()
                        .id(i.getId())
                        .productId(i.getProduct().getId())
                        .productName(i.getProduct().getName())
                        .price(i.getPrice())
                        .quantity(i.getQuantity())
                        .build()).collect(Collectors.toList()))
                .build();
        return ResponseEntity.ok(dto);
    }

    private OrderStatus parseStatus(String raw) {
        if (raw == null) return null;
        String norm = raw.trim().toUpperCase().replace('-', '_').replace(' ', '_');
        switch (norm) {
            case "PAID":
            case "PAYED":
            case "PAYMENT_CONFIRMED":
            case "PAYMENT":
            case "PAYMENT_RECEIVED":
                return OrderStatus.PAYMENT_RECEIVED;
            case "PROCESSING":
                return OrderStatus.PROCESSING;
            case "PREPARING":
            case "PACKING":
            case "PICKING":
                return OrderStatus.PREPARING;
            case "READY_FOR_DELIVERY":
            case "READY_FOR_SHIPMENT":
            case "READY_TO_SHIP":
                return OrderStatus.READY_FOR_DELIVERY;
            case "IN_DELIVERY":
            case "SHIPPED":
            case "OUT_FOR_DELIVERY":
            case "IN_TRANSIT":
                return OrderStatus.IN_DELIVERY;
            case "DELIVERED":
            case "COMPLETED":
                return OrderStatus.DELIVERED;
            case "CANCELED":
            case "CANCELLED":
                return OrderStatus.CANCELED;
            default:
                try { return OrderStatus.valueOf(norm); } catch (Exception ignored) { return null; }
        }
    }
}
