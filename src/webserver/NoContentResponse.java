package webserver;

import java.io.OutputStream;

/**
 * Created by euphoric on 2/17/17.
 */
class NoContentResponse extends Response
{
    public NoContentResponse(Resource resource) {
        super(resource);
    }

    public void prepare(){}

    public void prepareResponseHeaders(MimeTypes mime){}

}
