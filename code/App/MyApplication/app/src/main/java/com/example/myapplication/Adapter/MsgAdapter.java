package com.example.myapplication.Adapter;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Modal.ClientMessageBean;
import com.example.myapplication.Modal.SystemMessageBean;
import com.example.myapplication.R;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private List<ClientMessageBean> mMsgList;
    private MediaPlayer mPlayer;
    static class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout leftLayout;
        LinearLayout rightLayout;
        LinearLayout centerLayout;
        LinearLayout voiceRightLayout;
        LinearLayout voiceLeftLayout;
        TextView leftMsg;
        TextView rightMsg;
        TextView centerMsg;
        ImageButton broadcastRight;
        ImageButton broadcastLeft;
        public ViewHolder(View view){
            super(view);
            leftLayout = view.findViewById(R.id.left_layout);
            rightLayout = view.findViewById(R.id.right_layout);
            centerLayout = view.findViewById(R.id.center_layout);
            leftMsg = view.findViewById(R.id.left_msg);
            rightMsg = view.findViewById(R.id.right_msg);
            centerMsg = view.findViewById(R.id.center_msg);
            voiceLeftLayout = view.findViewById(R.id.voice_order);
            voiceRightLayout = view.findViewById(R.id.voice_self);
            broadcastLeft = view.findViewById(R.id.record_order);
            broadcastRight = view.findViewById(R.id.record_self);
        }
    }

    public MsgAdapter(List<ClientMessageBean> msgList, MediaPlayer mPlayer){
        mMsgList = msgList;
        this.mPlayer = mPlayer;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_view,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        ClientMessageBean msg = mMsgList.get(position);
        if (msg.getType() == 2){
            //如果收到消息，则显示在左边，将右边,中间布局隐藏
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.centerLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.GONE);
            //语音隐藏
            holder.voiceRightLayout.setVisibility(View.GONE);
            holder.voiceLeftLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getMessage());
        }else if(msg.getType() == 1){
            //将左，中布局隐藏
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.centerLayout.setVisibility(View.GONE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.voiceRightLayout.setVisibility(View.GONE);
            holder.voiceLeftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getMessage());
        }else if(msg.getType() == 0){
            //隐藏左右布局
            holder.centerLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.voiceRightLayout.setVisibility(View.GONE);
            holder.voiceLeftLayout.setVisibility(View.GONE);
            holder.centerMsg.setText(msg.getMessage());
        } else if (msg.getType() == 3) {
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.centerLayout.setVisibility(View.GONE);
            holder.voiceRightLayout.setVisibility(View.VISIBLE);
            holder.voiceLeftLayout.setVisibility(View.GONE);
            try {
                holder.broadcastRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    mPlayer.reset();
                                    mPlayer.setDataSource(msg.getMessage());
                                    mPlayer.prepare();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                mPlayer.start();
                            }
                        }).start();
                    }
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (msg.getType() == 4) {
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.centerLayout.setVisibility(View.GONE);
            holder.voiceRightLayout.setVisibility(View.GONE);
            holder.voiceLeftLayout.setVisibility(View.VISIBLE);
            try {
                holder.broadcastRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    mPlayer.reset();
                                    mPlayer.setDataSource(msg.getMessage());
                                    mPlayer.prepare();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                mPlayer.start();
                            }
                        }).start();
                    }
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public int getItemCount(){
        return mMsgList.size();
    }

}
