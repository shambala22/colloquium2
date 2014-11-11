package ru.ifmo.md.colloquium2;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;


public class MainActivity extends Activity implements View.OnClickListener{
    Button add;
    Button begin;
    ListView listView;
    EditText field;
    boolean voteInProcess = false;
    ArrayList<Candidates> list;
    MyAdapter adapter;
    int sumvotes = 0;

    DatabaseHelper dh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = (Button) findViewById(R.id.button);
        add.setOnClickListener(this);
        begin = (Button) findViewById(R.id.button2);
        begin.setOnClickListener(this);
        field = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(android.R.id.list);

        list = new ArrayList<Candidates>();

        dh = new DatabaseHelper(this);
        SQLiteDatabase database = dh.getWritableDatabase();
        Cursor cursor = database.query("mytable", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int ind = cursor.getColumnIndex("surname");
            do {
                list.add(new Candidates(cursor.getString(ind)));
            } while (cursor.moveToNext());
        }
        adapter = new MyAdapter(this, list);
        listView.setAdapter(adapter);

    }



    @Override
    public void onClick(View view) {
        ContentValues contentValues = new ContentValues();
        String surname = field.getText().toString();
        SQLiteDatabase database = dh.getWritableDatabase();
        if (view.getId() == R.id.button) {
            if (!voteInProcess) {
                contentValues.put("surname", surname);
                database.insert("mytable", null, contentValues);
                list.add(new Candidates(surname));
                adapter = new MyAdapter(this, list);


            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Voting is already in process", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        if (view.getId() == R.id.button2) {
            if (!voteInProcess) {
                voteInProcess = true;
                begin.setText("Clear");

            }
            else {
                begin.setText("Begin");
                database.delete("mytable", null, null);
                list.clear();
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }
        }

    }
}

class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table mytable ("
                + "id integer primary key autoincrement,"
                + "surname text"+ ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}