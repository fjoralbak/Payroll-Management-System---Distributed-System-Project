package Utils;

import models.Staff;

import repositories.StaffRepo;

import java.util.ArrayList;

public class Authenticate {

    private static ArrayList<Notification> notifications=new ArrayList<Notification>();

    public static Staff login(String email, String password) throws Exception {
        Staff u= StaffRepo.find(email);
        if(u==null) {
            notifications.add(new Notification("Invalid Credentials",NotificaitonType.ERROR));
            return null;
        }

        if(u.getPassword().equals(Security.hashPassword(password,u.getSalt())))
            return u;

        notifications.add(new Notification("Invalid Credentials",NotificaitonType.ERROR));
        return null;
    }


    public static ArrayList<Notification> getNotifications() {

        return notifications;
    }

    public static void clearNotificaitons()
    {
        notifications.removeAll(notifications);
    }


    public static boolean register(Staff u) throws Exception {
        if(userExists(u.getEmail()))
        {
            notifications.add(new Notification("User exits", NotificaitonType.WARNING));
            return false;
        }
        else {
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
