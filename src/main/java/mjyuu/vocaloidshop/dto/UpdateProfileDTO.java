package mjyuu.vocaloidshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateProfileDTO {
    private String nickname; // optional
    // ISO-8601 yyyy-MM-dd
    private String birthday; // optional
}
