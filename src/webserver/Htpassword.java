package webserver;

import java.io.IOException;
import java.util.*;
/**
 * Created by euphoric on 2/9/17.
 */
class Htpassword extends ConfigurationReader
{
    private Map<String, String> users;
    private String encryptionType;

    public Htpassword(String fileName)
    {
        super(fileName);
        users = new HashMap<String, String>();
    }

    public void load() throws IOException
    {
        String line;
        String user;
        String pass_hash;

        while(hasMoreLines()){
            line = nextLine();
            String[] temp = line.split(":");

            user = temp[0].trim();
            pass_hash = temp[1].trim().replace("{SHA}" , "");

            users.put(user,pass_hash);
        }
    }

    public void print_Users(){
        for(String key : users.keySet()) {

            String password_hash  = users.get(key);
            System.out.print("User Name: " + key +" ");
            System.out.print("Hashed Password: " + password_hash + "\n");
        }
    }

    boolean isAuthorized(String username, String password)
    {
        if(username.equals("valid-user")){
            for(String key : this.users.keySet()){
                if(users.get(key).equals(password)){
                    return true;
                }
            }
        } else if(users.get(username).equals(password)){
            System.out.println("Access Granted");
            return true;
        }

        return false;
    }

    public Map<String, String> getUsers()
    {

        return users;
    }

    public String getEncryptionType()
    {

        return encryptionType;
    }
}