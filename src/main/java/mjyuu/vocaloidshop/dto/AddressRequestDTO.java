package mjyuu.vocaloidshop.dto;

import lombok.Data;

@Data
public class AddressRequestDTO {
    private String recipientName;
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String phone;
    private Boolean isDefault;
}
