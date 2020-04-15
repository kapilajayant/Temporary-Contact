package com.example.temporarycontact.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.temporarycontact.MainActivity;
import com.example.temporarycontact.Model.TempContact;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    String tableName = "tempContact";

    public DBHelper(Context context) {
        super(context , "TempContactDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table tempContact(id integer primary key autoincrement, contactName text, contactNumber text, contactTime text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists tempContact");
        onCreate(sqLiteDatabase);
    }

    public List<TempContact> getContacts(){
        List<TempContact> contactList = new ArrayList<TempContact>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM tempContact", null);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                TempContact tempContact = new TempContact();
                String contactName = cursor.getString(cursor.getColumnIndex("contactName"));
                String contactNumber = cursor.getString(cursor.getColumnIndex("contactNumber"));
                String contactTime = cursor.getString(cursor.getColumnIndex("contactTime"));
                tempContact.setContactName(contactName);
                tempContact.setContactNumber(contactNumber);
                tempContact.setContactTime(contactTime);
                contactList.add(tempContact);
            }
        }
        return contactList;
    }

    public boolean deleteContact(Context context, String phoneNumber){
        SQLiteDatabase database = this.getWritableDatabase();
//        Cursor cursor = database.rawQuery("DELETE FROM tempContact where contactNumber = "+phoneNumber, null);
        int deletedRows = database.delete("tempContact", "contactNumber = ?", new String[] {phoneNumber});
        Toast.makeText(context, String.valueOf(deletedRows), Toast.LENGTH_SHORT).show();
        if (deletedRows > 0)
            return true;
        else
            return false;
    }

    public void addContact(TempContact tempContact) {

        SQLiteDatabase database = this.getWritableDatabase();
        String contactName = tempContact.getContactName();
        String contactNumber = tempContact.getContactNumber();
        String contactTime = tempContact.getContactTime();
        ContentValues contentValues = new ContentValues();
        contentValues.put("contactName", contactName);
        contentValues.put("contactNumber", contactNumber);
        contentValues.put("contactTime", contactTime);
        database.insert("tempContact", null, contentValues);

    }

}
