package webserver;

/**
 * Created by euphoric on 2/21/17.
 */
class BadRequestResponse extends Response {

    BadRequestResponse()
    {
        this.code = "400";
        this.reasonPhrase = "Bad Request";
    }

    public void prepare()
    {
        prepareGeneralHeaders();
    }

    public void prepareResponseHeaders(MimeTypes mime) {}


}
