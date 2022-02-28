package com.example.notebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.example.notebook.bean.Note;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class NoteDbOpenHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "noteSQLite.db";
    private static final String TABLE_NAME_NOTE = "note";
   // private static final String CREATE_TABLE_SQL = "create table" + TABLE_NAME_NOTE + " (id integer primary key autoincrement, title text, content text, create_time text)";
    private static final String CREATE_TABLE_SQL =  "create table note ("
                                                      + "id integer primary key autoincrement, "
                                                      + "title text, "
                                                      + "content text, "
                                                      + "create_time text)";

    private static final String INSERT_TABLE_SQL = " INSERT INTO note(create,title,content) VALUES (?,?,?) ";

    public NoteDbOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public long insertData(Note note){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title",note.getTitle());
        values.put("content",note.getContent());
        values.put("create_time",note.getCreatedTime());

        return db.insert(TABLE_NAME_NOTE,null,values);
        // return db.insert(INSERT_TABLE_SQL);
    }

    public List<Note> queryAllFromDB(){
        SQLiteDatabase db = getWritableDatabase();
        List<Note> noteList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME_NOTE,null,null,null,null,null,null);

        if (cursor!= null){

            while(cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String createTime = cursor.getString(cursor.getColumnIndex("create_time"));

                Note note = new Note();
                note.setId(id);
                note.setTitle(title);
                note.setContent(content);
                note.setCreatedTime(createTime);

                noteList.add(note);
            }

            cursor.close();
        }
        return noteList;

    }

    public int updateData(Note note) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title",note.getTitle());
        values.put("content",note.getContent());
        values.put("create_time",note.getCreatedTime());

        //"id = ?"  "id is ?"
        return db.update(TABLE_NAME_NOTE, values,"id like ?", new String[]{note.getId()});//id是那个就更新那个，用id来唯一标识记事


    }

    public int deleteFromDbById(String id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME_NOTE,"id like ?", new String[]{id});
    }

    public List<Note> queryFromDbByTitle(String title){

        if (TextUtils.isEmpty(title)){
            return queryAllFromDB();
        }

        SQLiteDatabase db = getWritableDatabase();
        List<Note> noteList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME_NOTE,null,"title like ?",new String[]{"%"+title+"%"},null,null,null);

        if (cursor!= null){

            while(cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String title2 = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String createTime = cursor.getString(cursor.getColumnIndex("create_time"));

                Note note = new Note();
                note.setId(id);
                note.setTitle(title2);
                note.setContent(content);
                note.setCreatedTime(createTime);

                noteList.add(note);
            }

            cursor.close();
        }
        return noteList;

    }

}
