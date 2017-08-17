package webserver;

import webserver.HttpdConf;

import java.io.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;


/**
 * Created by euphoric on 2/17/17.
 */
class Resource
{
    private String uri;
    private String modifiedPath;
    private String relativePath;
    private String fileExt;

    private Date lastModified;

    private HttpdConf config;
    private File file;
    private boolean isScript = false;
    private byte[] data;


    Resource(String uri, HttpdConf config)
    {
        this.uri = uri;
        this.config = config;
    }

    public void loadResource() throws IOException {
        this.file = new File(absolutePath());
        Path path = Paths.get(this.modifiedPath);
        this.data = Files.readAllBytes(path);
    }

    public String absolutePath()
    {
        //String modifiedPath;

        if(this.config.checkAlias(this.uri)){
            this.modifiedPath = this.config.getDocumentRoot() + this.config.getAliasedPath(this.uri);
            this.relativePath = this.config.getAliasedPath(this.uri);
        } else if (this.config.checkScriptAliases(this.uri)){
            this.isScript = true;
            this.modifiedPath = this.config.getDocumentRoot() + this.config.getScriptAliasedPath(this.uri);
            this.relativePath = this.config.getScriptAliasedPath(this.uri);
        } else {
            this.modifiedPath = this.config.getDocumentRoot() + this.uri;
            this.relativePath = this.uri;
        }

        if(isFile(modifiedPath)){
            return modifiedPath;
        } else {
            this.modifiedPath += this.config.getDirectoryIndex();
            return modifiedPath;
        }


    }


    public boolean isScript()
    {
        return this.isScript;
    }

    boolean isProtected()
    {
        Path path = Paths.get(this.modifiedPath);

        return false;
    }


    private boolean isFile(String path)
    {
        String[] dirStructure = path.split("/");

        return dirStructure[dirStructure.length - 1].contains(".");
    }



    //accessors

    public String getFileExt()
    {
        String abspath = absolutePath();
        int i = abspath.lastIndexOf('.');
        System.out.println("File Extension: " + abspath.substring(i+1));
        this.fileExt =abspath.substring(i + 1);
        return this.fileExt;
    }

    public long getLastModified()
    {
        return this.file.lastModified();
    }


    public String getUri()
    {
        return uri;
    }


    public String getModifiedPath()
    {
        return modifiedPath;
    }


    public HttpdConf getConfig()
    {
        return config;
    }


    public String getRelativePath()
    {
        String temp = absolutePath();
        return this.relativePath;
    }

    public byte[] getData()
    {
        return data;
    }

    public int getContentLength()
    {
        return data.length;
    }
}
