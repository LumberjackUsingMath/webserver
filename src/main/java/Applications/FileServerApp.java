package Applications;

import Http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * File server application class that implements what a file based application which can be executed by a server
 * @see WebApplication
 * @author Julian Grüber (taken from https://github.com/warchildmd/webserver)
 */
public class FileServerApp implements WebApplication{
    private final Logger LOGGER = LoggerFactory.getLogger(FileServerApp.class);
    private final String dataDirectory;


    public FileServerApp( String dataDirectory) {
        this.dataDirectory = dataDirectory;
    }


    /**
     * The method that handles a {@link HttpRequest}
     * @param request the {@link HttpRequest} to handle
     * @return a {@link HttpResponse} to execute
     */
    @Override
    public HttpResponse handle(HttpRequest request) {

        /* Get name of requested file */
        String filename = request.getFilename();

        LOGGER.info("Filename in Application: "  + filename);

        HttpResponse response;

        switch (request.getMethod()) {
            case HttpMethod.GET:
                Path requestedFile = Paths.get(dataDirectory, filename);
                LOGGER.info("the requested file is: " + requestedFile);
                if (requestedFile.normalize().startsWith(Paths.get(dataDirectory).normalize())) {
                    if (Files.exists(requestedFile)) {
                        LOGGER.info("File exists");
                        if (Files.isDirectory(requestedFile)) {
                            response = new EmptyHttpResponse(HttpStatus.FORBIDDEN);
                        } else {
                            response = new FileHttpResponse(HttpStatus.OK,
                                    new File(Paths.get(dataDirectory, filename).toString()));
                        }
                    } else {
                        response = new EmptyHttpResponse(HttpStatus.NOT_FOUND);
                    }
                } else {
                    response = new EmptyHttpResponse(HttpStatus.FORBIDDEN);
                }
                break;
            default:
                response = new EmptyHttpResponse(HttpStatus.NOT_IMPLEMENTED);
                break;
        }


        return response;
    }
}
