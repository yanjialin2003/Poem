package com.douyuehan.doubao.mapper;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHistoryMapper {
    String isHistoryContained(@Param("userID") String userID,@Param("postID")String postID);

    boolean insertHistory(@Param("userID") String userID,@Param("postID")String postID);
}
