package com.douyuehan.doubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.douyuehan.doubao.model.entity.BmsTip;

import java.util.List;
public interface IBmsTipService extends IService<BmsTip> {
    BmsTip getRandomTip();

    List<String> getStars(String title, String author);
}
