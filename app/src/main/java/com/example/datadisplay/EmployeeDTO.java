package com.example.datadisplay;

import android.os.Parcel;
import android.os.Parcelable;

public class EmployeeDTO implements Parcelable {

    String Name;
    String Designation;
    String city;
    String Eid;
    String DateOfJoining;
    String Salary;


    protected EmployeeDTO(Parcel in) {
        Name = in.readString();
        Designation = in.readString();
        city = in.readString();
        Eid = in.readString();
        DateOfJoining = in.readString();
        Salary = in.readString();
    }

    public EmployeeDTO() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(Designation);
        dest.writeString(city);
        dest.writeString(Eid);
        dest.writeString(DateOfJoining);
        dest.writeString(Salary);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<EmployeeDTO> CREATOR = new Parcelable.Creator<EmployeeDTO>() {
        @Override
        public EmployeeDTO createFromParcel(Parcel in) {
            return new EmployeeDTO(in);
        }

        @Override
        public EmployeeDTO[] newArray(int size) {
            return new EmployeeDTO[size];
        }
    };

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEid() {
        return Eid;
    }

    public void setEid(String eid) {
        Eid = eid;
    }

    public String getDateOfJoining() {
        return DateOfJoining;
    }

    public void setDateOfJoining(String dateOfJoining) {
        DateOfJoining = dateOfJoining;
    }

    public String getSalary() {
        return Salary;
    }

    public void setSalary(String salary) {
        Salary = salary;
    }


    @Override
    public String toString() {
        return Name;
    }
}
