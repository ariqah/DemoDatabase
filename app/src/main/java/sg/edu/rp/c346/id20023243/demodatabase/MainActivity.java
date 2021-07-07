package sg.edu.rp.c346.id20023243.demodatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnInsert, btnGetTasks;
    TextView tvResults;
    ListView lvTasks;
    ArrayAdapter<Task> aaTasks;
    ArrayList<Task> alTasks;
    EditText etDescription, etDate;
    boolean asc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResults = findViewById(R.id.tvResults);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);

        btnInsert = findViewById(R.id.btnInsert);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbh = new DBHelper(MainActivity.this); //create DBHelper object
                dbh.insertTask(etDescription.getText().toString(),
                        etDate.getText().toString()); //call insertTask() to insert task
            }
        });

        asc = true;

        btnGetTasks = findViewById(R.id.btnGetTasks);
        btnGetTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbh = new DBHelper(MainActivity.this);
                ArrayList<String> data = dbh.getTaskContent();
                dbh.close();

                String text = "";
                for (int i=0; i<data.size(); i++) {
                    Log.d("Database Content", i +". "+data.get(i));
                    text += i + ". "+ data.get(i) + "\n";
                }
                tvResults.setText(text);


                if(asc) {
                    asc = false;
                }
                else {
                    asc = true;
                }

                alTasks.clear(); //clear old one first so that does not repeat
                alTasks.addAll(dbh.getTasks(asc));
                aaTasks.notifyDataSetChanged();
            }
        });

        lvTasks = findViewById(R.id.lvTasks);
        alTasks = new ArrayList<>();
        aaTasks = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, alTasks);
        lvTasks.setAdapter(aaTasks);



    }
}