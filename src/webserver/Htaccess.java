package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * Created by euphoric on 2/9/17.
 */
class Htaccess extends ConfigurationReader
{
    Htpassword userFile;
    String authType;
    String authName;
    String require;

    public Htaccess(String fileName){
        super(fileName);
    }

    public void load()
    {
        String temp = null;

        try {
            while (hasMoreLines()) {
                temp = nextLine();
                System.out.println("Line: " + temp);

                if (skipLine(temp)) {
                    continue;
                }

                String[] kvPair = temp.split("\\s");

                switch (kvPair[0]) {
                    case "AuthUserFile":
                        this.userFile = new Htpassword(kvPair[1].replaceAll("^\"|\"$", ""));
                        break;
                    case "AuthType":
                        this.authType = kvPair[1];
                        break;
                    case "AuthName":
                        this.authName = kvPair[1];
                        break;
                    case "require":
                        this.require = kvPair[1];
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("bad ht access file");
        }
    }

    boolean isAuthorized(String authInfo)
    {

        String username;
        String password;
        String hashedPass;

        //System.out.println("AuthInfo: " + authInfo);
        String credentials = new String(Base64.getDecoder().decode(authInfo),
                Charset.forName("UTF-8"));

        String[] tokens = credentials.split(":");


        username = tokens[0];
        password = tokens[1];


        System.out.println("username: " + username);
        System.out.println("password: " + password);


        try {
            this.userFile.load();
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(password.getBytes());

            byte[] shaDigest = md.digest();

            StringBuffer buffer = new StringBuffer();

            for (byte bytes : shaDigest) {
                buffer.append(String.format("%02x", bytes & 0xff));
            }

            hashedPass = buffer.toString();
            System.out.println("hashed pass: " + hashedPass);

        } catch (Exception e){
            System.out.println(e);
            return false;
        }

        this.userFile.print_Users();

        return this.userFile.isAuthorized(username,hashedPass);

    }

    public static void main(String[] args)
    {
      //Htaccess test = new Htaccess();
    }


}
