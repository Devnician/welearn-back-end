package uni.ruse.welearn.welearn.util;

/**
 * Static constants
 *
 * @author petar ivanov
 */
public class Constants {

    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 8 * 60 * 60;
    public static final long ACCESS_TOKEN_VALIDITY_MILLISECONDS = 8 * 60 * 60 * 1000;

    public static final String SIGNING_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOI6XJhbSJ9ZDNQMZu2s6h3zziMTU1MTA5MjQzMiwidXaCIsImNyZWF0ZWRBdCNlcklkiOiJmLCJwYXJhbTEiOiJwY";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    public static final String USER_ID = "userId";
    public static final String ROLE_ID = "roleId";
    public static final String USERNAME = "username";
}
