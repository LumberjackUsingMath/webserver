package Http;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A {@link HttpResponse} that sends the content of .txt files back to the client
 * @author  Julian Grüber (taken from: https://github.com/warchildmd/webserver)
 * */
public class FileHttpResponse extends HttpResponse {

    /**
     * File to be sent to the user.
     */
    private final File inputFile;


    /**
     *
     * @param statusCode the {@link HttpStatus} of the request that will be sent to the client
     * @param inputFile the .txt file to send back to the client
     */
    public FileHttpResponse(int statusCode, File inputFile) {
        super();

        this.statusCode = statusCode;
        this.inputFile = inputFile;

        try {
            this.setContentType();
            this.setContentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The method to write the content of the inputFile
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

            if (inputFile != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
                char[] buffer = new char[1024];
                int read;
                while ((read = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, read);
                }
                reader.close();
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // NOT in use.
    private void setContentType() throws IOException {
        Path source = Paths.get(this.inputFile.toURI());
        String contentType = Files.probeContentType(source);
        if (contentType != null) {
            headers.put("Content-Type", contentType);
        }
    }

    private void setContentLength() throws IOException {
        headers.put("Content-Length", String.valueOf(this.inputFile.length()));
    }
}

