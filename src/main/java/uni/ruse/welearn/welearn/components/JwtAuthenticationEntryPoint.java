package uni.ruse.welearn.welearn.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import uni.ruse.welearn.welearn.service.IPService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    @Autowired
    private IPService ipService;

    private static final long serialVersionUID = 4708640358299835388L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String ip = ipService.getClientIpAddressFromRequest(request);
        log.info("Incoming request - URI: " + request.getRequestURI());
        log.info("Incoming request - Auth Type: " + request.getAuthType());
        log.info("Incoming request - Path info: " + request.getPathInfo());
        log.info("Incoming request - Context path: " + request.getContextPath());
        log.info("Unauthorized. The ban is near...&-))" + ip);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
