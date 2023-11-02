package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activity.ChatActivity;
import com.example.myapplication.Modal.RoomBean;
import com.example.myapplication.R;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder>{
    List<RoomBean> mRoomList ;
    Context mContext;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView rname;
        TextView description;
        public ViewHolder(View view){
            super(view);
            rname = view.findViewById(R.id.rname);
            description = view.findViewById(R.id.description);
        }
    }
    public RoomAdapter(List<RoomBean> list,Context context){
        this.mRoomList = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.room_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoomBean roomBean = mRoomList.get(position);
        holder.rname.setText(roomBean.getRname());
        holder.description.setText(roomBean.getDescription());
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                //用户名还需要调整
                intent.putExtra("username", String.valueOf(Math.random()));
                intent.putExtra("rid", roomBean.getRid());
                mContext.startActivity(intent);
            }
        };
        holder.description.setOnClickListener(listener);
        holder.rname.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return mRoomList.size();
    }
}
