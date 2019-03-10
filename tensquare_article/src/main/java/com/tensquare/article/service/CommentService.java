package com.tensquare.article.service;

import com.tensquare.article.dao.CommentDao;
import com.tensquare.article.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.IdWorker;

@Service
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 添加文章评论
     * @param comment
     */
    public void addComment(Comment comment) {
        commentDao.save(comment);
    }

    /**
     * 根据文章id查询评论
     * @param articleId
     * @return
     */
    public Object findByArticleid(String articleId) {
        return commentDao.findByArticleid(articleId);
    }

    /**
     * 根据评论id删除评论
     * @param parentId
     */
    public void deleteComment(String parentId) {
        commentDao.deleteById(parentId);
    }
}
