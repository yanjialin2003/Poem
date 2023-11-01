package com.douyuehan.doubao.service;

import com.douyuehan.doubao.model.entity.PoemBean;

import java.util.List;

public interface PoemService {
    PoemBean getOnePoem();

    PoemBean getByTitleAndAuthorSimple(String title, String author);
    PoemBean getByTitleAndAuthorDetail(String title, String author);

    PoemBean getByContentDetail(String content);

    List<String> getAllDynasty();
    List<String> getAllType();

    List<PoemBean> getBySearch(String key, Integer page, Integer size);
    List<PoemBean> getByTag(String dynasty,String tag, Integer page, Integer size);
}
