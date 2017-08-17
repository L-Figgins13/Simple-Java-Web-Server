package webserver;

import java.io.IOException;

/**
 * Created by euphoric on 2/7/17.
 */
class BadRequest extends IOException{

    public BadRequest(){}

    public BadRequest(String message)
    {
        super(message);
    }

}
