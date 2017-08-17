package webserver;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by euphoric on 2/17/17.
 */
class ScriptResponse extends Response{
    //private Request request;
    private Map<String, LinkedList<String>> requestHeaders;
    private String queryString;
    private byte[] requestBody;

    public ScriptResponse(Resource resource) {
        //do not.... use this constructor <<<<<<<<<<<<<<<<>>>>>>>>>>>
        super(resource);
    }

    public ScriptResponse(Resource resource , Request request){
        this.requestHeaders = request.getHeaders();
        this.requestBody = request.getBodyBytes();
        this.code = "200";
        this.reasonPhrase = "OK";
    }



    public void prepare(){
        prepareGeneralHeaders();
    }

    public void prepareResponseHeaders(MimeTypes mime){}

    @Override
    public void send(OutputStream out)
    {
        sendStatusLine(out);
        sendGeneralHeaders(out);

        ProcessBuilder builder = new ProcessBuilder(this.resource.absolutePath());
        setEnviroment(builder.environment());

        try {
            Process process = builder.start();
            if(requestBody != null) {
                OutputStream processOutputStream = process.getOutputStream();
                processOutputStream.write(this.requestBody);
            }

            //InputStream inputStream = process.getInputStream();
            //byte[] response = new byte[];
            //DataInputStream ds = new DataInputStream(inputStream);

           //tring line = ds.readLine();

           //hile(line != null) {

           //




        } catch (Exception e) {
            System.out.println(e);
        }


    }

    private void sendStatusLine(OutputStream out)
    {
        String statusLine = "HTTP/1.1 " + this.code + " " + this.reasonPhrase +"\r\n";

        try {
            out.write(statusLine.getBytes());
        } catch (Exception e){

        }
    }

    private void sendGeneralHeaders(OutputStream out){
        this.headers.forEach((key, list) -> {
            String header = key + ":";
            String values = " ";

            if (key.equals("Content-Length")) {
                this.contentLength = Integer.parseInt(list.get(0));
                this.hasBody = true;
            }

            int counter = 1;
            for (String element : list) {
                values += " " + element;
                if (counter != list.size()) {
                    values += ";";
                }
            }
            try {
                System.out.print(header + values + "\r\n");
                String headerLine = header + values + "\r\n";
                int byteLength = headerLine.getBytes("UTF-8").length;
                byte[] headerBytes = new byte[byteLength];
                headerBytes = headerLine.getBytes("UTF-8");
                out.write(headerBytes, 0, byteLength);
            } catch (Exception e) {
                System.out.println("I fucked up");
            }
        });
    }

    private void sendResponeHeaders(OutputStream out) {}

    private void setEnviroment(Map<String,String> environment)
    {
        requestHeaders.forEach((key, value) -> {
            // a bandaid how does the cgi account for headers with multiple fields?
          environment.put("HTTP_" + key.toUpperCase(), value.getFirst());
        });

        environment.put("SERVER_PROTOCOL", "HTTP/1.1");

        if(this.queryString != null){
            environment.put("QUERY_STRING", this.queryString);
        }
    }
}
