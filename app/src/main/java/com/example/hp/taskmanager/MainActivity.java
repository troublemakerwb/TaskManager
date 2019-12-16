package com.example.hp.taskmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.show);
        registerForContextMenu(list);
        dbHelper=new MyDatabaseHelper(this,"binbin",null,1);
        ArrayList<Map<String, String>> items=getAll();
        setWordsListView(items);
    }

    private ArrayList<Map<String, String>> getAll() {
        ArrayList<Map<String, String>> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query("task", null, null, null, null, null, null);
        int colums = c.getColumnCount();
        while(c.moveToNext()){
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < colums; i++) {
                String task1 = c.getColumnName(i);
                String value1 = c.getString(c.getColumnIndex(task1));
                map.put(task1, value1);
            }
            list.add(map);
        }
        return list;
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        getMenuInflater().inflate(R.menu.action, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        TextView textId=null;
        TextView textName=null;
        TextView textDate1=null;
        TextView textDate2=null;
        TextView textMatter=null;
        TextView textOther=null;
        AdapterView.AdapterContextMenuInfo info=null;
        View itemView=null;
        switch (item.getItemId()){
            case R.id.action_delete:           //删除任务
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                textId =(TextView)itemView.findViewById(R.id.list_id);
                if(textId!=null){
                    String strId=textId.getText().toString();
                    System.out.println(strId);
                    DeleteDialog(strId);
                }
                break;
            case R.id.action_update:            //修改任务
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                textId =(TextView)itemView.findViewById(R.id.list_id);
                textName =(TextView)itemView.findViewById(R.id.list_name);
                textDate1 =(TextView)itemView.findViewById(R.id.list_date1);
                textDate2 =(TextView)itemView.findViewById(R.id.list_date2);
                textMatter=(TextView)itemView.findViewById(R.id.list_matter);
                textOther=(TextView)itemView.findViewById(R.id.list_other);
                if(textId!=null && textName!=null && textDate1!=null && textDate2!=null&&textMatter!=null&&textOther!=null){
                    String strId=textId.getText().toString();
                    String strName=textName.getText().toString();
                    String strDate1=textDate1.getText().toString();
                    String strDate2=textDate2.getText().toString();
                    String strMatter=textMatter.getText().toString();
                    String strOther=textOther.getText().toString();
                    UpdateDialog(strId, strName, strDate1,strDate2,strMatter,strOther);
                }
                break;
        }
        return true;
    }
    //修改对话框
    @SuppressLint("WrongViewCast")
    private void UpdateDialog(final String strId, final String strName, final String strDate1, final String strDate2, final String strMatter, final String strOther) {
        final LinearLayout tableLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.add, null);
        ((EditText)tableLayout.findViewById(R.id.name)).setText(strName);
        ((TextView)tableLayout.findViewById(R.id.date1)).setText(strDate1);
        ((TextView)tableLayout.findViewById(R.id.date2)).setText(strDate2);
        ((EditText)tableLayout.findViewById(R.id.matter)).setText(strMatter);
        ((EditText)tableLayout.findViewById(R.id.other)).setText(strOther);
        Button btn1 =tableLayout.findViewById(R.id.btnChoose1Date);
        Button btn2 = tableLayout.findViewById(R.id.btnChoose2Date);
        //     在按钮中实现获取时间
        btn1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new DatePickerDialog(MainActivity.this,new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        String s1 =(String.format("%d-%d-%d",year,monthOfYear+1,dayOfMonth));
                        System.out.println(s1);
                        //btnChoose1Date.setText(theDate1);
                        TextView text1=(TextView)tableLayout.findViewById(R.id.date1);
                        text1.setText("开始时间："+ s1);
                    }
                },2016,4,11).show();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new DatePickerDialog(MainActivity.this,new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        String s1 =(String.format("%d-%d-%d",year,monthOfYear+1,dayOfMonth));
                        System.out.println(s1);
                        //btnChoose1Date.setText(theDate1);
                        TextView text2=(TextView)tableLayout.findViewById(R.id.date2);
                        text2.setText("开始时间："+ s1);
                    }
                },2016,4,11).show();
            }
        });
        new AlertDialog.Builder(this).setTitle("修改任务")//标题
                .setView(tableLayout) //设置视图
                // 确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strNewName = ((EditText) tableLayout.findViewById(R.id.name)).getText().toString();
                        String strNewDate1 = ((TextView) tableLayout.findViewById(R.id.date1)).getText().toString();
                        String strNewDate2 = ((TextView) tableLayout.findViewById(R.id.date2)).getText().toString();
                        String strNewMatter = ((EditText) tableLayout.findViewById(R.id.matter)).getText().toString();
                        String strNewOther = ((EditText) tableLayout.findViewById(R.id.other)).getText().toString();
                        //既可以使用Sql语句更新，也可以使用使用update方法更新
                        UpdateUseSql(strId,strNewName,strNewDate1,strNewDate2,strNewMatter,strNewOther);
                        //  Update(strId, strNewWord, strNewMeaning, strNewSample);
                        setWordsListView(getAll());
                    }
                })
                //取消按钮及其动作
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create()//创建对话框
                .show();//显示对话框
    }

    //使用Sql语句更新任务
    private void UpdateUseSql(String strId, String strName, String strDate1, String strDate2, String strMatter, String strOther) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql="update task set name=?,date1=?,date2=?,matter=?,other=? where id=?";
        db.execSQL(sql, new String[]{strName, strDate1, strDate2,strMatter,strOther,strId});
    }

    //删除对话框
    private void DeleteDialog(final String strId){
        new AlertDialog.Builder(this).setTitle("删除任务").setMessage("是否真的删除该任务?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //既可以使用Sql语句删除，也可以使用使用delete方法删除
                DeleteUseSql(strId);
                //Delete(strId);
                setWordsListView(getAll());
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }
    //使用Sql语句删除任务
    private void DeleteUseSql(String strId) {
        String sql="delete from task where id='"+strId+"'";
        //Gets the data repository in write mode*/
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.execSQL(sql);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }
    private void setWordsListView(ArrayList<Map<String,String>> item){
        SimpleAdapter adapter = new SimpleAdapter(this,item,R.layout.list_item,
                new String[]{"id",Words.Word.COLUMN_NAME_NAME,Words.Word.COLUMN_NAME_DATE1,Words.Word.COLUMN_NAME_DATE2,Words.Word.COLUMN_NAME_MATTER,Words.Word.COLUMN_NAME_OTHER},
                new int[]{R.id.list_id,R.id.list_name,R.id.list_date1, R.id.list_date2,R.id.list_matter,R.id.list_other});
        list.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int id=menuItem.getItemId();
        switch (id){
            case R.id.search:
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater=getLayoutInflater();
                final LinearLayout linearLayout1=(LinearLayout)getLayoutInflater().inflate(R.layout.search,null);
                builder.setView(linearLayout1).setTitle("任务查找").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String txtSearchWord=((EditText)linearLayout1.findViewById(R.id.searchXML)).getText().toString();
                        ArrayList<Map<String, String>> items=null;
                        items=SearchUseSql(txtSearchWord);
                        // items=Search(txtSearchWord);
                        if(items.size()>0) {
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("result",items);
                            Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(MainActivity.this,"没有找到",Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
                return true;
            case R.id.add:
                AlertDialog.Builder builder1=new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater1=getLayoutInflater();
                final LinearLayout linearLayout=(LinearLayout)getLayoutInflater().inflate(R.layout.add,null);
                final Date[] date1 = new Date[1];
                Date date2;
                String theDate2;
                Button btn1 =linearLayout.findViewById(R.id.btnChoose1Date);
                Button btn2 = linearLayout.findViewById(R.id.btnChoose2Date);
                //     在按钮中实现获取时间
                btn1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        new DatePickerDialog(MainActivity.this,new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                            {
                                String s1 =(String.format("%d-%d-%d",year,monthOfYear+1,dayOfMonth));
                                System.out.println(s1);
                                //btnChoose1Date.setText(theDate1);
                                TextView text1=(TextView)linearLayout.findViewById(R.id.date1);
                                text1.setText("开始时间："+ s1);
                            }
                        },2016,4,11).show();
                    }
                });
                btn2.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        new DatePickerDialog(MainActivity.this,new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                            {
                                String s1 =(String.format("%d-%d-%d",year,monthOfYear+1,dayOfMonth));
                                System.out.println(s1);
                                //btnChoose1Date.setText(theDate1);
                                TextView text2=(TextView)linearLayout.findViewById(R.id.date2);
                                text2.setText("开始时间："+ s1);
                            }
                        },2016,4,11).show();
                    }
                });
                builder1.setView(linearLayout).setTitle("新建任务").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str1="000";
                        String str2="111";
                        String str3="222";
                        String str4="333";
                        String str5="444";
                        EditText et1=(EditText)linearLayout.findViewById(R.id.name);
                        if(et1!=null) {
                            str1 = et1.getText().toString();
                        }
                        TextView et2=(TextView) linearLayout.findViewById(R.id.date1);
                        if(et2!=null) {
                            str2 = et2.getText().toString();
                        }
                        TextView et3=(TextView) linearLayout.findViewById(R.id.date2);
                        if(et3!=null) {
                            str3 = et3.getText().toString();
                        }
                        EditText et4=(EditText)linearLayout.findViewById(R.id.matter);
                        if(et4!=null) {
                            str4 = et4.getText().toString();
                        }
                        EditText et5=(EditText)linearLayout.findViewById(R.id.other);
                        if(et5!=null) {
                            str5 = et5.getText().toString();
                        }
                        SQLiteDatabase db=dbHelper.getWritableDatabase();
                        ContentValues values=new ContentValues();
                        values.put("name","任务名"+str1);
                        values.put("date1",str2);
                        values.put("date2",str3);
                        values.put("matter","任务内容"+str4);
                        values.put("other","备注"+str5);
                        db.insert("task",null,values);
                        values.clear();
                        ArrayList<Map<String, String>> items=getAll();
                        setWordsListView(items);
                    }
                }).setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    

    private ArrayList<Map<String, String>> SearchUseSql(String txtSearchWord) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql="select * from task where name like ? order by name desc";
        Cursor c=db.rawQuery(sql,new String[]{"%"+txtSearchWord+"%"});
        return ConvertCursor2List(c);
    }

    private ArrayList<Map<String, String>> ConvertCursor2List(Cursor cursor) {
        ArrayList<Map<String,String>> arrayList=new ArrayList<Map<String,String>>();

        while(cursor.moveToNext()) {

            Map<String, String> map = new HashMap<String, String>();

            map.put("name", cursor.getString(1));

            map.put("date1", cursor.getString(2));

            map.put("date2", cursor.getString(3));

            map.put("matter",cursor.getString(4));

            map.put("other",cursor.getString(5));

            arrayList.add(map);

        }

        return arrayList;
    }
}
