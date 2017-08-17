package webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Path;

/**
 * Created by euphoric on 2/11/17.
 */
class Worker extends Thread
{
    private Socket client;
    private MimeTypes mimes;
    private HttpdConf config;

    Worker(Socket socket, HttpdConf config, MimeTypes mimes){
        this.client = socket;
        this.config = config;
        this.mimes = mimes;
    }

    @Override
    public void run() {
        super.run();

        try {
            ResponseFactory factory = new ResponseFactory();
            Request request = new Request(client.getInputStream());
            request.parse();

            Resource resource = new Resource(request.getUri(), this.config);
            Response response = factory.getResponse(request,resource);

            //TODO log shit
            response.prepare();
            response.prepareResponseHeaders(this.mimes);

            Logger logger = new Logger(this.config.getLogFile());
            logger.write(request,response);

            response.send(client.getOutputStream());
            client.close();

        } catch (BadRequest e){
            System.out.println("Bad Request 400 Goes here");
            Response badRequest = ResponseFactory.getBadRequestResponse();

            try {
                badRequest.send(client.getOutputStream());
            } catch (IOException ioe) {
                System.out.println("io exception");
                System.out.println(ioe);
            }

            //TODO create 400 response
        } catch(IOException e){
            System.out.println(e);
            System.out.println("IO error");
            System.out.println("500");
        } finally {
            try {
                client.close();
            } catch (Exception e){
                    System.out.println("client not closes");
                }
        }
        System.out.println("thread exits");
    }
}
