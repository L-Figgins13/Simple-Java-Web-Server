package webserver;



import javax.print.DocFlavor;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by euphoric on 2/11/17.
 */
abstract class Response
{
    protected String code;
    protected  boolean hasBody = false;
    protected int contentLength;
    protected String reasonPhrase;
    protected boolean omitBody = false;
    protected Resource resource;


    protected Map<String, LinkedList<String>> headers;

    Response(){}

    Response(Resource resource)
    {
        this.headers = new HashMap<>();
        this.resource = resource;
    }

    public abstract void prepare();


    public abstract  void prepareResponseHeaders(MimeTypes mimeTypes) throws IOException;


    protected void prepareGeneralHeaders() //TODO make this better did not realize hash map was a reference...
    {
        LinkedList<String> values = new LinkedList<>();
        values.push(getDate());

        this.headers.put("Date",values);

        LinkedList<String> values2 = new LinkedList<>();
        values2.push("Logan's Server");
        this.headers.put("Server" , values2);
    }


    public void send(OutputStream out) throws IOException
    {
        String statusLine;
        DataOutputStream writer = new DataOutputStream(out);


        statusLine = "HTTP/1.1 " + this.code + " " + this.reasonPhrase + "\r\n";
        int statusLineByteLength = statusLine.getBytes("UTF-8").length;
        byte[] statusLineBytes= statusLine.getBytes("UTF-8");


        writer.write(statusLineBytes, 0, statusLineByteLength);

        headers.forEach( (key , list) ->{
            String header = key + ":";
            String values = " ";

            if(key.equals("Content-Length")){
                this.contentLength = Integer.parseInt(list.get(0));
                this.hasBody = true;
            }

            int counter = 1;
            for (String element : list){
                values += " " + element;
                if(counter != list.size()){
                    values += ";";
                }
            }
            try {
                System.out.print(header + values + "\r\n");
                String headerLine = header + values + "\r\n";
                int byteLength = headerLine.getBytes("UTF-8").length;
                byte[] headerBytes = new byte[byteLength];
                headerBytes = headerLine.getBytes("UTF-8");
                writer.write(headerBytes, 0 , byteLength);
            } catch (Exception e) {
                System.out.println("I fucked up");
            }
        });

        String blankline = "\r\n";
        int size = blankline.getBytes("UTF-8").length;
        writer.write(blankline.getBytes("UTF-8"), 0 ,size);

        if(hasBody && !omitBody){
            sendbody(writer);
        }




    }

    protected String getDate()
    {
        //I DOUBT I NEED TO CITE THIS>>> BUT JUST in CASE I GOOGLED getting date in HTTP format
        // and found something similar on stack overflow ..... http://stackoverflow.com/questions/7707555/getting-date
        // -in-http-format-in-java. Just making sure you don't give us a zero for literally no reason

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format.format(calendar.getTime());
    }

    public void resolveContentType(MimeTypes mimeTypes)
    {
        String mime;

        LinkedList<String> value = new LinkedList<>();

        mime  = mimeTypes.lookup(resource.getFileExt());
        value.push(mime);


        this.headers.put("Content-Type",value);
    }

    private void sendbody(DataOutputStream writer) throws IOException
    {
        byte[] body = new byte[contentLength];
        body = this.resource.getData();

        writer.write(body, 0, this.contentLength);
        writer.flush();
    }

    public Resource getResource () {return this.resource;}

    public String getCode() {
        return code;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setOmitBody ()
    {
        this.omitBody = true;
    }

    public Map<String, LinkedList<String>> getHeaders() {
        return this.headers;
    }

    public boolean hasResponseBody() {
        return hasBody;
    }
}
