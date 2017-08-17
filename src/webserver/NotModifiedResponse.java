package webserver;

import java.io.OutputStream;

/**
 * Created by euphoric on 2/17/17.
 */
class NotModifiedResponse extends Response
{
    public NotModifiedResponse(Resource resource) {
        super(resource);
        this.code = "304";
        this.reasonPhrase = "NotModified";
   }

    public void prepare(){
        prepareGeneralHeaders();
        this.hasBody = false;

    }

    public void prepareResponseHeaders(MimeTypes mime)
    {

    }
}
