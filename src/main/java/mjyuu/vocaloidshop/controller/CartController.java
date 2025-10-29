package mjyuu.vocaloidshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mjyuu.vocaloidshop.dto.AddToCartRequestDTO;
import mjyuu.vocaloidshop.dto.CartItemResponseDTO;
import mjyuu.vocaloidshop.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<Void> addToCart(@Valid @RequestBody AddToCartRequestDTO dto) {
        cartService.addToCart(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemResponseDTO>> viewCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getUserCart(userId));
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId) {
        cartService.removeCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }
}
