package Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Security {

    public static String generateSalt()
    {
        byte[] saltByte=new byte[6];
        SecureRandom sec=new SecureRandom();
        sec.nextBytes(saltByte);

        StringBuilder str=new StringBuilder();
        for (int i = 0; i < saltByte.length; i++) {
            str.append(Integer.toString((saltByte[i] & 0xff) + 0x100, 16).substring(1));
        }
        return str.toString();
    }

    public static String hashPassword(String passToHash,String salt)
    {
        passToHash+=salt;
        try
        {
            MessageDigest md=MessageDigest.getInstance("SHA-256");
            byte[] hashData=md.digest(passToHash.getBytes());

            StringBuilder str=new StringBuilder();
            for (int i = 0; i < hashData.length; i++) {
                str.append(Integer.toString((hashData[i] & 0xff) + 0x100, 16).substring(1));
            }

            return str.toString();
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
