package com.tensquare.search.controller;

import com.tensquare.search.pojo.Article;
import com.tensquare.search.service.ArticleSearchService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Api(tags = "1.API列表-搜索")
@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleSearchController {

    @Autowired
    private ArticleSearchService articleSearchService;

    @ApiOperation("添加文章")
    @ApiImplicitParam(name = "article",value = "文章数据",required = true,dataType = "Article",paramType = "body")
    @RequestMapping(value = "",method = RequestMethod.POST)
    public Result add(@RequestBody Article article){
        articleSearchService.add(article);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    @ApiOperation("关键字查询文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keywords",value = "关键字",required = true,dataType = "String",paramType = "path"),
            @ApiImplicitParam(name = "page",value = "页码",required = true,dataType = "int",paramType = "path"),
            @ApiImplicitParam(name = "size",value = "页大小",required = true,dataType = "int",paramType = "path")
    })
    @RequestMapping(value = "/search/{keywords}/{page}/{size}",method = RequestMethod.GET)
    public Result findByTitleLike(@PathVariable String keywords,@PathVariable int page,@PathVariable int size){
        Page<Article> p = articleSearchService.findByTitleLike(keywords,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Article>(p.getTotalElements(),p.getContent()));
    }
}
