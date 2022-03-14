package Applications;

import Http.HttpRequest;
import Http.HttpResponse;



/** Interface for the web application.
 * @author Julian Grüber (taken from https://github.com/warchildmd/webserver)
 * */
public interface WebApplication {

    /**
     * Abstract method that defines how the WebApplication handles an incoming {@link HttpRequest}
     * @param request a {@link HttpRequest} that will be handled
     * @return a {@link HttpResponse}
     */
    HttpResponse handle(HttpRequest request);
}
