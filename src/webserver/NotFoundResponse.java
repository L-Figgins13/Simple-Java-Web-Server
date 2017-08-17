package webserver;

/**
 * Created by euphoric on 2/20/17.
 */
class NotFoundResponse extends Response {

    NotFoundResponse(Resource resource){
        super(resource);
        this.code = "404";
        this.reasonPhrase = "Not Found";
    }

    public void prepare(){
        prepareGeneralHeaders();
    }

    public void prepareResponseHeaders(MimeTypes mime){

    }


}
