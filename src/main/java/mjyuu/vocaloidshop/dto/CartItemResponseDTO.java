package mjyuu.vocaloidshop.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CartItemResponseDTO {

    private Long productId;
    private String productName;
    private int price;
    private int quantity;
    private int totalPrice;
}
