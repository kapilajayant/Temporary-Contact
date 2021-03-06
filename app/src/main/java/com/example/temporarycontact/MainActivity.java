package com.example.temporarycontact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.temporarycontact.Adapter.ContactsAdapter;
import com.example.temporarycontact.Model.TempContact;
import com.example.temporarycontact.db.DBHelper;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import ru.alexbykov.nopermission.PermissionHelper;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    ContactsAdapter adapter;
    private String phoneNumber;
    private int numberOfContacts;
    RelativeLayout rl;
    List<TempContact> contactList = new ArrayList<TempContact>();
    String[] perms = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.FOREGROUND_SERVICE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent startIntent = new Intent(getApplicationContext(), CallService.class);
        startIntent.setAction("myService");
        startService(startIntent);

        rv = findViewById(R.id.rv);
        rl = findViewById(R.id.rl);

        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContactDialog();
            }
        });

//        PermissionHelper permissionHelper = new PermissionHelper(this); //don't use getActivity in fragment!
//
//        permissionHelper.check(Manifest.permission.ACCESS_COARSE_LOCATION)
//                .onSuccess(this::onSuccess)
//                .onDenied(this::onDenied)
//                .onNeverAskAgain(this::onNeverAskAgain)
//                .run();

        checkPermission(perms[0],1);
        checkPermission(perms[1],1);
        checkPermission(perms[2],1);
        checkPermission(perms[3],1);
        checkPermission(perms[4],1);

        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(layoutManager);
        contactList = new DBHelper(MainActivity.this).getContacts();
        adapter = new ContactsAdapter(contactList,MainActivity.this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rv);
        rv.setAdapter(adapter);
        rv.scrollToPosition(0);
        rv.setItemAnimator(new SlideInUpAnimator());
        adapter.notifyDataSetChanged();

    }

    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(MainActivity.this,permission)== PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[] { permission },requestCode);
        }
        else {
//            Toast.makeText(MainActivity.this,"Permission already granted",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this,"Permission Granted",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isDeleted;
    ItemTouchHelper.SimpleCallback simpleCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    isDeleted = new DBHelper(MainActivity.this).deleteContact(getApplicationContext(), contactList.get(viewHolder.getAdapterPosition()).getContactNumber());
                    if (isDeleted){
                        Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        deleteContact(MainActivity.this, contactList.get(viewHolder.getAdapterPosition()).getContactNumber(), contactList.get(viewHolder.getAdapterPosition()).getContactName());
//                        Snackbar.make(rv, "Contact Deleted", Snackbar.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
                    }
                    contactList.remove(viewHolder.getAdapterPosition());
                    adapter.notifyDataSetChanged();
                }
            };

    public void addContactDialog()  {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.add_contact, viewGroup, false);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText et = dialogView.findViewById(R.id.et_contactName);
        final EditText etNumber = dialogView.findViewById(R.id.et_contactNumber);
        etNumber.setHint("Enter Phone Number");
        et.requestFocus();
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        builder.setView(dialogView);
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(dialogView.getWindowToken(), 0);
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
                            addContact(et.getText().toString(),etNumber.getText().toString());
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "Contact Added", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void addContact(String contactName, String phoneNumber){

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
        new DBHelper(MainActivity.this).addContact(tempContact);
        startActivity(new Intent(MainActivity.this, MainActivity.class));
        finish();
    }

    public static boolean deleteContact(Context ctx, String phone, String name) {
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        Cursor cur = ctx.getContentResolver().query(contactUri, null, null, null, null);
        try {
            if (cur.moveToFirst()) {
                do {
                    if (cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(name)) {
                        String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                        ctx.getContentResolver().delete(uri, null, null);
                        return true;
                    }

                } while (cur.moveToNext());
            }

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        } finally {
            cur.close();
        }
        return false;
    }

}