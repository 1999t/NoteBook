package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notebook.bean.Note;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {
    private Note note;
    private EditText etTitle,etContent;
    private NoteDbOpenHelper mNoteDbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);

         mNoteDbOpenHelper = new NoteDbOpenHelper(this);

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        note = (Note) intent.getSerializableExtra("note");
        if (note != null){
            etTitle.setText(note.getTitle());
            etContent.setText(note.getContent());
        }
        mNoteDbOpenHelper = new NoteDbOpenHelper(this);
    }


    public void save(View view) {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        if (TextUtils.isEmpty(title)){
            Toast.makeText(view.getContext(),"标题不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setCreatedTime(getCurrentTimeFormat());
        long row = mNoteDbOpenHelper.updateData(note);
        if(row != -1){///
            Toast.makeText(view.getContext(),"修改成功！",Toast.LENGTH_SHORT).show();
            this.finish();
        }else{
            Toast.makeText(view.getContext(),"修改失败！",Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentTimeFormat(){
        SimpleDateFormat simpleDateFormat= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            simpleDateFormat = new SimpleDateFormat("YYYY年MM月dd HH:mm:ss");
        }
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

}
