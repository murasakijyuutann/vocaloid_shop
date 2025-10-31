package mjyuu.vocaloidshop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ContactRequestDTO {
    @NotBlank
    private String senderName;

    @Email @NotBlank
    private String senderEmail;

    @NotBlank
    private String title;

    @NotBlank
    private String details;
}
