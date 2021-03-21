package uni.ruse.welearn.welearn.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import uni.ruse.welearn.welearn.model.User;
import uni.ruse.welearn.welearn.services.IPService;
import uni.ruse.welearn.welearn.services.UserService;
import uni.ruse.welearn.welearn.util.JwtTokenUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static uni.ruse.welearn.welearn.util.Constants.HEADER_STRING;
import static uni.ruse.welearn.welearn.util.Constants.ROLE_ID;
import static uni.ruse.welearn.welearn.util.Constants.TOKEN_PREFIX;
import static uni.ruse.welearn.welearn.util.Constants.USER_ID;


/**
 * Filter with chained authentication check
 *
 * @author petar ivanov
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private IPService ipService;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);
        String username = null;
        String authToken = null;
        new User();
        User user;
        String ip = ipService.getClientIpAddressFromRequest(req);
        logger.debug(ip);

        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            authToken = header.replace(TOKEN_PREFIX, "");
            if (authToken.isEmpty()) {
                res.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            try {
                String userId = jwtTokenUtil.getUserPropFromTokenAsString(authToken, USER_ID);
                int roleId = jwtTokenUtil.getUserPropFromTokenAsInt(authToken, ROLE_ID);
                username = jwtTokenUtil.getUsernameFromToken(authToken);
                /**
                 * Compare token info with user info:
                 */
                user = userService.findUserById(userId);
                if (user == null) {
                    throw new IllegalArgumentException("No such user");
                } else {
                    if (user.getRole().getId() != roleId || !user.getUsername().equals(username)) {
                        username = null;
                        // userService.kickUser(user);
                        System.out.println("This token is not on this user..");
                        res.sendError(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    }
                }
            } catch (IllegalArgumentException e) {
                logger.error("an error occured during getting username from token", e);
            } catch (ExpiredJwtException e) {
                System.out.println("Expired, than logout...");
                // userService.kickUser(user);
                logger.warn("the token is expired and not valid anymore", e);
            } catch (SignatureException e) {
                logger.error("Authentication Failed. Username or Password not valid.");
            }
        } else {
            logger.warn("couldn't find bearer string, will ignore the header");
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                logger.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                System.out.println("LOGOUT FROM HERE --- user: " + username);
            }
        }
        chain.doFilter(req, res);
    }
}
