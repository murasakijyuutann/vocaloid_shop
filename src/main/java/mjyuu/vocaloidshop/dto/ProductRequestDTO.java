package mjyuu.vocaloidshop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDTO {

    @NotBlank(message = "상품명을 입력하세요")
    private String name;

    private String description;

    @Min(value = 100, message = "가격은 100원 이상이어야 합니다")
    private int price;

    @Min(value = 0, message = "재고는 0 이상이어야 합니다")
    private int stockQuantity;

    private String imageUrl;

    private String category;
}
