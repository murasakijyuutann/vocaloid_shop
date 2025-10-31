package mjyuu.vocaloidshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
    private Long id;
    private String email;
    private String name;
    // Ensure JSON property remains "isAdmin" (not "admin") for frontend compatibility
    @JsonProperty("isAdmin")
    private boolean isAdmin;
    private String nickname;
    private java.time.LocalDate birthday;
}
