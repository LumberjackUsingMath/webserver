/**
 * @
 */


import Applications.FileServerApp;
import Applications.WebApplication;
import com.ibm.icu.impl.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * The class to host a webserver
 * @author Julian Grüber
 */
public class Server implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(Server.class);

    /**
     * The servr socket to accept clients
     */
    private ServerSocket server;

    /**
     * The number of workers to use in the thread-pool
     */
    private int numberOfThreads;
    private ExecutorService pool;

    /**
     * The Application that the serer will execute
     */
    private WebApplication app;

    private int clientsConnected=0;


    /**
     * Constructor of the webserver to host a web application
     * @param port positive integer
     * @param numberOfThreads amount of worker that will be used by the theardpool
     * @param timeoutServer in milliseconds
     * @throws IOException
     * @throws IllegalArgumentException
     * */
    public Server( int port, int numberOfThreads, String dataDirectory, int timeoutServer) throws IOException, IllegalArgumentException {


        LOGGER.info("Create Server on port " + port + " with " + numberOfThreads + " threads, and timeout " + timeoutServer);

        this.numberOfThreads = numberOfThreads;

        // specify the application to use
        this.app = new FileServerApp(dataDirectory);

        pool = Executors.newFixedThreadPool(numberOfThreads);

        // Host the server
        server = new ServerSocket(port);
        server.setSoTimeout(timeoutServer);

    }

    /**
     * Method to start the server and let it run. The server will run until the timeout, which was set in the
     * constructor, becomes effective
     */
    @Override
    public void run(){
        LOGGER.info("Waiting for client on port " + server.getLocalPort());
        LOGGER.info("Using " + this.numberOfThreads + " threads");


        while(true){
            try {
                //Socket client  = server.accept();

                Socket client = server.accept();
                client.setSoTimeout(7000); // Set timeout for keep-alive behavior

                pool.execute(new ClientHandler(client, app)); // Add the client to the queue
                clientsConnected++;
                LOGGER.info("New client connected. Number: " + clientsConnected);
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
                e.printStackTrace();
                break;
            }


        }
        LOGGER.info("Server is shutting down");
    }

}
