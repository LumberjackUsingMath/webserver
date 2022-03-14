import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;



import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;



public class ClientTest {
    private Thread server; // Thread to let the server run
    private final int port = 8080; // Set port

    @Before
    public void beforeTest() throws IOException {
        server = new Thread( new Server(port, 1, "Data", 100000));
        server.start();


    }

    @After
    public void afterTest(){
        this.server.interrupt();
    }


    @Test
    public void request() throws IOException {

        /* Create client to send requests */
        CloseableHttpClient httpclient = HttpClients.createDefault();


        // Request an existing fle
        HttpGet httpGet = new HttpGet("http://localhost:" + port);
        httpGet.addHeader("requestedFile", "file1.txt");

        CloseableHttpResponse response =  httpclient.execute(httpGet);
        Assert.assertEquals(response.getCode(), 200);
        response.close();


        // Requesting a non-existing File
        httpGet.addHeader("requestedFile", "filex.txt");
        response =  httpclient.execute(httpGet);
        Assert.assertEquals(response.getCode(), 404);
        response.close();


        // Requesting a not-implemented method
        HttpPost httpPost = new HttpPost("http://localhost:" + port);
        response =  httpclient.execute(httpPost);
        Assert.assertEquals(response.getCode(), 501);




        /* ---- Not working ---
        Idea to test multithreading via extending the class Thread, in order forward the exception to the test
        //TODO: Fix



        ClientThread c1 = new ClientThread(port);
        ClientThread c2 = new ClientThread(port);
        ClientThread c3 = new ClientThread(port);

        c1.start();c2.start();c3.start();

        Assert.assertNull(c1.getException());
        Assert.assertNull(c2.getException());
        Assert.assertNull(c3.getException());
         */




    }
}
