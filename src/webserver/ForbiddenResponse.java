package webserver;

import java.io.OutputStream;

/**
 * Created by euphoric on 2/17/17.
 */
class ForbiddenResponse extends Response
{
    public ForbiddenResponse(Resource resource) {
        super(resource);
        this.code = "403";
        this.reasonPhrase = "Forbidden";
    }

    public void prepare(){
        prepareGeneralHeaders();
    }

    public void prepareResponseHeaders(MimeTypes mime){
        System.out.println("no headeres");
    }
}
