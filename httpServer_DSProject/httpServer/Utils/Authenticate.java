package Utils;

import models.Staff;
import repositories.StaffRepo;

import java.util.ArrayList;

public class Authenticate {


    public static Staff login(String email, String password) throws Exception {
        Staff u= StaffRepo.find(email);
        if(u==null) {
            return null;
        }

        if(u.getPassword().equals(Security.hashPassword(password,u.getSalt())))
            return u;

        return null;
    }



    public static boolean register(Staff u) throws Exception {
        if(userExists(u.getEmail()))
        {
            return false;
        }
        else{
            String salt=Security.generateSalt();
            u.setSalt(salt);
            String hashedPass=Security.hashPassword(u.getPassword(),u.getSalt());
            u.setPassword(hashedPass);
            u.setAdmin(false);

            return StaffRepo.create(u);
        }
    }

    public static boolean userExists(String email) throws Exception {
        return  StaffRepo.find(email)!=null;
    }



}
