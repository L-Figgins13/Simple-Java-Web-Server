package webserver;

/**
 * Created by euphoric on 2/22/17.
 */
class ServerStarter
{
    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start();
        } catch (Exception e) {
            System.out.println("Server Failed to start");
            System.out.println(e);
        }
    }

}
