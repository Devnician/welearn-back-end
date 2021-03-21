/**
 *
 */
package uni.ruse.welearn.welearn.services;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Service that wires all related repositories and methods for processing and
 * retrieving information.
 *
 * @author petar ivanov
 *
 */
@Service(value = "ipService")
public class IPService {

//	@Autowired
//	IpLogsRepository ipLogsRepository;

    public String getClientIpAddressFromRequest(HttpServletRequest request) {
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    private final String[] IP_HEADER_CANDIDATES = {"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR"};

}
