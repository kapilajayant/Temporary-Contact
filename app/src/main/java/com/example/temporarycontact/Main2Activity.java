package com.example.temporarycontact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        addContactDialog();
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Message")
//                .setTitle("Title")
//                .setCancelable(true)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener()
//                {
//                    public void onClick(DialogInterface dialog, int id)
//                    {
//                        Main2Activity.this.finish();
//                    }
//                });
//
//        AlertDialog alert = builder.create();
//        alert.show();

    }
    public void addContactDialog()  {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.add_contact, viewGroup, false);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText et = dialogView.findViewById(R.id.et_contact);
        et.setHint(getIntent().getStringExtra("phoneNumber"));
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
                Main2Activity.this.finish();
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
                            Toast.makeText(getApplicationContext(), "Contact Added", Toast.LENGTH_SHORT).show();
                            Main2Activity.this.finish();
                        }
                        else {
                            et.setError("Type something");
                        }
                    }
                }
        );

        builder.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                        Main2Activity.this.finish();
                    }
                }
        );
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
