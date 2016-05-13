package rmuti.newbasketball;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private Cursor c;
    int id;
    private ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Database
        db = this.openOrCreateDatabase("mydatabase",MODE_PRIVATE,null);
        String sql = ""
                + " CREATE TABLE IF NOT EXISTS db("
                + "   id INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + "   location VARCHAR," + " date VARCHAR," + " time VARCHAR" + " )";
        db.execSQL(sql);
        bindData();
    }

    private void bindData(){
        String sql = "SELECT * FROM db";
        c = db.rawQuery(sql, null);

        int item = android.R.layout.simple_list_item_1;
        ArrayList data = new ArrayList();

        while(c.moveToNext()){
            int index = c.getColumnIndex("location");
            data.add(c.getString(index));
        }

        adapter = new ArrayAdapter(this, item, data);

        ListView myList = (ListView) findViewById(R.id.myList);
        myList.setAdapter(adapter);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int i, long l) {
                itemClick(i);
            }
        });
    }

    public void doNew(View v){
        setContentView(R.layout.form);
    }

    public void doSave(View v){
        EditText txtLocation = (EditText) findViewById(R.id.txtLocation);
        EditText txtDate = (EditText) findViewById(R.id.txtDate);
        EditText txtTime = (EditText) findViewById(R.id.txtTime);

        String location = txtLocation.getText().toString();
        String date = txtDate.getText().toString();
        String time = txtTime.getText().toString();

        if(location.equals("") || date.equals("") || time.equals("")){

        }else{
            String sql = "";
            if(id == 0){
                sql = "INSERT INTO db VALUES(null, ':location', ':date', ':time')";
                sql = sql.replace(":location",location);
                sql = sql.replace(":date",date);
                sql = sql.replace(":time",time);
                db.execSQL(sql);
            }else{
                ContentValues value = new ContentValues();
                value.put("location", location);
                value.put("date",date);
                value.put("time",time);
                db.update("db", value, "id = "+ id, null);
                id = 0;
            }
            setContentView(R.layout.activity_main);
            bindData();
        }
    }
    public void doHome(View v){
        setContentView(R.layout.activity_main);
        id = 0;
        bindData();
    }
    public void doDelete(View v){
        db.delete("db", "id = " + id, null);
        doHome(v);
    }
    public void doEdit(View v){

        id = c.getInt(c.getColumnIndex("id"));
        setContentView(R.layout.form);
        EditText txtLocation = (EditText) findViewById(R.id.txtLocation);
        EditText txtDate = (EditText) findViewById(R.id.txtDate);
        EditText txtTime = (EditText) findViewById(R.id.txtTime);

        txtLocation.setText(c.getString(c.getColumnIndex("location")));
        txtDate.setText(c.getString(c.getColumnIndex("date")));
        txtTime.setText(c.getString(c.getColumnIndex("time")));
    }

    public void itemClick(int index){
        c.moveToPosition(index);
        id = c.getInt(c.getColumnIndex("id"));
        setContentView(R.layout.show);

        TextView location = (TextView)findViewById(R.id.location);
        TextView date = (TextView)findViewById(R.id.date);
        TextView time = (TextView)findViewById(R.id.time);

        location.setText(c.getString(c.getColumnIndex("location")));
        date.setText(c.getString(c.getColumnIndex("date")));
        time.setText(c.getString(c.getColumnIndex("time")));

    }

}
