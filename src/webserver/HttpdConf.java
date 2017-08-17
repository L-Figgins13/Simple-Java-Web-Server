package webserver;

import java.io.IOException;
import java.util.*;


/**
 * Created by euphoric on 2/7/17.
 */



class HttpdConf extends ConfigurationReader {

    private Map<String, String> aliases = null;
    private Map<String, String> scriptAliases = null;

    private String serverRoot = null;
    private String documentRoot = null;
    private String logFile = null;
    private String accessFileName = null;
    private String directoryIndex = null;

    private int listen = -1;

     HttpdConf(String fileName)
     {
        super(fileName);
        this.aliases = new HashMap<String, String>();
        this.scriptAliases = new HashMap<String, String>();
    }

    void load() throws IOException
    {
        String temp = null;

        while (hasMoreLines()) {
            temp = nextLine();
            if (skipLine(temp)) {
                continue;
            }
            String[] options = temp.split("\\s");
            //System.out.println("First element of Split Line: " + options[0]);

            switch (options[0]) {
                case "ServerRoot":
                    serverRoot = options[1].replaceAll("^\"|\"$" , "");
                    break;

                case "Listen":
                    listen = Integer.valueOf(options[1]);
                    break;

                case "DocumentRoot":
                    documentRoot = options[1].replaceAll("^\"|\"$" , "");
                    documentRoot = documentRoot.replaceAll("/$" , "");
                    break;

                case "LogFile":
                    logFile = options[1].replaceAll("^\"|\"$" , "");
                    break;

                case "Alias":
                    aliases.put(options[1], options[2]);
                    break;

                case "ScriptAlias":
                    scriptAliases.put(options[1], options[2]);
                    break;

                case "AccessFileName":
                    accessFileName = options[1];
                    break;

                case "DirectoryIndex":
                    directoryIndex = options[1];
                    break;
            }
        }

    }

    public void printConfig()
    {
        System.out.println("Server Root: " + serverRoot);
        System.out.println("Dog File: " + logFile);
        System.out.print("Aliases: ");

        for(String name: aliases.keySet()){
            String key = name;
            String value = aliases.get(name);
            System.out.println(key + " " + value);
        }

        System.out.println("Listing Script Aliases");

        for(String name: scriptAliases.keySet()){
            String key = name;
            String value = scriptAliases.get(name);
            System.out.println(key + " " + value);
        }

        System.out.println("Access File Name: " + accessFileName);
        System.out.println("Directory Index: " + directoryIndex);
    }

    public boolean checkAlias(String key)
    {
        return aliases.containsKey(key);
    }

    public boolean checkScriptAliases(String key)
    {
        return scriptAliases.containsKey(key);
    }

    public String getAliasedPath(String key)
    {
        return this.aliases.get(key);
    }

    public String getScriptAliasedPath(String key){
        return this.scriptAliases.get(key);
    }

    public String getServerRoot() {
        return serverRoot;
    }

    public String getDocumentRoot() {
        return documentRoot;
    }

    public String getLogFile() {
        return logFile;
    }

    public String getDirectoryIndex() {
        return directoryIndex;
    }

    public String getAccessFileName() {
        return accessFileName;
    }

    public int getListen() {
        return listen;
    }

    public static void main(String[] args){
      String workingDirectory = System.getProperty("user.dir");
      String[] temp = workingDirectory.split("/");
      String path = "/";
      for(int i = 1 ; i < temp.length-1; i++){
        path += temp[i] + "/";
      }

      System.out.println("Path after join: " + path);


      // String[] reducedPath = ArrayTools.reduce(String.class, temp, 2);
      // String path = reducePath.join("/");
      path = path + "config/httpd.conf";

      HttpdConf conf_test = new HttpdConf(path);

      try {
      conf_test.load();
    } catch (Exception e) {
      System.out.println(e);
    }

      conf_test.printConfig();
    }
}
