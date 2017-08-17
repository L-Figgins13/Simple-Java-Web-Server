package webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * Created by euphoric on 2/11/17.
 */
class Server {
    private HttpdConf httpdconf;
    private MimeTypes mimes;

    private ServerSocket socket;
    private Map<String, Htpassword> headers;

    Server()
    {
        String workingDirectory = System.getProperty("user.dir");
        System.out.println(workingDirectory);
        loadHttpconf();
        loadMimeTypes();
        // String workingDirectory = System.getProperty("user.dir");
        // System.out.println(workingDirectory);
    }

    public void start() throws IOException
    {
        this.socket = new ServerSocket(this.httpdconf.getListen());
        this.socket.setReuseAddress(true);

        while (true) {
            try {
                Socket clientConnection = this.socket.accept();
                Thread thread = new Worker(clientConnection, this.httpdconf, this.mimes);
                thread.start();

            } catch (Exception e) {
                System.out.println("error making thread or some shit");

            }
        }
    }

    private void loadHttpconf()
    {
      String workingDirectory = System.getProperty("user.dir");
      String[] temp = workingDirectory.split("/");
      String path = "/";

      for(int i = 1 ; i < temp.length-1; i++){
        path += temp[i] + "/";
      }

      System.out.println("Path after join: " + path);
      path = path + "config/httpd.conf";

      httpdconf = new HttpdConf(path);



      try { httpdconf.load();} catch (Exception e) {System.out.println("error loading httpd config file");}

      //TODO REMOVE
      httpdconf.printConfig();
    }

    private void loadMimeTypes() {
      String workingDirectory = System.getProperty("user.dir");
      String[] temp = workingDirectory.split("/");
      String path = "/";

      for(int i = 1 ; i < temp.length-1; i++){
        path += temp[i] + "/";
      }

      System.out.println("Path after join: " + path);
      path = path + "config/mime.types";

      mimes = new MimeTypes(path);
      try {mimes.load();} catch (Exception e) { System.out.println("error loading mimetypes file\n" + e);}

      mimes.printConfig();
    }


}
