package com.promoanalytics.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextWatcher;


/**
 * Created by think360 on 22/03/17.
 */

public class RegisterUser extends BaseObservable {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String password;

    @Bindable
    public TextWatcher getNameWatcher() {
        return new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // notifyPropertyChanged(BR.firstName);
            }
        };
    }

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    @Bindable
    public String getError() {
        if (firstName == null || firstName.length() < 3) {
            return "Too short!";
        } else {
            return null;
        }
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        // notifyPropertyChanged(BR.firstName);
        // notifyPropertyChanged(BR.error);
    }

    @Bindable({"firstName"})
    public String getLastName() {

        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Bindable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
