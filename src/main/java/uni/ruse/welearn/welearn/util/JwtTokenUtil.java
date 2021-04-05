package uni.ruse.welearn.welearn.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import uni.ruse.welearn.welearn.model.Role;
import uni.ruse.welearn.welearn.model.User;
import uni.ruse.welearn.welearn.service.RoleService;
import uni.ruse.welearn.welearn.service.UserService;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Function;

import static uni.ruse.welearn.welearn.util.Constants.ACCESS_TOKEN_VALIDITY_SECONDS;
import static uni.ruse.welearn.welearn.util.Constants.ROLE_ID;
import static uni.ruse.welearn.welearn.util.Constants.SIGNING_KEY;
import static uni.ruse.welearn.welearn.util.Constants.USERNAME;
import static uni.ruse.welearn.welearn.util.Constants.USER_ID;


/**
 * Copmonent with utility methods for token processing
 *
 * @author petar ivanov
 */
@Component
@Slf4j
public class JwtTokenUtil implements Serializable {

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    /**
     *
     */
    private static final long serialVersionUID = -517125042817294278L;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getUserPropFromTokenAsString(String token, String claim) {
        final Claims claims = getAllClaimsFromToken(token);
        return (String) claims.get(claim);
    }

    public int getUserPropFromTokenAsInt(String token, String claim) {
        final Claims claims = getAllClaimsFromToken(token);
        return (int) claims.get(claim);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(SIGNING_KEY).parseClaimsJws(token).getBody();
    }

    public String generateToken(User user, Role role) {
        return doGenerateToken(user, role);
    }

    private String doGenerateToken(User user, Role role) {
        String subject = user.getUsername();

        Claims claims = Jwts.claims().setSubject(subject);

        claims.put("scopes", Arrays.asList(new SimpleGrantedAuthority(role.getRole())));
        claims.put(USER_ID, user.getUserId());
        claims.put(ROLE_ID, user.getRole().getId());
        claims.put(USERNAME, user.getUsername());

        return Jwts.builder().setClaims(claims).setIssuer("uni.ruse.welearn")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY).compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

}
