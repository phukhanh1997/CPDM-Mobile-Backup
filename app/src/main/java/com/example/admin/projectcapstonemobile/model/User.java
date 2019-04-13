package com.example.admin.projectcapstonemobile.model;

import java.io.Serializable;

public class User implements Serializable {
    private Integer id;
    private String displayName;
    private String password;
    private String email;
    private String phone;
    private String address;
    private String birthday;
    private Department department;
    private Role role;

    public User(Integer id, String displayName, String password, String email, String phone, String address, String birthday, Role role) {
        this.id = id;
        this.displayName = displayName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.birthday = birthday;
        this.role = role;
    }

    public User() {
    }

    public User(Integer id, String displayName, String password, String email, String phone, String address, String birthday, Department department, Role role) {
        this.id = id;
        this.displayName = displayName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.birthday = birthday;
        this.department = department;
        this.role = role;
    }

    public User(Integer id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(String email, String password) {
        this.password = password;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
