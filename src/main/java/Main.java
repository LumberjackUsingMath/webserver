import org.apache.log4j.BasicConfigurator;

import java.io.*;

public class Main {


    public static void main(String[] args){
        BasicConfigurator.configure(); // Configure log4j


        try {
            Server server = new Server(8080, 1, "Data", 100000);
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
