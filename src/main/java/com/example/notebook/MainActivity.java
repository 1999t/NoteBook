package com.example.notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.notebook.adapter.MyAdapter;
import com.example.notebook.bean.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private FloatingActionButton mBtnAdd;
    private List<Note> mNotes;
    private List<Note> mNote;
    private MyAdapter mMyAdapter;


    private NoteDbOpenHelper mNoteDbOpenHelper;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshDataFromDB();
    }

    private void refreshDataFromDB() {
        mNotes = getDataFromDB();
        //还要通知adapter
        mMyAdapter.refreshData(mNotes);
    }

    private void initEvent() {
        mMyAdapter =new MyAdapter(this,mNotes);
        mRecyclerView.setAdapter(mMyAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initData() {
        mNotes = new ArrayList<>();
        mNoteDbOpenHelper = new NoteDbOpenHelper(this);

//        for (int i = 0; i < 30; i++) {
//            Note note = new Note();
//            note.setTitle("这是标题"+ i);
//            note.setContent("这是内容"+ i);
//            note.setCreatedTime(getCurrentTimeFormat());
//            mNotes.add(note);
//        }

       // mNotes = getDataFromDB();

    }

    private List<Note> getDataFromDB() {
        return mNoteDbOpenHelper.queryAllFromDB();
    }

    private String getCurrentTimeFormat(){
        SimpleDateFormat simpleDateFormat= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            simpleDateFormat = new SimpleDateFormat("YYYY年MM月dd HH:mm:ss");
        }
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.rlv);
    }

    public void add(View view) {
        Intent intent = new Intent(this,AddActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {//zhiyongzhege
                mNotes = mNoteDbOpenHelper.queryFromDbByTitle(newText);
                mMyAdapter.refreshData(mNotes);


                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_linear:
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                mRecyclerView.setLayoutManager(linearLayoutManager);
                return true;
            case R.id.menu_grid:
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
                mRecyclerView.setLayoutManager(gridLayoutManager);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}