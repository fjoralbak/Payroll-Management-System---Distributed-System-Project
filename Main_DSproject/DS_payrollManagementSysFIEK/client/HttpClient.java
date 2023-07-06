package client;

import Utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import models.LoginInfo;
import models.Staff;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class  HttpClient {

    public static Staff sendLoginRequest(LoginInfo loginInfo) throws Exception {
        URL url = new URL("http://localhost:8081/login");
        String response = postPutRequest(url, loginInfo, "POST");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Staff s = gson.fromJson(response, Staff.class);
        return s;
    }

    public static List<Staff> getStaffRequest(Staff s) throws Exception {
        URL url = s.isAdmin() ? new URL(String.format(("http://localhost:8081/staff/?ssn=%d&staff=getAll"),s.getSSN())) : new URL("http://localhost:8081/staff/?ssn="+s.getSSN());
        String response = getDeleteRequest(url, "GET");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        ArrayList<Staff> userArray = new ArrayList<>();
        try {
            Type userListType = new TypeToken<ArrayList<Staff>>(){}.getType();
            userArray = gson.fromJson(response, userListType);
            }catch(Exception e){
            Staff staff = gson.fromJson(response, Staff.class);
            userArray.add(staff);
        }
        SessionManager.allStaff = userArray;
        return userArray;
    }

    public static String insertStaffRequest(Staff staff) throws Exception {
        URL url = new URL("http://localhost:8081/staff");
        return postPutRequest(url, staff, "POST");
    }

    public static String updateStaffRequest(Staff staff) throws Exception {
        URL url = new URL("http://localhost:8081/staff");
        return postPutRequest(url, staff, "PUT");
    }

    public static String deleteStaffRequest(Staff staff) throws Exception {
        URL url = new URL("http://localhost:8081/staff/?ssn="+staff.getSSN());
        return getDeleteRequest(url, "DELETE");
    }

    private static String postPutRequest(URL url, Object obj, String httpMethod) throws IOException {
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod(httpMethod);
        con.addRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String userJson = gson.toJson(obj);
        try( DataOutputStream wr = new DataOutputStream( con.getOutputStream())) {
            wr.write(userJson.getBytes());
        }

        if (con.getResponseCode() == 200 || con.getResponseCode() == 201 || con.getResponseCode() == 204){
            BufferedReader buffer = new BufferedReader(new InputStreamReader(con.getInputStream()));

            StringBuilder stB = new StringBuilder();
            String response = "";
            while ((response = buffer.readLine()) != null) {
                stB.append(response);
            }
            response = stB.toString();
            return response;
        } else {
            return null;
        }
    }

    private static String getDeleteRequest(URL url, String httpMethod) throws Exception {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(httpMethod);
        con.connect();

        int responseCode = con.getResponseCode();

        if (responseCode != 200)
        {
            return null;
        }
        else
        {
            InputStreamReader in = new InputStreamReader(con.getInputStream());
            BufferedReader buffer = new BufferedReader(in);
            StringBuilder stB = new StringBuilder();
            String response = "";

            while ((response = buffer.readLine()) != null) {
                stB.append(response);
            }

            response = stB.toString();
            return response;
        }
    }
}

