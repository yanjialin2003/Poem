package com.douyuehan.doubao.controller;

import com.douyuehan.doubao.common.api.ApiResult;
import com.douyuehan.doubao.model.entity.PoemAuthor;
import com.douyuehan.doubao.model.entity.PoemBean;
import com.douyuehan.doubao.service.IBmsTipService;
import com.douyuehan.doubao.service.PoemAuthorService;
import com.douyuehan.doubao.service.PoemService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/poem")
public class PoemController {
    @Autowired
    PoemService poemService;
    @Autowired
    IBmsTipService iBmsTipService;

    @Autowired
    PoemAuthorService poemAuthorService;

    //随机获取一首诗
    @RequestMapping("/getOnePoem")
    public String  getOnePoem() {
//        PoemBean poemBean = poemService.getOnePoem();
        try {
            // 设置请求的URL
            String url = "http://10.68.160.45:5000/Spark/recommendPeom";
            // 创建URL对象
            URL obj = new URL(url);
            // 打开连接
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // 设置请求方式为GET
            con.setRequestMethod("GET");
            // 读取响应内容
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Gson().toJson(ApiResult.failed());
    }

    //通过诗名和作者找到对应诗，返回JavaBean类型的Json字符串
    @RequestMapping(value = "/getByTitleAndAuthorSimple", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult<PoemBean> getByTitleAndAuthorSimple(String id) {
        PoemBean poemBean = poemService.getByTitleAndAuthorSimple(id);
        return ApiResult.success(poemBean);
    }

    @RequestMapping(value = "/getByTitleAndAuthorDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult<PoemBean> getByTitleAndAuthorDetail(String id) {
        PoemBean poemBean = poemService.getByTitleAndAuthorDetail(id);
        // 选出名句
        List<String> allStars = iBmsTipService.getStars(poemBean.getTitle(), poemBean.getAuthor());
        List<String> stars = new ArrayList<>();
        for (String star : allStars){
            if(poemBean.getContent().contains(star)){
                stars.add(star);
            }
        }
        poemBean.setStars(stars);
        // 查找诗人
        PoemAuthor poemAuthor = poemAuthorService.getAuthor(poemBean.getAuthor(), poemBean.getDesty());
        poemBean.setAuthorDetail(poemAuthor);
        return ApiResult.success(poemBean);
    }

    /**
     * 通过输入的内容，进行模糊查询（此处又疑问，可能查到多个匹配诗句，根据前端输入要求可以再调整）
     */
    @RequestMapping(value = "/getByContentDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult<PoemBean> getByContentDetail(String content) {
        PoemBean poemBean = poemService.getByContentDetail(content);
        return ApiResult.success(poemBean);
    }

    //获取所有朝代
    @RequestMapping(value = "/getAllDynasty", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult<List<String>> getAllDynasty() {
        List<String> list = poemService.getAllDynasty();
        return ApiResult.success(list);
    }

    //获取所有类型

    /**
     * 这个地方获取所有的 tag 然后分开成单独的词
     */
    @RequestMapping(value = "/getAllType", method = RequestMethod.GET)
    public ApiResult<List<String>> getAllType() {
        List<String> list = poemService.getAllType();
        return ApiResult.success(list);
    }

    @RequestMapping(value = "/getBySearch", method = RequestMethod.GET)
    public ApiResult<List<PoemBean>> getBySearch(String key, Integer page, Integer size) {
        List<PoemBean> list = poemService.getBySearch(key, page, size);
        return ApiResult.success(list);
    }

    //通过选择的标签来获取诗词

    /**
     * 这里的逻辑有待考虑
     * 1.必须符合所有的筛选
     * 2.只要再筛选的标签中就给出
     * 这里先实现第二种
     * 而且再朝代和类型之间使用的是AND
     * 语义是在这些朝代的这些诗词
     */
    @RequestMapping(value = "/getByTag", method = RequestMethod.GET)
    public ApiResult<List<PoemBean>> getByTag(String dynasty, String tag, Integer page, Integer size) {
        List<PoemBean> list = poemService.getByTag(dynasty, tag, page, size);
        return ApiResult.success(list);
    }

}
