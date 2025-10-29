package mjyuu.vocaloidshop.controller;

import lombok.RequiredArgsConstructor;
import mjyuu.vocaloidshop.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place/{userId}")
    public ResponseEntity<Void> placeOrder(@PathVariable Long userId) {
        orderService.placeOrder(userId);
        return ResponseEntity.ok().build();
    }
}
