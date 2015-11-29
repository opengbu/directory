package in.darbose.gbu.directory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class AddActivity extends Activity implements OnClickListener {
    private Button btn_save;
    private Button btn_delete;
    private EditText edit_first, edit_last;
    private DBHelper mHelper;
    private SQLiteDatabase dataBase;
    private String id, fname, lname;
    private boolean isUpdate;
    private AlertDialog.Builder build;

    private ArrayList<String> userId = new ArrayList<String>();
    private ArrayList<String> user_fName = new ArrayList<String>();
    private ArrayList<String> user_lName = new ArrayList<String>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        btn_save = (Button) findViewById(R.id.save_btn);
        btn_delete = (Button) findViewById(R.id.delete_btn);
        edit_first = (EditText) findViewById(R.id.frst_editTxt);
        edit_last = (EditText) findViewById(R.id.last_editTxt);

        isUpdate = getIntent().getExtras().getBoolean("update");
        if (isUpdate) {
            id = getIntent().getExtras().getString("ID");
            fname = getIntent().getExtras().getString("Fname");
            lname = getIntent().getExtras().getString("Lname");
            edit_first.setText(fname);
            edit_last.setText(lname);

        }

        btn_save.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        mHelper = new DBHelper(this);


    }


    // saveButton click event
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.save_btn):
                fname = edit_first.getText().toString().trim();
                lname = edit_last.getText().toString().trim();
                if (fname.length() > 0 && lname.length() > 0) {
                    saveData();
                } else {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AddActivity.this);
                    alertBuilder.setTitle("Invalid Data");
                    alertBuilder.setMessage("Please, Enter valid data");
                    alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });
                    alertBuilder.create().show();
                }
            case R.id.delete_btn:
                deleteData();
        }



    }


    private boolean deleteData() {
        dataBase = mHelper.getWritableDatabase();
        build = new AlertDialog.Builder(AddActivity.this);
        build.setTitle("Delete " + fname);
        build.setMessage("Do you want to delete ?");
        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getApplicationContext(),
                        fname + " is deleted.", Toast.LENGTH_SHORT).show();

                dataBase.delete(
                        DBHelper.TABLE_NAME,
                        DBHelper.KEY_ID + "="
                                + id, null);

                dataBase.close();
                finish();

                dialog.cancel();
            }
        });

        build.setNegativeButton("No", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = build.create();
        alert.show();

        return true;
    }
    /**
     * save data into SQLite
     */
    private void saveData() {
        dataBase = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBHelper.KEY_FNAME, fname);
        values.put(DBHelper.KEY_LNAME, lname);

        System.out.println("");
        if (isUpdate) {
            //update database with new data 
            dataBase.update(DBHelper.TABLE_NAME, values, DBHelper.KEY_ID + "=" + id, null);
        } else {
            //insert data into database
            dataBase.insert(DBHelper.TABLE_NAME, null, values);
        }
        //close database
        dataBase.close();
        finish();


    }

}