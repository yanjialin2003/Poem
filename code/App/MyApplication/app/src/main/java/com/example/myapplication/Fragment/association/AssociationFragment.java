package com.example.myapplication.Fragment.association;

import static com.xuexiang.xui.XUI.getContext;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.RoomAdapter;
import com.example.myapplication.Modal.RoomBean;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentAssociationBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AssociationFragment extends Fragment {

    private FragmentAssociationBinding binding;

    RecyclerView recyclerView;
    List<RoomBean> roomList = new ArrayList<>();
    RoomAdapter adapter;

    AlertDialog.Builder createRoomBuilder ;
    View createView;
    ImageButton createRoomButton;
    ImageButton searchButton;
    AlertDialog dialog;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if(message.what == 1){
                System.out.println("room fresh");
                //更新聊天室列表
                adapter.notifyDataSetChanged();
            }
            return false;
        }
    });

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAssociationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initView();
        //设置适配器
        adapter = new RoomAdapter(roomList,getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        getRoomList(0,100);

        //添加监听
        createRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRoomBuilder = new AlertDialog.Builder(getContext());
                createView = View.inflate(getContext(),R.layout.create_room,null);
                Button button = createView.findViewById(R.id.button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText newRname = createView.findViewById(R.id.rname);
                        EditText newDescription = createView.findViewById(R.id.description);
                        String rname = String.valueOf(newRname.getText());
                        String description = String.valueOf(newDescription.getText());
                        createRoom(rname,description);
                        dialog.dismiss();
                    }
                });
                createRoomBuilder.setView(createView);
                dialog = createRoomBuilder.create();
                dialog.show();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = binding.key;
                String key = String.valueOf(input.getText());
                //发请求查找
                OkHttpClient client = new OkHttpClient();
                HttpUrl url = new HttpUrl.Builder()
                        .scheme("http")
                        .host("101.33.242.218")
                        .port(8081)
                        .addPathSegment("chatController")
                        .addPathSegment("getBySearch")
                        .addQueryParameter("key",key)
                        .addQueryParameter("page","0")
                        .addQueryParameter("size","100")
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("search failure");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        System.out.println("onResponse");
                        String res = response.body().string();
                        Gson gson = new Gson();
                        List<RoomBean> list = gson.fromJson(res, new TypeToken<ArrayList<RoomBean>>() {
                        }.getType());
                        roomList.clear();
                        roomList.addAll(list);
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                });
            }
        });

        return root;
    }

    void initView(){
        recyclerView = binding.roomList;
        createRoomButton = binding.createRoomButton;
        searchButton = binding.searchButton;
    }

    void getRoomList(Integer page, Integer size){
        OkHttpClient client = new OkHttpClient();
        //这一部分还可调整页数刷新
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("101.33.242.218")
//                .host("192.168.18.163")
                .port(8081)
                .addPathSegment("chatController")
                .addPathSegment("getAllRoom")
                .addQueryParameter("page", String.valueOf(page))
                .addQueryParameter("size", String.valueOf(size))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("http","getAllRoom Failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("onResponse");
                String res = response.body().string();
                Gson gson = new Gson();
                List<RoomBean> list = gson.fromJson(res, new TypeToken<ArrayList<RoomBean>>() {
                }.getType());
                roomList.clear();
                roomList.addAll(list);
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        });
    }

    void createRoom(String rname, String description){
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("101.33.242.218")
                .port(8081)
                .addPathSegment("chatController")
                .addPathSegment("createRoom")
                .addQueryParameter("rname",rname)
                .addQueryParameter("description",description)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("create failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //刷新聊天室列表
                //这里可以让服务器返回单个房间优化一下
                getRoomList(0,100);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}