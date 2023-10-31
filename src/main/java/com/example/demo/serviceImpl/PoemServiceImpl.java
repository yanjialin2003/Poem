package com.example.demo.serviceImpl;

import com.example.demo.bean.PoemBean;
import com.example.demo.mapper.PoemMapper;
import com.example.demo.service.PoemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class PoemServiceImpl implements PoemService{
    @Autowired
    PoemMapper poemMapper;

    @Override
    public PoemBean getOnePoem(){
       PoemBean poemBean = poemMapper.getOnePoem();
       return poemBean;
    }

    @Override
    public PoemBean getByTitleAndAuthorSimple(String title, String author){
        PoemBean poemBean = poemMapper.getByTitleAndAuthorSimple(title, author);
        return poemBean;
    }
    @Override
    public PoemBean getByTitleAndAuthorDetail(String title, String author){
        PoemBean poemBean = poemMapper.getByTitleAndAuthorDetail(title, author);
        return poemBean;
    }
    @Override
    public PoemBean getByContentDetail(String content){
        //转化为句子之间使用 % 号连接
        String format="%";
        String tempTexts[] = content.split("、|，|。|；|？|！");
        System.out.println("content:"+content);
        for(int i=0;i<tempTexts.length;i++) {
            System.out.println("分割："+tempTexts[i]);
            format += tempTexts[i] + "%";
        }
        PoemBean poemBean = poemMapper.getByContentDetail(format);
        return poemBean;
    }

    @Override
    public List<String> getAllDynasty() {
        List<String> list = poemMapper.getAllDynasty();
        return list;
    }

    @Override
    public List<String> getAllType() {
        List<String> list = poemMapper.getAllType();
        HashSet<String> res = new HashSet<>();
        //处理每一个tag，按照，分开
        //再去重处理
        for(String item:list){
            if(item == null||item.equals("")){
                continue;
            }
            String tempTexts[] = item.split(",");
            for (String i:tempTexts){
                res.add(i);
            }
        }
        list = new ArrayList<>();
        for (String item:res){
            list.add(item);
        }
        return list;
    }

    @Override
    public List<PoemBean> getBySearch(String key, Integer page, Integer size) {
        key = "%" + key + "%";
        page = page*size;
        List<PoemBean> list = poemMapper.getBySearch(key,page,size);
        return list;
    }

    @Override
    public List<PoemBean> getByTag(String dynasty, String tag, Integer page, Integer size) {
        String[] tags=tag.split(",");
        page = page*size;
        for(int i = 0;i < tags.length; i++){
            tags[i] = "%" + tags[i] + "%";
        }
        String[] dynastys=dynasty.split(",");
        for(int i = 0;i < dynastys.length; i++){
            dynastys[i] = "%" + dynastys[i] + "%";
        }
        List<PoemBean> list = poemMapper.getByTag(dynastys, tags,page,size);
        return list;
    }


}
