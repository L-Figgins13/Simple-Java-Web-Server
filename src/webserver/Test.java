package webserver;
import webserver.HttpdConf;
import webserver.Htpassword;
import webserver.Htaccess;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.Security;
// import ltools.ArrayTools;

/**
 * Created by euphoric on 2/7/17.
 */
class Test {

    public static void main(String[] args) throws IOException{
        test_httpd();


        //
        //
        //     MessageDigest md = MessageDigest.getInstance("SHA");
        //     String password = "Guest";
        //     md.update(password.getBytes());
        //     byte[] messageDigestSha = md.digest();
        //
        //     StringBuffer stringBuffer = new StringBuffer();
        //
        //     for(byte bytes: messageDigestSha) {
        //         stringBuffer.append(String.format("%02x", bytes & 0xff));
        //     }
        //
        //     System.out.println("password: " + password);
        //     System.out.println("hash: " + stringBuffer.toString());
        //
        //
        //
        // } catch (Exception e){
        //     System.out.println("bad algo");
        // }
        //
        // boolean flag = false;
        //
        // Path path = Paths.get("/home/euphoric/IdeaProjects/WebServerProject/config/httpd.conf");
        //
        // for(Path name : path){
        //     System.out.println(name);
        // }
        //
        // if(Files.exists(path)){
        //     System.out.println(path.toString() + " File exists");
        // } else {
        //     System.out.println(path.toString() + " File does not exist");
        // }
        //


    }

    private static void test_httpd()
    {

        String workingDirectory = System.getProperty("user.dir");
        String[] temp = workingDirectory.split("/");
        String path = "/";
        for(int i = 1 ; i < temp.length-2; i++){
          path += temp[i] + "/";
        }

        System.out.println("Path after join: " + path);


        // String[] reducedPath = ArrayTools.reduce(String.class, temp, 2);
        // String path = reducePath.join("/");
        path = path + "config/httpd.conf";

        HttpdConf conf_test = new HttpdConf(path);

        try {
            conf_test.load();
        } catch (Exception e){
            System.out.println(e);
        }
         conf_test.printConfig();
    }

    // static private void test_mime()
    // {
    //     MimeTypes mime_test = new MimeTypes("mime.types");
    //     try{
    //         mime_test.load();
    //         mime_test.printConfig();
    //     } catch (Exception e){
    //         System.out.println(e);
    //     }
    // }
    //
    // static private void test_htpassword(){
    //     Htpassword htp_test = new Htpassword(".htpasswd");
    //     try {
    //         htp_test.load();
    //         htp_test.print_Users();
    //
    //     } catch (Exception e) {
    //         System.out.println(e);
    //     }
    //
    // }
    //
    // static private void test_request_objects() throws IOException
    // {
    //     String string_http_request = "POST /cgi-bin/process.cgi HTTP/1.1\r\n" +
    //             "User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)\r\n"+
    //             "Host: www.tutorialspoint.com\r\n" +
    //             "Content-Type: text/xml; charset=utf-8\r\n" +
    //             "Content-Length: 21\r\n" +
    //             "Accept-Language: en-us\r\n"+
    //             "Accept-Encoding: gzip, deflate\r\n" +
    //             "Connection: Keep-Alive\r\n\r\nHello this is my body";
    //
    //
    //     InputStream test_stream = new ByteArrayInputStream(string_http_request.getBytes());
    //
    //     Request string_request = new Request(string_http_request);
    //
    //     Request request = new Request(test_stream);
    //     request.parse();
    //     System.out.println(request.getRawHttp());
    //
    // }
}
