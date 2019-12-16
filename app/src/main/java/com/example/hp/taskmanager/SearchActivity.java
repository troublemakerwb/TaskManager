package com.example.hp.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private ListView listView;
   // private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listView=(ListView)findViewById(R.id.search_words);
        Intent intent=getIntent();
        ArrayList<Map<String, String>> arrayList=(ArrayList<Map<String, String>>)intent.getSerializableExtra("result");
        setWordsListView(arrayList);
    }

    private void setWordsListView(ArrayList<Map<String, String>> item){
        SimpleAdapter adapter = new SimpleAdapter(this,item,R.layout.list_item,
                new String[]{"id",Words.Word.COLUMN_NAME_NAME,Words.Word.COLUMN_NAME_DATE1,Words.Word.COLUMN_NAME_DATE2,Words.Word.COLUMN_NAME_MATTER,Words.Word.COLUMN_NAME_OTHER},
                new int[]{R.id.list_id,R.id.list_name,R.id.list_date1, R.id.list_date2,R.id.list_matter,R.id.list_other});
        listView.setAdapter(adapter);
    }


}
