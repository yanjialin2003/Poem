package com.douyuehan.doubao.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@TableName("poetry")
@AllArgsConstructor
@NoArgsConstructor
public class PoemBean {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    @NotBlank(message = "标题不可以为空")
    @TableField(value = "title")
    private String title;
    @TableField("desty")
    private String desty;
    @TableField("author")
    private String author;
    @NotBlank(message = "内容不可以为空")
    @TableField("`content`")
    private String content;
    @TableField("trans_content")
    private String trans_content;
    @TableField("appear")
    private String appear;
    @TableField("background")
    private String background;
    @TableField("tag")
    private String tag;
    @TableField("type")
    private String type;
    @TableField("date")
    private String date;
    @TableField("cipai")
    private String cipai;
    @TableField("qupai")
    private String qupai;
    @TableField("zhu")
    private String zhu;

    @TableField("fig")
    private String fig;

    private List<String> stars;
    private PoemAuthor authorDetail;

    public PoemAuthor getAuthorDetail() {
        return authorDetail;
    }

    public void setAuthorDetail(PoemAuthor authorDetail) {
        this.authorDetail = authorDetail;
    }

    public List<String> getStars() {
        return stars;
    }

    public void setStars(List<String> stars) {
        this.stars = stars;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesty() {
        return desty;
    }

    public void setDesty(String desty) {
        this.desty = desty;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTrans_content() {
        return trans_content;
    }

    public void setTrans_content(String trans_content) {
        this.trans_content = trans_content;
    }

    public String getAppear() {
        return appear;
    }

    public void setAppear(String appear) {
        this.appear = appear;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCipai() {
        return cipai;
    }

    public void setCipai(String cipai) {
        this.cipai = cipai;
    }

    public String getQupai() {
        return qupai;
    }

    public void setQupai(String qupai) {
        this.qupai = qupai;
    }

    public String getZhu() {
        return zhu;
    }

    public void setZhu(String zhu) {
        this.zhu = zhu;
    }
}
