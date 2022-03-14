/** Source: https://github.com/warchildmd/webserver/blob/master/src/main/java/me/homework/server/http/HttpMethod.java */
package Http;

public class HttpMethod {

    /**
     * Safe methods
     */
    public static final String OPTIONS = "OPTIONS";
    public static final String GET = "GET";
    public static final String HEAD = "HEAD";
    public static final String TRACE = "TRACE";

    /**
     * Unsafe methods
     */
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    public static final String CONNECT = "CONNECT";
}