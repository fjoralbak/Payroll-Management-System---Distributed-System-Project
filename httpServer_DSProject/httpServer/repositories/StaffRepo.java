package repositories;

import Utils.DateHelper;
import Utils.DbHelper;
import models.Staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StaffRepo {
    public static int count() throws Exception {
        Connection conn = DbHelper.getConnection();
        ResultSet res = conn.createStatement().executeQuery("SELECT COUNT(*) FROM staff");
        res.next();
        return res.getInt(1);
    }



    public static Staff find(int ssn) throws Exception {
        Connection conn = DbHelper.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM staff where ssn = ? ");
        stmt.setInt(1, ssn);

        ResultSet res = stmt.executeQuery();
        if (!res.next()) return null;

        return getUserFromRes(res);
    }

    public static boolean create(Staff user) throws Exception {

        Connection conn = DbHelper.getConnection();

        PreparedStatement stmt = conn.prepareStatement("INSERT into staff() VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, user.getSSN());
        stmt.setString(2, user.getName());
        stmt.setString(3, user.getSurname());
        stmt.setString(4, user.getEmail());
        stmt.setString(5, user.getDepartment());
        stmt.setInt(6, user.getExperience());
        stmt.setTimestamp(7, java.sql.Timestamp.valueOf(DateHelper.toSqlFormat(user.getBirthday())));
        stmt.setString(8, user.getPhoneNumber());
        stmt.setString(9, user.getPosition());
        stmt.setInt(10, user.getSalary());
        stmt.setString(11, user.getAddress());
        stmt.setString(12, user.getGender());
        stmt.setTimestamp(13, java.sql.Timestamp.valueOf(DateHelper.toSqlFormat(user.getDateHired())));
        stmt.setString(14, user.getBankAccountNr());
        stmt.setString(15, user.getPassword());
        stmt.setString(16, user.getSalt());
        stmt.setBoolean(17, user.isAdmin());

        if (stmt.executeUpdate() != 1)
            throw new Exception("ERR_NO_ROW_CHANGE");

        return true;
    }

    public static Staff update(Staff model) throws Exception {
        Connection conn = DbHelper.getConnection();
        String query = "UPDATE staff SET emri = ?, mbiemri = ?, email = ?, department = ?, experience = ?, birthday = ?, " +
                "phoneNumber = ?, position = ?, salary = ?, address = ?, gender = ?, dateHired = ?, bankAccountNr = ?, " +
                "password = ?, salt = ?, admin = ? WHERE ssn = ?";
        PreparedStatement stmt = conn.prepareStatement(query);

        stmt.setString(1, model.getName());
        stmt.setString(2, model.getSurname());
        stmt.setString(3, model.getEmail());
        stmt.setString(4, model.getDepartment());
        stmt.setInt(5, model.getExperience());
        stmt.setTimestamp(6, java.sql.Timestamp.valueOf(DateHelper.toSqlFormat(model.getBirthday())));
        stmt.setString(7, model.getPhoneNumber());
        stmt.setString(8, model.getPosition());
        stmt.setInt(9, model.getSalary());
        stmt.setString(10, model.getAddress());
        stmt.setString(11, model.getGender());
        stmt.setTimestamp(12, java.sql.Timestamp.valueOf(DateHelper.toSqlFormat(model.getDateHired())));
        stmt.setString(13, model.getBankAccountNr());
        stmt.setString(14, model.getPassword());
        stmt.setString(15, model.getSalt());
        stmt.setBoolean(16, model.isAdmin());
        stmt.setInt(17, model.getSSN());

        if (stmt.executeUpdate() != 1)
            throw new Exception("ERR_NO_ROW_CHANGE");

        return find(model.getSSN());
    }

    public static boolean delete(int ssn) throws Exception{
        Connection conn = DbHelper.getConnection();
        PreparedStatement stmt=conn.prepareStatement("DELETE FROM staff WHERE ssn =? ");
        stmt.setInt(1, ssn);

        return stmt.executeUpdate() == 1;
    }

    public static Staff find(String email) throws Exception {
        Connection conn = DbHelper.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM staff where email =? ");
        stmt.setString(1, email);

        ResultSet res = stmt.executeQuery();
        if (!res.next()) return null;

        return getUserFromRes(res);
    }

    private static Staff getUserFromRes(ResultSet res) throws Exception {
        int SSN = res.getInt("ssn");
        String name = res.getString("emri");
        String surname = res.getString("mbiemri");// me shtu n'DB
        String email = res.getString("email");
        String department = res.getString("department");
        int experience = res.getInt("experience");
        Date birthday = res.getDate("birthday");
        String phoneNumber = res.getString("phoneNumber");
        String position = res.getString("position");
        int salary = res.getInt("salary");
        String address = res.getString("address");
        String gender = res.getString("gender");
        Date dateHired = res.getDate("dateHired");
        String bankAccountNr = res.getString("bankAccountNr");
        String password = res.getString("password");
        String salt = res.getString("salt");
        boolean isAdmin = res.getBoolean("admin");

        return new Staff(SSN, name, surname, email, department, experience, birthday, phoneNumber, position, salary, address, gender, dateHired, bankAccountNr, password, salt, isAdmin);
    }

    private static String getDepartmentsFromRes(ResultSet res) throws Exception {
        String departmentName = res.getString("departmentName");

        return departmentName;
    }

    private static Integer getDepartmentIdFromRes(ResultSet res) throws Exception {
        Integer departmentId = res.getInt("departmentId");
        return departmentId;
    }

    public static List<Staff> getAll() throws Exception {
        Connection conn = DbHelper.getConnection();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM staff");
        ArrayList<Staff> users = new ArrayList<>();
        while (rs.next()) {
            users.add(getUserFromRes(rs));
        }

        return users;
    }

    public static List<Staff> getSome(int pageSize, int page) throws Exception {
        ArrayList<Staff> list = new ArrayList<>();

        Connection conn = DbHelper.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM staff ORDER BY ssn ASC LIMIT ? OFFSET ?");
        stmt.setInt(1, pageSize);
        stmt.setInt(2, pageSize * page);
        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            list.add(getUserFromRes(res));
        }
        return list;
    }
}