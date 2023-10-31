package com.example.demo.controller;

import com.example.demo.bean.PoemBean;
import com.example.demo.service.PoemService;
import com.example.demo.utils.FileUtils;
import com.example.demo.utils.JavaExecPython;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/poetry")
public class PoemController {
    @Autowired
    PoemService poemService;

    Gson gson = new Gson();

    //随机获取一首诗
    @RequestMapping("/getOnePoem")
    public String getOnePoem() {
        PoemBean poemBean = poemService.getOnePoem();
        if (poemBean == null) {
            return "null";
        }
        String poemJson = gson.toJson(poemBean);
        return poemJson;
    }

    //通过诗名和作者找到对应诗，返回JavaBean类型的Json字符串
    @RequestMapping(value = "/getByTitleAndAuthorSimple", method = RequestMethod.GET)
    public String getByTitleAndAuthorSimple(String title, String author) {
        PoemBean poemBean = poemService.getByTitleAndAuthorSimple(title, author);
        return gson.toJson(poemBean);
    }

    @RequestMapping(value = "/getByTitleAndAuthorDetail", method = RequestMethod.GET)
    public String getByTitleAndAuthorDetail(String title, String author) {
        PoemBean poemBean = poemService.getByTitleAndAuthorDetail(title, author);
        return gson.toJson(poemBean);
    }

    /**
     * 通过输入的内容，进行模糊查询（此处又疑问，可能查到多个匹配诗句，根据前端输入要求可以再调整）
     */
    @RequestMapping(value = "/getByContentDetail", method = RequestMethod.GET)
    public String getByContentDetail(String content) {
        PoemBean poemBean = poemService.getByContentDetail(content);
        return gson.toJson(poemBean);
    }

    //获取所有朝代
    @RequestMapping(value = "/getAllDynasty", method = RequestMethod.GET)
    public String getAllDynasty() {
        List<String> list = poemService.getAllDynasty();
        return gson.toJson(list);
    }

    //获取所有类型

    /**
     * 这个地方获取所有的 tag 然后分开成单独的词
     */
    @RequestMapping(value = "/getAllType", method = RequestMethod.GET)
    public String getAllType() {
        List<String> list = poemService.getAllType();
        return gson.toJson(list);
    }

    @RequestMapping(value = "/getBySearch", method = RequestMethod.GET)
    public String getBySearch(String key, Integer page, Integer size) {
        List<PoemBean> list = poemService.getBySearch(key, page, size);
        return gson.toJson(list);
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
    public String getByTag(String dynasty, String tag, Integer page, Integer size) {
        List<PoemBean> list = poemService.getByTag(dynasty, tag, page, size);
        return gson.toJson(list);
    }

    //服务器调用模型生成藏头诗
    @RequestMapping(value = "/generate", method = RequestMethod.GET)
    public String generate(String form, String head) {
        return JavaExecPython.createPoem(form, head);
    }

    @RequestMapping(value = "/emotion", method = RequestMethod.POST)
    public String emotion(@RequestParam("file")MultipartFile file) {
        System.out.println(file.getName());
        try {
            byte[] bytes = file.getBytes();
            //要存入本地的地址放到path里面
            Path path = Paths.get(FileUtils.UPLOAD_FOLDER + "/");
            //如果没有files文件夹，则创建
            if (!Files.isWritable(path)) {
                Files.createDirectories(path);
            }
            String extension = FileUtils.getFileExtension(file);  //获取文件后缀
            FileUtils.getFileByBytes(bytes, FileUtils.UPLOAD_FOLDER, file.getOriginalFilename());
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            return gson.toJson(JavaExecPython.emotionAnalyze(FileUtils.UPLOAD_FOLDER+file.getOriginalFilename()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
