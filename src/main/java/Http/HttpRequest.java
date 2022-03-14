package Http;

import exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;


/**
 * @author  Julian Grüber ( taken from https://github.com/warchildmd/webserver)
 * Contains all the information included in a request.
 * Contains a static method {@link #parse(InputStream, OutputStream)} that creates an {@link HttpRequest} by reading
 * and parsing the data from the socket.
 */
public class HttpRequest {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequest.class);

    /** Input stream from client socket*/
    private InputStream inputStream;

    /**Request line containing protocol version, URI, and request method*/
    private String requestLine;

    /** Request information*/
    private String method, uri, version;

    /** Headers of the request*/
    private HashMap<String, String> headers;

    /** Query parameters */
    private HashMap<String, String> params;

    /** Filename which is requested by the Http request */
    private String filename;

    /** URL query part */
    private String query;

    /** Flag for keep-alive requests */
    private boolean keepAlive = false;


    public HttpRequest() {
        headers = new HashMap<>();
        params = new HashMap<>();
    }

    /**
     * This method creates a new {@link HttpRequest} with the data read form the {@link #inputStream}.
     * Will throw main.exceptions in case of errors.
     *
     * @param inputStream The stream to read from.
     * @return A new instance of {@link HttpRequest} containing information from the request
     * @throws BadRequestException If the request is not properly formatted
     * @throws ConnectionClosedException If there is an error while reading data
     */
    public static HttpRequest parse(InputStream inputStream, OutputStream outputStream) throws BadRequestException, ConnectionClosedException {
        try {
            HttpRequest request = new HttpRequest();
            request.inputStream = inputStream;

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            request.requestLine = reader.readLine();
            if (request.requestLine == null) {
                throw new ConnectionClosedException();
            }

            String[] requestLineParts = request.requestLine.split(" ", 3);
            request.method = requestLineParts[0];
            request.uri = requestLineParts[1];
            request.version = requestLineParts[2];

            LOGGER.info("Method of request is: " + request.requestLine);

            String line = reader.readLine();
            while (!line.equals("")) {
                String[] lineParts = line.split(": ", 2);
                if (lineParts.length == 2) {
                    request.headers.put(lineParts[0], lineParts[1]);
                }
                line = reader.readLine();
            }

            String[] uriParts = request.uri.split("\\?", 2);
            if (uriParts.length == 2) {
                LOGGER.info("URI: " + request.uri);
                request.filename = uriParts[0];
                request.query = uriParts[1];

                String[] keyValuePairs = request.query.split("&");
                for (String keyValuePair : keyValuePairs) {
                    String[] keyValue = keyValuePair.split("=", 2);
                    if (keyValue.length == 2) {
                        request.params.put(keyValue[0], keyValue[1]);
                    }
                }
            } else {
                request.filename = request.headers.getOrDefault("requestedFile", "close");
                LOGGER.info("Filename of request is: " +  request.filename);
                request.query = "";
            }

            if (request.headers.getOrDefault("Connection", "close").equalsIgnoreCase("keep-alive")) {
                System.out.println("Set keep alive to true");
                request.keepAlive = true;
            }

            return request;
        } catch(ConnectionClosedException e) {
            throw e;
        } catch (Exception e) {
            new RawHttpRequest(400, "main.Server only accepts HTTP protocol").write(outputStream);
            throw new BadRequestException();
        }
    }



    public String getMethod() {
        return method;
    }

    public String getFilename() {
        return filename;
    }

    public boolean getKeepAlive() {
        return keepAlive;
    }
}

