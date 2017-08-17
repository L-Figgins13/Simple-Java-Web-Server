package webserver;



import java.util.*;
import java.io.*;
import java.lang.*;
/**
 * Created by euphoric on 2/7/17.
 */

class Request{

    private DataInputStream reader;
    private InputStream stream;

    private String rawHttp;
    private String uri;
    private String body;
    private byte[] bodyBytes;


    //or verb, IF YOU WILL
    private String httpMethod;
    private String httpVersion;
    private String queryString;

    private Map<String, LinkedList<String>> headers;

    private boolean body_included = false;
    private int contentLength = 0;

    Request(String test)
    {
        rawHttp = test;
    }

    Request(InputStream client)
    {
        this.stream = client;
        this.reader = new DataInputStream(this.stream);
    }


    void parse() throws BadRequest
    {

        headers= new HashMap<String, LinkedList<String>>();

        try {
            String requestLine = reader.readLine();
            rawHttp = requestLine + "\r\n";
            String[] requestLineValues = requestLine.split("\\s");

            httpMethod = requestLineValues[0];
            uri = removeQueryString(requestLineValues[1]);
            httpVersion = requestLineValues[2];

            populateHeadersMap();

            bodyBytes = new byte[contentLength];
            char[] ctest = new char[contentLength];

            //get body of the message if content-length header exists

            if(body_included){
                //this.stream.reset();
                //his.stream.read(bodyBytes, 0, this.contentLength);

                reader.read(bodyBytes, 0, this.contentLength);

                this.body = new String(bodyBytes);
                this.rawHttp += "\r\n" + this.body;
            }






        } catch (Exception e) {
            //to be replaced with BadRequest exception
            System.out.println(e);
            System.out.println("400 Bad Exception");

            throw new BadRequest("400");

        } finally {
            //try {this.reader.close();} catch (Exception e) { System.out.println("stream in request class not closing");}
        }
    }


    public void printValues()
    {
        for(String key : this.headers.keySet()){
            List values = this.headers.get(key);
            String output = key +": ";
            System.out.print(output);
            values.forEach(name -> System.out.print(name + " "));
            System.out.println("");
        }
    }

    //private methods to make main code path more readable

    //WARNING SIDE EFFECT (intentional but still)
    private boolean checkContentLengthHeader(String header){
        if(header.equals("Content-Length")) {
            body_included = true;
            return true;
        } else {
            return false;
        }
    }

    private void populateHeadersMap() throws IOException
    {
        int test_counter = 1;

        boolean hasMoreHeaders = true;
        String header = "";
        LinkedList<String> fieldList;

        while(hasMoreHeaders){
            String line = reader.readLine();
                System.out.println("Body: " + this.body);

            this.rawHttp += line + "\r\n";

            fieldList = new LinkedList<>();

            if(line.isEmpty()){
                hasMoreHeaders = false;

            } else {
                String[] initialSplit = line.split(":" , 2);
                header = initialSplit[0];

                initialSplit[1].trim();

                String[] fields = initialSplit[1].split(";");

                if(checkContentLengthHeader(header)) {
                    String temp = fields[0].trim();
                    this.contentLength = Integer.parseInt(temp);

                }

                for(String field : fields){
                  String temp = field.trim();
                    System.out.print(temp);
                    fieldList.add(temp);
                }

                headers.put(header, fieldList);
                }
            }
            reader.mark(2000);
        }

    private String removeQueryString(String string)
    {
        if(string.contains("?")) {
            String[] tokens = string.split("\\?");
            this.queryString = tokens[1];
            return tokens[1];
        }

        return string;
    }








    //accessors

    public boolean headerExists(String key){
        return this.headers.containsKey(key);
    }

    public LinkedList<String> getHeaderValues(String key)
    {
        return this.headers.get(key);
    }


    public Map getHeaders()
    {
        return this.headers;
    }

    public byte[] getBodyBytes() {return this.bodyBytes;}

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getBody() {
        return this.body;
    }

    public String getUri() {
        return this.uri;
    }

    public String getHttpVersion() {
        return this.httpVersion;
    }

    public String getRawHttp() {
        return this.rawHttp;
    }

    public String getQueryString() {
        return queryString;
    }
}