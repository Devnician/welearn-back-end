package uni.ruse.welearn.welearn.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * The auth token
 *
 * @author petar ivanov
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class AuthToken {
    private String message;
    private String token;
    private String username;
    private String firstName;
    private String lastName;
    private String id;
    private long roleId;

}
