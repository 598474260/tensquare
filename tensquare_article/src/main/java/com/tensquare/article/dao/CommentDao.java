package com.tensquare.article.dao;

import com.tensquare.article.pojo.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentDao extends MongoRepository<Comment,String> {

    /**
     * 根据文章id查询评论
     * @param articleId
     * @return
     */
    Object findByArticleid(String articleId);
}
