package webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by euphoric on 2/17/17.
 */
class OKResponse extends Response
{
    private boolean omitBody = false;

    public OKResponse(Resource resource) {
        super(resource);
        this.code = "200";
        this.reasonPhrase = "OK";
    }

    public void prepare()
    {
        prepareGeneralHeaders();
    }

    @Override
    public void prepareResponseHeaders(MimeTypes mimeTypes) throws IOException
    {
        String length;
        String mime;
        LinkedList<String> values = new LinkedList<>();

        try {
            this.resource.loadResource();


            //content type header
            mime = mimeTypes.lookup(resource.getFileExt());
            values.push(mime);
            //values.add("charset=utf-8");
            this.headers.put("Content-Type", values);


            // content length header
            LinkedList<String> val2 = new LinkedList<>();
            length = Integer.toString(resource.getContentLength());
            val2.push(length);
            this.headers.put("Content-Length", val2);

            LinkedList<String> lastModified = new LinkedList<>();
            long unformatedDate = resource.getLastModified();
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM HH:mm:ss z", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            lastModified.add(format.format(unformatedDate));
            this.headers.put("Last-Modified",lastModified);



        } catch (Exception e) {
            System.out.println("Response does not have a body");
        }
    }





}
