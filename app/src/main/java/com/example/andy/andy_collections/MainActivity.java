package com.example.andy.andy_collections;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static android.R.attr.id;
import static android.R.attr.label;

public class MainActivity extends AppCompatActivity {
    DBActivity db;
    EditText name, location;
    Button submit_btn, view_all_btn, delete_btn, update_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBActivity(this);


        /* initializes text fields and buttons */
        name = (EditText) findViewById(R.id.name_field_text);
        location = (EditText) findViewById(R.id.location_field_text);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        view_all_btn = (Button) findViewById(R.id.view_all_btn);
        delete_btn = (Button) findViewById(R.id.delete_btn);
        update_btn = (Button) findViewById(R.id.update_btn);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDB();
            }
        });

        view_all_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAll();
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }

    public void addToDB() {

//        if (name.getText().toString().trim().isEmpty() || location.getText().toString().trim().isEmpty()) {
//            toast("Fields can't be empty");
//        } else {
        boolean isInserted = db.insertData(name.getText().toString(), location.getText().toString());
        if (isInserted) {
            toast("Data inserted");
            name.setText("");
            location.setText("");
        } else {
            toast("Data failed to insert");
        }
//        }
        System.out.println(name.getText().toString());
        System.out.println(location.getText().toString());
    }

    public void delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter ID");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    final int user_input = Integer.parseInt(input.getText().toString());
                    if (db.deleteById(user_input)) {
                        toast("Data deleted");
                    } else {
                        toast("Failed to delete");
                    }
                } catch (NumberFormatException e) {
                    toast("Enter a valid number");
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    public void update() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText id_input = new EditText(this);

        builder.setTitle("Enter ID");
        builder.setView(id_input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    final int user_input = Integer.parseInt(id_input.getText().toString());
                    Cursor res = db.getDataById(user_input);
                    if (res.getCount() > 0){
                        while(res.moveToNext()) {
                           showUpdateAlert(res.getString(1), res.getString(2), user_input);
                        }
                    }else {
                        toast("Nothing is found");
                        return;
                    }
                } catch (NumberFormatException e) {
                    toast("Enter a valid number");
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
        System.out.println("update button clicked");
    }

    public void showUpdateAlert(String name, String location, final int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText name_input = new EditText(this);
        final EditText location_input = new EditText(this);

        final TextView name_label = new TextView(this);
        final TextView location_label = new TextView(this);

        /* initializes the input fields */
        name_input.setText(name);
        location_input.setText(location);

        /* initializes the labels */
        name_label.setText("Name: ");
        name_label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        location_label.setText("Location: ");
        location_label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(name_label);
        ll.addView(name_input);
        ll.addView(location_label);
        ll.addView(location_input);

        builder.setView(ll);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (db.update(name_input.getText().toString(), location_input.getText().toString(), id)){
                    toast("Updated Successfully");
                }else {
                    toast("Failed to update");
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
           @Override
            public void onClick(DialogInterface dialogInterface, int i){
               dialogInterface.cancel();
           }
        });
        builder.show();
    }

    public void viewAll() {
        Cursor res = db.getAllData();
        if (res.getCount() == 0) {
            toast("There is nothing in database");
            return;
        } else {
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                System.out.println();
                buffer.append("ID: " + res.getString(0) + "\n");
                buffer.append("Name: " + res.getString(1) + "\n");
                buffer.append("Location: " + res.getString(2) + "\n");
                buffer.append("\n"); //skips at the end of every row
            }
            showMessage(buffer);
        }

        System.out.println("view all button clicked");
    }

    public void showMessage(StringBuffer buffer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Data");
        builder.setMessage(buffer);
        builder.show();
    }

    public void toast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
