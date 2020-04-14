package com.example.temporarycontact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.temporarycontact.Model.TempContact;
import com.example.temporarycontact.db.DBHelper;

public class Main2Activity extends AppCompatActivity {

    String phoneNumber;
    int numberOfContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Cursor cursor =  managedQuery(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        numberOfContacts = cursor.getCount();
        Toast.makeText(getApplicationContext(), String.valueOf(numberOfContacts), Toast.LENGTH_LONG).show();
        phoneNumber = getIntent().getExtras().getString("phoneNumber");
        if(!phoneNumber.isEmpty())
        {
            addContactDialog();
        }
    }
    public void addContactDialog()  {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.add_contact, viewGroup, false);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText et = dialogView.findViewById(R.id.et_contactName);
        final EditText etNumber = dialogView.findViewById(R.id.et_contactNumber);
        etNumber.setText(getIntent().getExtras().getString("phoneNumber"));
        et.requestFocus();
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        builder.setView(dialogView);
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(dialogView.getWindowToken(), 0);
                finishAffinity();
            }
        });
        builder.setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(dialogView.getWindowToken(), 0);
                        if (et.getText().toString().length()>0)
                        {
                            addContact(et.getText().toString());
                            Toast.makeText(getApplicationContext(), "Contact Added", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                }
        );

        builder.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        );
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void addContact(String contactName){

        TempContact tempContact = new TempContact();
        tempContact.setContactName(contactName);
        tempContact.setContactNumber(phoneNumber);
        tempContact.setContactTime("Tomorrow");
        ContentValues values = new ContentValues();
        values.put(Contacts.People.NUMBER, phoneNumber);
        values.put(Contacts.People.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM);
        values.put(Contacts.People.LABEL, "Temporary-Contact");
//        values.put(ContactsContract.CommonDataKinds.Organization.COMPANY, "Temporary-Contact");
        values.put(Contacts.People.NAME, contactName);
        Uri dataUri = getContentResolver().insert(Contacts.People.CONTENT_URI, values);
        Uri updateUri = Uri.withAppendedPath(dataUri, Contacts.People.Phones.CONTENT_DIRECTORY);
        values.clear();
        values.put(Contacts.People.Phones.TYPE, Contacts.People.TYPE_MOBILE);
        values.put(Contacts.People.NUMBER, phoneNumber);
        updateUri = getContentResolver().insert(updateUri, values);
        new DBHelper(Main2Activity.this).addContact(tempContact);
    }
}
