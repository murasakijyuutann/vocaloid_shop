package mjyuu.vocaloidshop.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {

    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private String category;
}
