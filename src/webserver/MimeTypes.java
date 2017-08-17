
package webserver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by euphoric on 2/7/17.
 */

class MimeTypes extends ConfigurationReader{

    private Map<String, String> types;

    public MimeTypes(String fileName)
    {
        super(fileName);
        this.types = new HashMap<String, String>();
    }

     void load() throws IOException
    {
        String temp;

        while(hasMoreLines()){
            temp = nextLine();

            if(skipLine(temp)){
                continue;
            }

            String[] values = temp.split("\\s");

            if(values.length != 1 || values.length == 0){
                for(int index = 1; index < values.length; index++){
                    this.types.put(values[index] , values[0]);
                }
            }
        }
    }

     String lookup(String extension)
    {
        if(this.types.containsKey(extension)){
            return this.types.get(extension);
        } else {
            return null;
        }
    }

     void printConfig(){
        for(String name: this.types.keySet()){
            String key = name;
            String value = this.types.get(name);
            System.out.println(key + " " + value);
        }
    }
}
