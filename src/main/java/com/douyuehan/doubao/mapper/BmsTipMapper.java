package com.douyuehan.doubao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.douyuehan.doubao.model.entity.BmsTip;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BmsTipMapper extends BaseMapper<BmsTip> {
    BmsTip getRandomTip();

    List<String> getStars(String title, String author);
}
