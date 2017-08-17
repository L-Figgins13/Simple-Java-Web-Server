package webserver;

import java.util.LinkedList;

/**
 * Created by euphoric on 2/20/17.
 */
class UnauthorizedResponse extends Response
{
    UnauthorizedResponse(Resource resource)
    {
        super(resource);
        this.code = "401";
        this.reasonPhrase = "Unauthorized";
    }

    public void prepare(){
        prepareGeneralHeaders();
    }

    public void prepareResponseHeaders(MimeTypes mime){
        LinkedList<String> value = new LinkedList<>();
        value.add("Basic realm=\"myRealm\"");
        //value.add("realm=\"myrealm\"");
        this.headers.put("WWW-Authenticate", value);
    }
}
