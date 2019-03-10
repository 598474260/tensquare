package com.tensquare.article.controller;

import com.tensquare.article.pojo.Comment;
import com.tensquare.article.service.CommentService;
import entity.Result;
import entity.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "2.API列表-文章评论")
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 添加文章评论
     * @param comment
     * @return
     */
    @ApiOperation("添加文章评论")
    @ApiImplicitParam(name = "comment",value = "评论信息",required = true,dataType = "Comment",paramType = "body")
    @RequestMapping(value = "",method = RequestMethod.POST)
    public Result addComment(@RequestBody Comment comment){
        commentService.addComment(comment);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    /**
     * 根据文章id查询评论
     * @param articleId
     * @return
     */
    @ApiOperation("根据文章id查询评论")
    @ApiImplicitParam(name = "articleId",value = "文章id",required = true,dataType = "String",paramType = "path")
    @RequestMapping(value = "/article/{articleId}",method = RequestMethod.GET)
    public Result findByArticleid(@PathVariable String articleId){

        return new Result(true,StatusCode.OK,"查询成功",commentService.findByArticleid(articleId));
    }

    /**
     * 根据评论id删除评论
     * @param parentId
     * @return
     */
    @ApiOperation("删除评论")
    @ApiImplicitParam(name = "parentId",value = "评论id",required = true,dataType = "String",paramType = "path")
    @RequestMapping(value = "/{parentId}",method = RequestMethod.DELETE)
    public Result deleteComment(@PathVariable String parentId){
        commentService.deleteComment(parentId);
        return new Result(true,StatusCode.OK,"删除成功");
    }
}
