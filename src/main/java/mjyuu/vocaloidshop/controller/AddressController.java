package mjyuu.vocaloidshop.controller;

import lombok.RequiredArgsConstructor;
import mjyuu.vocaloidshop.dto.AddressRequestDTO;
import mjyuu.vocaloidshop.dto.AddressResponseDTO;
import mjyuu.vocaloidshop.service.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<AddressResponseDTO>> list(@PathVariable Long userId) {
        return ResponseEntity.ok(addressService.list(userId));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<AddressResponseDTO> create(@PathVariable Long userId, @RequestBody AddressRequestDTO dto) {
        return ResponseEntity.ok(addressService.create(userId, dto));
    }

    @PutMapping("/{userId}/{addressId}")
    public ResponseEntity<AddressResponseDTO> update(@PathVariable Long userId, @PathVariable Long addressId, @RequestBody AddressRequestDTO dto) {
        return ResponseEntity.ok(addressService.update(userId, addressId, dto));
    }

    @DeleteMapping("/{userId}/{addressId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId, @PathVariable Long addressId) {
        addressService.delete(userId, addressId);
        return ResponseEntity.noContent().build();
    }
}
