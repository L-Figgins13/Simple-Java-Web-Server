package webserver;

import java.util.*;
import java.io.*;
import java.lang.*;

/**
 * Created by euphoric on 2/7/17.
 */
abstract class ConfigurationReader{

        private File file;
        protected BufferedReader bufferedReader = null;

        private final int BUFFER_SIZE = 5000;

        ConfigurationReader(String fileName)
        {
            String path = fileName;
            file = new File(path);

            try {
                bufferedReader = new BufferedReader(new FileReader(file));
            } catch (Exception e) {
                System.out.println("Error buffered reader");
                System.out.println(e);
            }
        }

         boolean hasMoreLines() throws IOException
        {
            bufferedReader.mark(BUFFER_SIZE);

            if(bufferedReader.readLine() != null) {
                bufferedReader.reset();
                return true;
            } else {
                bufferedReader.reset();
                return false;
            }

        }

         String nextLine() throws IOException
        {
            if(hasMoreLines()){
                return bufferedReader.readLine();
            } else {
                return null;
            }
        }


        abstract void load() throws IOException;


        boolean skipLine(String line)
        {
            String firstCharacter;
            try {
                firstCharacter = line.substring(0, 1);
            } catch (Exception e){
                return true;
            }

            if (line.isEmpty()) {
                //System.out.println("Skipping due to Empty Line");
                return true;
            } else if (firstCharacter.equals("#")) {
                //System.out.println("Skipping Line due to first character: " + firstCharacter);
                return true;
            } else if (firstCharacter.equals("\\s")) {
                //System.out.println("Skipping Line due to first character: " + firstCharacter);
                return true;
            } else if (firstCharacter.equals("\\n")) {
                //System.out.println("Skipping Line due to first character: " + firstCharacter);
                return true;
            } else if (firstCharacter.equals("\\r")){
                //System.out.println("Skipping Line due to first character: " + firstCharacter);
                return true;
            } else {
                return false;
            }
        }
    }
