package com.example.notebook.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notebook.EditActivity;
import com.example.notebook.NoteDbOpenHelper;
import com.example.notebook.R;
import com.example.notebook.bean.Note;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    private List<Note> mBeanList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private NoteDbOpenHelper mNoteDbOpenHelper;
    private MyViewHolder holder;
    private int position;

    private int viewType;//声明绑定的类型grid / Linear

    public static int TYPE_LINEAR_LAYOUT = 0;
    public static int TYPE_GRID_LAYOUT = 1;


//
    public MyAdapter(Context context, List<Note> mBeanList) {
        this.mBeanList =mBeanList ;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mNoteDbOpenHelper = new NoteDbOpenHelper(mContext);
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    //给adapter进行传递

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }


    //    public MyAdapter(MainActivity mainActivity, List<Note> mNotes) {
////        this.mContext = context;
////        bsman = LayoutInflater.from(context);
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = mLayoutInflater.inflate(R.layout.list_item_layout, parent, false);
            // View view = mLayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {//
        this.holder = holder;
        this.position = position;
        Note note = mBeanList.get(position);
        holder.mTvTitle.setText(note.getTitle());
        holder.mTvContent.setText(note.getContent());
        holder.mTvtTime.setText(note.getCreatedTime());
        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 Intent intent = new Intent(mContext, EditActivity.class);
                //   Intent intent = new Intent(, EditActivity.class);
                intent.putExtra("note",note);
                mContext.startActivity(intent);
            }
        });

        holder.rlContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //长按弹出弹窗 删除或编辑
                Dialog dialog = new Dialog(mContext, android.R.style.ThemeOverlay_Material_Dialog_Alert);
                View view = mLayoutInflater.inflate(R.layout.list_item_dialog_layout,null);
                TextView tvDelete = view.findViewById(R.id.tv_delete);
                TextView tvEdit = view.findViewById(R.id.tv_edit);

                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int row = mNoteDbOpenHelper.deleteFromDbById(note.getId());
                        if (row > 0) {
                            removeData(position);

                            Toast.makeText(view.getContext(),"删除成功！",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(view.getContext(),"删除失败！",Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });

                tvEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(mContext, EditActivity.class);
                        //   Intent intent = new Intent(, EditActivity.class);
                        intent.putExtra("note",note);
                        mContext.startActivity(intent);
                        dialog.dismiss();
                    }
                });

                dialog.setContentView(view);
                dialog.show();

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        //return mBeanList.size();
        return mBeanList == null ? 0 : mBeanList.size();

    }


    public void refreshData(List<Note> notes){
        this.mBeanList = notes;
        notifyDataSetChanged();//通知数据集改变了，调用此方法时，adapter重新调用自己的方法们

    }

    public void removeData(int pos){
        mBeanList.remove(pos);
        notifyItemRemoved(pos);
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mTvTitle;
        TextView mTvContent;
        TextView mTvtTime;
        ViewGroup rlContainer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mTvTitle = itemView.findViewById(R.id.tv_title);
            this.mTvContent = itemView.findViewById(R.id.tv_content);
            this.mTvtTime = itemView.findViewById(R.id.tv_time);
            this.rlContainer = itemView.findViewById(R.id.rl_item_container);
        }
    }
}
