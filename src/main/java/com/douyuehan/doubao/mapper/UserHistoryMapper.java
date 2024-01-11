package com.douyuehan.doubao.mapper;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHistoryMapper {
    String isHistoryContained(@Param("userName") String userName,@Param("postID")String postID);

    boolean insertHistory(@Param("userName") String userName,@Param("postID")String postID);
}
