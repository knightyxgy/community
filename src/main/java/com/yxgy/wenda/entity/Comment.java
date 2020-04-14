package com.yxgy.wenda.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {

    private int id;
    private int userId;
    //评论帖子，还是评论别人的评论等
    private int entityType;
    private int entityId;
    //评论回复需要有回复的对象
    private int targetId;
    private String content;
    private int status;
    private Date createTime;
}
