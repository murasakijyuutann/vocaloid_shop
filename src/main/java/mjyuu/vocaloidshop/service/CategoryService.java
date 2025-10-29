package mjyuu.vocaloidshop.service;

import lombok.RequiredArgsConstructor;
import mjyuu.vocaloidshop.dto.CategoryRequestDTO;
import mjyuu.vocaloidshop.dto.CategoryResponseDTO;
import mjyuu.vocaloidshop.entity.Category;
import mjyuu.vocaloidshop.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryResponseDTO createCategory(CategoryRequestDTO dto) {
        Category category = Category.builder()
                .name(dto.getName())
                .build();
        return modelMapper.map(categoryRepository.save(category), CategoryResponseDTO.class);
    }

    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(c -> modelMapper.map(c, CategoryResponseDTO.class))
                .collect(Collectors.toList());
    }

    public Category getOrCreateByName(String name) {
        return categoryRepository.findByName(name)
                .orElseGet(() -> categoryRepository.save(Category.builder().name(name).build()));
    }
}
