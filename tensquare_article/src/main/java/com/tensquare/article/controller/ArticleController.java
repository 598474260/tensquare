package com.tensquare.article.controller;

import com.tensquare.article.pojo.Article;
import com.tensquare.article.service.ArticleService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
/**
 * 控制器层
 * @author Administrator
 *
 */
@Api(tags = "1.API列表-文章")
@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleController {

	@Autowired
	private ArticleService articleService;
	@Autowired
	private HttpServletRequest request;

	/**
	 * 查询全部数据
	 * @return
	 */
	@ApiOperation("查询全部文章列表")
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",articleService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@ApiOperation("根据id查询文章")
	@ApiImplicitParam(name = "id",value = "文章id",required = true,dataType = "int",paramType = "path")
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",articleService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@ApiOperation("根据条件查询文章列表分页")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "searchMap",value = "查询条件",required = true,dataType = "Article",paramType = "body"),
			@ApiImplicitParam(name = "page",value = "页码",required = true,dataType = "int",paramType = "path"),
			@ApiImplicitParam(name = "size",value = "页大小",required = true,dataType = "int",paramType = "path")
	})
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<Article> pageList = articleService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Article>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
	@ApiOperation("根据条件查询文章列表")
	@ApiImplicitParam(name = "searchMap",value = "查询条件",required = true,dataType = "Article",paramType = "body")
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",articleService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param article
	 */
	@ApiOperation("添加文章")
	@ApiImplicitParam(name = "article",value = "文章信息",required = true,dataType = "Article",paramType = "body")
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Article article  ){
		//从request域对象中获取到载荷信息
		Claims claims = (Claims)request.getAttribute("user_claims");
		//判断
		if (claims == null){
			return new Result(false,StatusCode.LOGINERROR,"请登录");
		}
		articleService.add(article);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param article
	 */
	@ApiOperation("根据id修改文章")
	@ApiImplicitParam(name = "id",value = "文章id",required = true,dataType = "int",paramType = "path")
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Article article, @PathVariable String id ){
		//从request域对象中获取到载荷信息
		Claims claims = (Claims)request.getAttribute("user_claims");
		//判断
		if (claims == null){
			return new Result(false,StatusCode.LOGINERROR,"请登录");
		}
		article.setId(id);
		articleService.update(article);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@ApiOperation("根据id删除文章")
	@ApiImplicitParam(name = "id",value = "文章id",required = true,dataType = "int",paramType = "path")
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		//从request域对象中获取到载荷信息
		Claims claims = (Claims)request.getAttribute("user_claims");
		//判断
		if (claims == null){
			return new Result(false,StatusCode.LOGINERROR,"请登录");
		}
		articleService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 文章审核
	 * @param articleId
	 * @return
	 */
	@ApiOperation("文章审核")
	@ApiImplicitParam(name = "articleId",value = "文章id",required = true,dataType = "int",paramType = "path")
	@RequestMapping(value = "/examine/{articleId}",method = RequestMethod.PUT)
	public Result examine(@PathVariable String articleId){
		//从request域对象中获取到载荷信息
		Claims claims = (Claims)request.getAttribute("admin_claims");
		//判断
		if (claims == null){
			return new Result(false,StatusCode.LOGINERROR,"请登录或权限不足");
		}
		Article article = articleService.findById(articleId);
		article.setState("1");
		articleService.update(article);
		return new Result(true,StatusCode.OK,"审核成功");
	}

	@ApiOperation("文章点赞")
	@ApiImplicitParam(name = "articleId",value = "文章id",required = true,dataType = "int",paramType = "path")
	@RequestMapping(value = "/thumbup/{articleId}",method = RequestMethod.PUT)
	public Result thumbup(@PathVariable String articleId){
		Article article = articleService.findById(articleId);
		article.setThumbup(article.getThumbup()+1);
		articleService.update(article);
		return new Result(true,StatusCode.OK,"点赞成功");
	}
}
