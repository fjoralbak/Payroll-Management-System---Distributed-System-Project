package models;

import java.util.Date;

public class Staff {
    private int SSN;
    private String name;
    private String surname;// me shtu n'DB
    private String email;
    private String department;
    private int experience;
    private Date birthday;
    private String phoneNumber;
    private String position;
    private int salary;
    private String address;
    private String gender;
    private Date dateHired;
    private String bankAccountNr;
    private String password;
    private String salt;
    private boolean isAdmin;

    public Staff(int SSN, String name, String surname, String email, String department, int experience, Date birthday, String phoneNumber, String position, int salary, String address, String gender, Date dateHired, String bankAccountNr, String password, String salt, boolean isAdmin) {
        this.SSN = SSN;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.department = department;
        this.experience = experience;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.position = position;
        this.salary = salary;
        this.address = address;
        this.gender = gender;
        this.dateHired = dateHired;
        this.bankAccountNr = bankAccountNr;
        this.password = password;
        this.salt = salt;
        this.isAdmin = isAdmin;
    }

    public Staff(){
        this(-1,"","","","",-1, null, "","",-1,"","",null,"","","", false);
    }

    public int getSSN() {
        return SSN;
    }

    public void setSSN(int SSN) {
        this.SSN = SSN;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateHired() {
        return dateHired;
    }

    public void setDateHired(Date dateHired) {
        this.dateHired = dateHired;
    }

    public String getBankAccountNr() {
        return bankAccountNr;
    }

    public void setBankAccountNr(String bankAccountNr) {
        this.bankAccountNr = bankAccountNr;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}