package webserver;


import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by euphoric on 2/11/17.
 */
class Logger {
    private File logFile;

    Logger(String fileName)
    {
        this.logFile = new File(fileName);
    }

    synchronized void write(Request request , Response response)
    {

        try {
            if(!logFile.exists()){
                logFile.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(logFile,true);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            writeRequest(request,writer);
            writer.write("\r\n\r\n");
            writeResponse(response,writer);

            writer.write("---------------------------------------------------------------------------\r\n");
            writer.flush();

    } catch (Exception e) {
            System.out.println(e);
        }

    }

    private void writeRequest(Request request, BufferedWriter bWriter) throws IOException
    {
        bWriter.write("Request on: ");
        bWriter.write(new java.util.Date().toString() + "\r\n");
        bWriter.write(request.getRawHttp());
    }

    private void writeResponse(Response response, BufferedWriter bWriter)throws IOException
    {
        bWriter.write(new java.util.Date().toString() + "\r\n");
        String statusLine = "HTTP/1.1 " + response.getCode() + " " + response.getReasonPhrase() + "\r\n";

        bWriter.write(statusLine);

        Map<String, LinkedList<String>> headers = response.getHeaders();

        headers.forEach( (key , list) ->{
            String header = key + ":";
            String values = " ";

            int counter = 1;
            for (String element : list){
                values += " " + element;
                if(counter != list.size()){
                    values += ";";
                }
            }
            try {
                bWriter.write(header + values + "\r\n");
            } catch (Exception e) {
                System.out.println("I fucked up");
            }
        });

        bWriter.write("\r\n");


        if(response.hasResponseBody()){
            int bodyLength = response.getResource().getContentLength();
            byte[] data = new byte[bodyLength];
            data = response.getResource().getData();

            bWriter.write(new String(data));
        }


    }


}
