package mjyuu.vocaloidshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mjyuu.vocaloidshop.dto.ContactRequestDTO;
import mjyuu.vocaloidshop.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<Void> send(@Valid @RequestBody ContactRequestDTO dto) {
        contactService.send(dto.getSenderName(), dto.getSenderEmail(), dto.getTitle(), dto.getDetails());
        return ResponseEntity.ok().build();
    }
}
