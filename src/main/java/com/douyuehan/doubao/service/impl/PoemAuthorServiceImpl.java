package com.douyuehan.doubao.service.impl;

import com.douyuehan.doubao.mapper.PoemAuthorMapper;
import com.douyuehan.doubao.model.entity.PoemAuthor;
import com.douyuehan.doubao.service.PoemAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PoemAuthorServiceImpl implements PoemAuthorService {
    @Autowired
    PoemAuthorMapper poemAuthorMapper;
    @Override
    public PoemAuthor getAuthor(String name, String desty) {
        return poemAuthorMapper.getAuthor(name, desty);
    }
}
