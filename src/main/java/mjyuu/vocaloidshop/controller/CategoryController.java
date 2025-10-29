package mjyuu.vocaloidshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mjyuu.vocaloidshop.dto.CategoryRequestDTO;
import mjyuu.vocaloidshop.dto.CategoryResponseDTO;
import mjyuu.vocaloidshop.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@Valid @RequestBody CategoryRequestDTO dto) {
        return ResponseEntity.ok(categoryService.createCategory(dto));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAll() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
