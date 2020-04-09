package com.example.temporarycontact.Model;

public class TempContact
{
    String ContactName, ContactNumber;

    public TempContact(String contactName, String contactNumber) {
        ContactName = contactName;
        ContactNumber = contactNumber;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }
}
