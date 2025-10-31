package mjyuu.vocaloidshop.controller;

import lombok.RequiredArgsConstructor;
import mjyuu.vocaloidshop.dto.WishlistItemResponseDTO;
import mjyuu.vocaloidshop.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<WishlistItemResponseDTO>> list(@PathVariable Long userId) {
        return ResponseEntity.ok(wishlistService.list(userId));
    }

    @PostMapping("/{userId}/{productId}")
    public ResponseEntity<Void> add(@PathVariable Long userId, @PathVariable Long productId) {
        wishlistService.add(userId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<Void> remove(@PathVariable Long userId, @PathVariable Long productId) {
        wishlistService.remove(userId, productId);
        return ResponseEntity.noContent().build();
    }
}
