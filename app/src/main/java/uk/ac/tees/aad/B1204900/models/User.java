package uk.ac.tees.aad.B1204900.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class User implements Parcelable {
    private String Id,
            Email,
            FirstName,
            LastName,
            PhoneNumber,
            Role,
            Department;


    public User(){

    }

    public User(String id, String email, String firstName, String lastName, String phoneNumber,
                String role, String department) {
        Id = id;
        Email = email;
        FirstName = firstName;
        LastName = lastName;
        PhoneNumber = phoneNumber;
        Role = role;
        Department = department;
    }

    public String getId() {
        return Id;
    }

    public String getEmail() {
        return Email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getRole() {
        return Role;
    }

    public String getDepartment(){return Department;}

    public void setId(String id) {
        Id = id;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public void setRole(String role) {
        Role = role;
    }

    public void setDepartment(String department){
        Department = department;
    }

    protected User(Parcel in) {
        Id = in.readString();
        Email = in.readString();
        FirstName = in.readString();
        LastName = in.readString();
        PhoneNumber = in.readString();
        Role = in.readString();
        Department = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(Id);
        parcel.writeString(Email);
        parcel.writeString(FirstName);
        parcel.writeString(LastName);
        parcel.writeString(PhoneNumber);
        parcel.writeString(Role);
        parcel.writeString(Department);
    }
}
