import Applications.FileServerApp;
import Applications.WebApplication;
import Http.*;
import exceptions.BadRequestException;
import exceptions.ConnectionClosedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * The class that handles the incoming clients implemented as Runnable, such that a thread pool can execute it.
 *
 * @author Julian Grüber
 *
 * Source: www.codejava.net
 */
public class ClientHandler implements Runnable{

    private final static Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);

    private final Socket client; // One client handles  exactly one client, and shuts down after it is done
    private WebApplication app; // The application to execute

    /**
     * Specify to apply keep alive. Is the to true for now.
     */
    private boolean keepAlive = true;

    /**
     * Constructor of client handler with keep-alive behaviour following the logic of the article
     * https://stackoverflow.com/questions/9334401/http-keep-alive-and-tcp-keep-alive
     * @param client a client of Class @see Socket
     * @param app the @see WebApplication to execute
     *
     */
    public ClientHandler(Socket client, WebApplication app) {
        super();
        this.client = client;
        this.app = app;
    }

    /**
     * The run method which specifies what the ClientHandler does while being executed by a thread */
    public void run() {
        try {

            LOGGER.info("Start with execution of client");
            OutputStream clientOut = client.getOutputStream();

            /** Parse the raw http request tp proper @see HttpRequest. As this also verify whether the request is good,
             * we need to catch the possible main.exceptions, in order to inform the client. Therefore, we need to put it
             * into an inner try and catch clause, as  */

            HttpRequest request = HttpRequest.parse(client.getInputStream(), clientOut);
            client.setKeepAlive(request.getKeepAlive());
            HttpResponse response = app.handle(request);

            response.write(clientOut); // Send out the response


            if(this.keepAlive && !request.getKeepAlive()){
                client.close();
                LOGGER.info("Close connection");
            }


        } catch (IOException e) {
            LOGGER.error("main.Server exception: " + e.getMessage());
            e.printStackTrace();
        } catch (BadRequestException e) {
            e.printStackTrace();
            LOGGER.error("main.Server exception: " + e.getMessage());
        } catch (ConnectionClosedException e) {
            e.printStackTrace();
            LOGGER.error("main.Server exception: " + e.getMessage());
        }
    }
}