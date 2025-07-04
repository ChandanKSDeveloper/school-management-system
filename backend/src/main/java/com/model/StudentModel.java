package com.model;

import com.utils.ValidationUtils;

import java.sql.Date;

public class StudentModel {
    private int id;
    private String first_name;
    private String last_name;
    private String father_name;
    private String email;
    private Date dob;
    private String mobile_no;
    private String gender;
    private String district;
    private String city;
    private String state;
    private String nationality;
    private String photo;
    private int classroom; // class 1 to 12
    private String photo_id;



    public StudentModel(
            int id,
            String first_name,
            String last_name,
            String father_name,
            String email,
            Date dob,
            String mobile_no,
            String gender,
            String district,
            String city,
            String state,
            String nationality,
            String photo,
            int classroom)
    {

        ValidationUtils.validateNonNull(first_name, "First name");
        ValidationUtils.validateNonNull(last_name, "Last name");
        ValidationUtils.validateNonNull(father_name, "Father's name");
        ValidationUtils.validateNonNull(email, "Email");
        ValidationUtils.validateNonNull(dob, "Date of birth");
        ValidationUtils.validateNonNull(mobile_no, "Mobile number");
        ValidationUtils.validateNonNull(gender, "Gender");
        ValidationUtils.validateNonNull(district, "District");
        ValidationUtils.validateNonNull(city, "City");
        ValidationUtils.validateNonNull(state, "State");
        ValidationUtils.validateNonNull(nationality, "Nationality");


        if (!ValidationUtils.isValidGender(gender)) {
            throw new IllegalArgumentException("Invalid gender. Must be Male, Female, or Other");
        }

        if (!ValidationUtils.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (!ValidationUtils.isValidMobile(mobile_no)) {
            throw new IllegalArgumentException("Invalid mobile number");
        }

        if (!ValidationUtils.isValidClass(classroom)) {
            throw new IllegalArgumentException("Class must be between 1 and 12");
        }

//        if (dob.after(Date.valueOf(LocalDate.now()))) {
//            throw new IllegalArgumentException("Date of birth cannot be in the future");
//        }

        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.father_name = father_name;
        this.email = email;
        this.dob = dob;
        this.mobile_no = mobile_no;
        this.gender = gender;
        this.district = district;
        this.city = city;
        this.state = state;
        this.nationality = nationality;
        this.photo = photo;
        this.classroom = classroom;
    }

    public StudentModel(
            String first_name,
            String last_name,
            String father_name,
            String email,
            Date dob,
            String mobile_no,
            String gender,
            String district,
            String city,
            String state,
            String nationality,
            String photo,
            int classroom,
            String photo_id)
    {

        ValidationUtils.validateNonNull(first_name, "First name");
        ValidationUtils.validateNonNull(last_name, "Last name");
        ValidationUtils.validateNonNull(father_name, "Father's name");
//        ValidationUtils.validateNonNull(email, "Email");
        ValidationUtils.validateNonNull(dob, "Date of birth");
        ValidationUtils.validateNonNull(mobile_no, "Mobile number");
        ValidationUtils.validateNonNull(gender, "Gender");
        ValidationUtils.validateNonNull(district, "District");
        ValidationUtils.validateNonNull(city, "City");
        ValidationUtils.validateNonNull(state, "State");
        ValidationUtils.validateNonNull(nationality, "Nationality");


        if (!ValidationUtils.isValidGender(gender)) {
            throw new IllegalArgumentException("Invalid gender. Must be Male, Female, or Other");
        }

        if (!ValidationUtils.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (!ValidationUtils.isValidMobile(mobile_no)) {
            throw new IllegalArgumentException("Invalid mobile number");
        }

        if (!ValidationUtils.isValidClass(classroom)) {
            throw new IllegalArgumentException("Class must be between 1 and 12");
        }

//        if (dob.after(Date.valueOf(LocalDate.now()))) {
//            throw new IllegalArgumentException("Date of birth cannot be in the future");
//        }

        this.first_name = first_name;
        this.last_name = last_name;
        this.father_name = father_name;
        this.email = email;
        this.dob = dob;
        this.mobile_no = mobile_no;
        this.gender = gender;
        this.district = district;
        this.city = city;
        this.state = state;
        this.nationality = nationality;
        this.photo = photo;
        this.classroom = classroom;
        this.photo_id = photo_id;
    }
    private boolean isValidGender(String gender) {
        return gender != null &&
                (gender.equalsIgnoreCase("Male") ||
                        gender.equalsIgnoreCase("Female") ||
                        gender.equalsIgnoreCase("Other"));
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        if (!isValidGender(gender)) {
            throw new IllegalArgumentException("Invalid gender. Must be Male, Female, or Other");
        }

        this.gender = gender;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getClassroom() {
        return classroom;
    }

    public void setClassroom(int classroom) {
        this.classroom = classroom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
