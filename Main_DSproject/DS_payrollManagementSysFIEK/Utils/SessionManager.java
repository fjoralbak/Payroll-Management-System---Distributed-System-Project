package Utils;


import models.Staff;

import java.util.ArrayList;
import java.util.Date;

public class SessionManager {
    public static Staff user = null;
    public static Staff staffSelected = null;
    public static Date lastLogin = null;
    public static ArrayList<Staff> allStaff = null;
}
