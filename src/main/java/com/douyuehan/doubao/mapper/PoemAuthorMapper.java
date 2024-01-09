package com.douyuehan.doubao.mapper;

import com.douyuehan.doubao.model.entity.PoemAuthor;
import org.springframework.stereotype.Repository;

@Repository
public interface PoemAuthorMapper {
    PoemAuthor getAuthor(String name, String desty);
}
