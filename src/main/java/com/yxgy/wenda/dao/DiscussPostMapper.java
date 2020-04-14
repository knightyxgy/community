package com.yxgy.wenda.dao;

import com.yxgy.wenda.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    //当userid没有值，才拼到sql
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    //如果要动态查询，只有一个参数时必须要加@Param
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);

    int updateCommentCount(int id, int commentCount);
}
