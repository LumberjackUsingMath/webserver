import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 *
 * A runnable client class that implements a test client as Runnable, in order to test the multithreading of the server
 * It works technically, but I struggled to properly test it assert statements, as run method is not allow to throw an expection
 */
public class ClientThread extends Thread{

    private Logger LOGGER = LoggerFactory.getLogger(ClientThread.class);

    private int port;
    private Exception ex =null;

    public ClientThread(int port){
        this.port = port;
        LOGGER.info("ClientThread created");

    }

    @Override
    public void run() {
        LOGGER.info("ClientThread run");


        CloseableHttpClient httpclient = HttpClients.createDefault();


        // Request an existing fle
        HttpGet httpGet = new HttpGet("http://localhost:" + port);
        httpGet.addHeader("requestedFile", "file1.txt");
        try {
            LOGGER.info("Execute");
            CloseableHttpResponse response = httpclient.execute(httpGet);
            LOGGER.info("Assert");
            Assert.assertEquals(response.getCode(), 200);
            LOGGER.info("Close");
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
            this.ex = e;
            LOGGER.error(e.getMessage());
        }
    }


    public Object getException() {
        return this.ex;
    }
}
