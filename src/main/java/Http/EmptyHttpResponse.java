package Http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * HttpResponse extension that writes an empty response.
 * @author  Julian Grüber (taken from https://github.com/warchildmd/webserver)
 */
public class EmptyHttpResponse extends HttpResponse {
    private final Logger LOGGER = LoggerFactory.getLogger(EmptyHttpResponse.class);

    public EmptyHttpResponse(int statusCode) {
        super();

        this.statusCode = statusCode;
    }

    /**
     * This function writes the HTTP response to an output stream.
     *
     * @param out the target {@link OutputStream} for writing
     */
    public void write(OutputStream out) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(getResponseLine());
            writer.write("\r\n");

            for (String key: headers.keySet()) {
                writer.write(key + ":" + headers.get(key));
                writer.write("\r\n");
            }
            writer.write("\r\n");

            writer.flush();
            writer.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}