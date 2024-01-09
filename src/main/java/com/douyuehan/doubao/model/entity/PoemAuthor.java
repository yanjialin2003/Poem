package com.douyuehan.doubao.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@TableName("author")
@AllArgsConstructor
@NoArgsConstructor
public class PoemAuthor {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "brief")
    private String brief;
    @TableField(value = "poem_count")
    private Integer poem_count;
    @TableField(value = "experience")
    private String experience;
    @TableField(value = "photo")
    private String photo;
    @TableField(value = "desty")
    private String desty;
    @TableField(value = "start_year")
    private String start_year;
    @TableField(value = "end_year")
    private String end_year;
    @TableField(value = "zi")
    private String zi;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public Integer getPoem_count() {
        return poem_count;
    }

    public void setPoem_count(Integer poem_count) {
        this.poem_count = poem_count;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDesty() {
        return desty;
    }

    public void setDesty(String desty) {
        this.desty = desty;
    }

    public String getStart_year() {
        return start_year;
    }

    public void setStart_year(String start_year) {
        this.start_year = start_year;
    }

    public String getEnd_year() {
        return end_year;
    }

    public void setEnd_year(String end_year) {
        this.end_year = end_year;
    }

    public String getZi() {
        return zi;
    }

    public void setZi(String zi) {
        this.zi = zi;
    }

    public String getHao() {
        return hao;
    }

    public void setHao(String hao) {
        this.hao = hao;
    }

    @TableField(value = "hao")
    private String hao;

}
