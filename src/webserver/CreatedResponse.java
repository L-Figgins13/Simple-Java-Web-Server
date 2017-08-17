package webserver;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;

/**
 * Created by euphoric on 2/24/17.
 */
class CreatedResponse extends Response
{
    CreatedResponse(Resource resource)
    {
        super(resource);
        this.code = "201";
        this.reasonPhrase = "Created";
    }

    public void prepare()
    {
        prepareGeneralHeaders();
    }

    public void prepareResponseHeaders(MimeTypes mimeTypes) throws IOException
    {
        LinkedList<String> val = new LinkedList<>();
        val.add(this.resource.absolutePath());
        this.headers.put("Location", val);
    }




}
