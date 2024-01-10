package com.douyuehan.doubao.mapper;

import com.douyuehan.doubao.model.entity.PoemBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface PoemMapper {
    PoemBean getOnePoem();
    PoemBean getByTitleAndAuthorSimple(String title, String author);
    PoemBean getByTitleAndAuthorDetail(String id);
    PoemBean getByContentDetail(String content);

    List<String> getAllDynasty();
    List<String> getAllType();
    List<PoemBean> getBySearch(@Param("key") String key, @Param("page") Integer page, @Param("size") Integer size);
    List<PoemBean> getByTag(@Param("dynastys") String[] dynastys,@Param("tags") String[] tags,@Param("page") Integer page,@Param("size") Integer size);

}
