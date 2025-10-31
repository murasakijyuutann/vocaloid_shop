package mjyuu.vocaloidshop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterRequestDTO {
    @Email( message = "Invalid email format" )
    private String email;

    @NotBlank (message = "Password cannot be blank")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    private String password;

    @NotBlank(message = "Nickname cannot be blank")
    @Size(min = 2, max = 100, message = "Nickname must be between 2 and 100 characters")
    private String nickname; // optional

    // ISO-8601: yyyy-MM-dd
    private String birthday; // optional; parsed to LocalDate
}
