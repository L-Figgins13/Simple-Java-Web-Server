package webserver;

import javax.print.DocFlavor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;

/**
 * Created by euphoric on 2/20/17.
 */

class ResponseFactory
{

    public static Response getBadRequestResponse(){
        return new BadRequestResponse();
    }

    public Response getResponse(Request request, Resource resource) throws IOException
    {
        Htaccess htaccess = checkForAccessFile(resource);

        if(htaccess != null){
           if(checkForAuthHeader(request)){
               if(!checkCredentials(htaccess, request)){
                   return new ForbiddenResponse(resource);
               }
           } else {
               return new UnauthorizedResponse(resource);
           }
        }


        if(!request.getHttpMethod().equals("PUT")){
            if(!fileExists(resource)){
                return new NotFoundResponse(resource);
            }
        }



        switch (request.getHttpMethod()) {
            case "GET":
                if(checkForCaching(request, resource)){
                    Response response = new OKResponse(resource);
                    response.prepare();
                    return new NotModifiedResponse(resource);
                }
                return new OKResponse(resource);

            case "HEAD":
                Response response = new OKResponse(resource);
                response.prepare();
                response.setOmitBody();
                return new OKResponse(resource);

            case "POST":
                if(resource.isScript()){
                    return new ScriptResponse(resource);
                }
            case "PUT":
                String path = resource.absolutePath();


                if(fileExists(resource)){ // if files exists overwrite file
                    File file = new File(path);
                    FileOutputStream stream = new FileOutputStream(file,false);
                    byte[] bodyBytes = request.getBodyBytes();

                    stream.write(bodyBytes);
                    stream.close();
                    return new OKResponse(resource);

                } else if (fileDoesNotExist(resource)){ // if file does not exist create it
                    File file = new File(path);
                    FileOutputStream stream = new FileOutputStream(file);

                    System.out.println("Attempting to create file at Location: " +path);
                    byte[] bodyBytes = request.getBodyBytes();
                    System.out.println("Size of Body in Bytes: " + bodyBytes.length);

                    stream.write(bodyBytes);
                    stream.flush();
                    stream.close();

                    return new CreatedResponse(resource);
                }

                break;

            case "DELETE":
                //NOTE: no need to check if the file exists because of previous check
                String target = resource.absolutePath();
                File file = new File(target);
                file.delete();
                return new OKResponse(resource);
        }






        return new ISEResponse(resource);
    }


    private boolean checkCredentials(Htaccess htaccess, Request request)
    {
        htaccess.load();
        LinkedList<String> values = request.getHeaderValues("Authorization");
        String headerField =  values.getFirst();
        String[] tokens = headerField.split("\\s");
        String credentials = tokens[1];

        System.out.println("BaseEncoded String in ResponseFactory.checkCredentials: " + credentials);

        return htaccess.isAuthorized(credentials);
    }

    private Htaccess checkForAccessFile(Resource resource)
    {
        System.out.println("Doc Root: " + resource.getConfig().getDocumentRoot());
        Path docroot = Paths.get(resource.getConfig().getDocumentRoot());
        Path htaccess = Paths.get(".htaccess");
        Path currentDirectory;
        String relativePath = resource.getRelativePath();

        String[] directories = relativePath.split("/");

        //currentDirectory = d

        //checks doc root for .htaccess
        currentDirectory = docroot;
        if(Files.exists(currentDirectory.resolve(htaccess))){
            return new Htaccess(currentDirectory.resolve(htaccess).toString());
        }

        for (String dir : directories){
            currentDirectory = currentDirectory.resolve(dir);
            Path htaccessPath = currentDirectory.resolve(htaccess);

            if(Files.exists(htaccessPath)){
                return new Htaccess(htaccessPath.toString());
            }
        }

        return null;
    }

    private boolean checkForAuthHeader(Request request)
    {
        if(request.headerExists("Authorization")){
            return true;
        }
        return false;
    }

    private boolean fileExists(Resource resource)
    {
        Path path = Paths.get(resource.absolutePath());

        return Files.exists(path);
    }

    private boolean fileDoesNotExist(Resource resource)
    {
        Path path = Paths.get(resource.absolutePath());

        return Files.notExists(path);
    }


    private boolean checkForCaching(Request request, Resource resource){

        try{
            resource.loadResource();
        } catch (Exception e) {
            System.out.println(e);
        }

        if(request.headerExists("If-Modified-Since"))
        { /*
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM HH:mm:ss z", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            */

            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM HH:mm:ss z", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));

            long unformatedDate = resource.getLastModified();
            LinkedList<String> values = request.getHeaderValues("If-Modified-Since");


            Date lastModified = new Date(unformatedDate);
            try {
                Date ifModifiedSince =  (Date) format.parse(values.getFirst());
                System.out.println("This is the date being compared: " + ifModifiedSince.toString());
                if(lastModified.compareTo(ifModifiedSince) > 0 ){
                    return true;
                }
            } catch (Exception e){
                System.out.println(e);
                return false;
            }

            format.setTimeZone(TimeZone.getTimeZone("GMT"));
        }

        return false;
    }
}
