package uni.ruse.welearn.welearn.model.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The auth token
 *
 * @author petar ivanov
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthToken {
    private String token;
    private String username;
    private String firstName;
    private String lastName;
    private String id;
    private long roleId;

}
