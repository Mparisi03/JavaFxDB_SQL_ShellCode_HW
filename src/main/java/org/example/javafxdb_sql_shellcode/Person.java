package org.example.javafxdb_sql_shellcode;

public class Person {


    private Integer id;
    private String Name;
    private String email;
    private String phone;
    private String address;
    private String password;


    public Person() {
    }


    public Person(Integer id, String Name, String email, String phone, String address, String password) {
        this.id = id;
        this.Name = Name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}




