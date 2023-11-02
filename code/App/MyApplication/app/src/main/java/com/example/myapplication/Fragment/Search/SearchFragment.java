package com.example.myapplication.Fragment.Search;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.Activity.PoetryListActivity;
import com.example.myapplication.Modal.PoetryUtil;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentAssociationBinding;
import com.example.myapplication.databinding.FragmentSearchBinding;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    private static final String TAG = "test";

    private Button sureButton;

    private final int COMPLETED = 1;
    private final int RESPONSE_SUCCESS = 2;
    private final int RESPONSE_FAIL = 0;
    private final int TO_WEBVIEW = 3;
    private JSONObject poetryObject;
    private JSONArray collectObject;

    private TagFlowLayout typeTagLayout;
    private TagFlowLayout dynastyTagLayout;
    private TagFlowLayout styleTagLayout;

    private String[] typeList = null;
    private String[] dynastyList = null;
    private String[] styleList = null;

    private List<String> selectedType = new ArrayList<String>();
    private List<String> selectedStyle = new ArrayList<String>();
    private List<String> selectedDynasty = new ArrayList<String>();

    public SearchFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        initView();//初始化视图

        //将标签数据传递给下一页面
        sureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getContext(), PoetryListActivity.class);
                String tag = "";
                String dynasty = "";
                String toolBarTitle = "";
                if (selectedDynasty.size() > 0)
                    toolBarTitle += selectedDynasty.get(0) + ".";
                if (selectedType.size() > 0)
                    toolBarTitle += selectedType.get(0) + ".";
                if (selectedStyle.size() > 0)
                    toolBarTitle += selectedStyle.get(0);
                if (selectedType.size() == 0 && selectedStyle.size() == 0)
                    toolBarTitle = selectedDynasty.get(0);

                for (String temp : selectedType)
                    tag += temp + ",";
                for (String temp1 : selectedStyle)
                    tag += temp1 + ",";
                for (String temp2 : selectedDynasty)
                    dynasty += temp2 + ",";

                intent.putExtra("from", "filter");
                intent.putExtra("tag", tag);
                intent.putExtra("dynasty", dynasty);
                intent.putExtra("toolBarTitle", toolBarTitle);
                startActivity(intent);
            }
        });

        return root;
    }


    private void initView() {
        sureButton = binding.sureButton;

        initTagFlowLayout();
    }

    //    分类检索的标签初始化
    private void initTagFlowLayout() {
        typeTagLayout = binding.typeFloat;
        dynastyTagLayout = binding.dynastyFloat;
        styleTagLayout = binding.styleFloat;

        typeList = PoetryUtil.getType();//获取诗词类型标签
        dynastyList = PoetryUtil.getDynasty();//获取诗词朝代标签
        styleList = PoetryUtil.getStyle();//获取诗词风格标签

        final LayoutInflater mInflater = getLayoutInflater();

//        适配器
        typeTagLayout.setAdapter(new TagAdapter<String>(typeList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {

                TextView tv = (TextView) mInflater.inflate(R.layout.adapter_item_tag,
                        typeTagLayout, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                selectedType.add(typeList[position]);
                showSelected(selectedType);
                TextView textView = (TextView) view;
                textView.setTextColor(Color.WHITE);
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                TextView textView = (TextView) view;
                selectedType.remove(textView.getText());
                showSelected(selectedType);
                textView.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        dynastyTagLayout.setAdapter(new TagAdapter<String>(dynastyList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {

                TextView tv = (TextView) mInflater.inflate(R.layout.adapter_item_tag,
                        dynastyTagLayout, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                selectedDynasty.add(dynastyList[position]);
                TextView textView = (TextView) view;
                textView.setTextColor(Color.WHITE);
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                TextView textView = (TextView) view;
                selectedDynasty.remove(textView.getText());
                textView.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        styleTagLayout.setAdapter(new TagAdapter<String>(styleList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {

                TextView tv = (TextView) mInflater.inflate(R.layout.adapter_item_tag,
                        styleTagLayout, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                selectedStyle.add(styleList[position]);
                TextView textView = (TextView) view;
                textView.setTextColor(Color.WHITE);
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                TextView textView = (TextView) view;
                selectedStyle.remove(textView.getText());
                textView.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
    }

    private void showSelected(List<String> temp) {
        if (temp.size() > 0) {
            for (String tempString : temp)
                System.out.println("selectedTag:" + tempString);
        } else
            System.out.println("没有选择");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}