package com.tensquare.qa.controller;

import com.tensquare.qa.client.LabelClient;
import com.tensquare.qa.pojo.Problem;
import com.tensquare.qa.service.ProblemService;
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
 *
 * @author Administrator
 */
@Api(tags = "1.API列表-问题")
@RestController
@CrossOrigin
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private LabelClient labelClient;

    /**
     * 查询全部数据
     *
     * @return
     */
    @ApiOperation("查询所有问题列表")
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", problemService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @ApiOperation("根据id查询问题")
    @ApiImplicitParam(name = "id", value = "问题id", required = true, dataType = "String", paramType = "path")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", problemService.findById(id));
    }

    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @ApiOperation("根据条件查询问题列表分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchMap", value = "查询条件", required = true, dataType = "Problem", paramType = "body"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页大小", required = true, dataType = "int", paramType = "path"),
    })
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<Problem> pageList = problemService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Problem>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @ApiOperation("根据条件查询问题列表")
    @ApiImplicitParam(name = "searchMap", value = "查询条件", required = true, dataType = "Problem", paramType = "body")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", problemService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param problem
     */
    @ApiOperation("添加问题")
    @ApiImplicitParam(name = "problem", value = "问题信息", required = true, dataType = "Problem", paramType = "body")
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Problem problem) {
        //从request域对象中获取到载荷信息
        Claims claims = (Claims)request.getAttribute("user_claims");
        //判断
        if (claims == null){
            return new Result(false,StatusCode.LOGINERROR,"请登录或权限不足");
        }
        problemService.add(problem);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param problem
     */
    @ApiOperation("根据id修改问题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "problem", value = "问题信息", required = true, dataType = "Problem", paramType = "body"),
            @ApiImplicitParam(name = "id", value = "问题id", required = true, dataType = "String", paramType = "path")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Problem problem, @PathVariable String id) {
        //从request域对象中获取到载荷信息
        Claims claims = (Claims)request.getAttribute("user_claims");
        //判断
        if (claims == null){
            return new Result(false,StatusCode.LOGINERROR,"请登录");
        }
        problem.setId(id);
        problemService.update(problem);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @ApiOperation("根据id删除问题")
    @ApiImplicitParam(name = "id", value = "问题id", required = true, dataType = "String", paramType = "path")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        //从request域对象中获取到载荷信息
        Claims claims = (Claims)request.getAttribute("user_claims");
        //判断
        if (claims == null){
            return new Result(false,StatusCode.LOGINERROR,"请登录");
        }
        problemService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 查询最新问答列表
     *
     * @param labelId
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("最新问答列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "labelId", value = "标签id", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页大小", required = true, dataType = "int", paramType = "path")
    })
    @RequestMapping(value = "/newlist/{labelId}/{page}/{size}", method = RequestMethod.GET)
    public Result findNewListByLabelId(@PathVariable String labelId, @PathVariable int page, @PathVariable int size) {
        Page<Problem> p = problemService.findNewListByLabelId(labelId, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Problem>(p.getTotalElements(), p.getContent()));
    }

    /**
     * 查询热门问答列表
     *
     * @param labelId
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("热门问答列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "labelId", value = "标签id", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页大小", required = true, dataType = "int", paramType = "path")
    })
    @RequestMapping(value = "/hotlist/{labelId}/{page}/{size}", method = RequestMethod.GET)
    public Result findHotListByLabelId(@PathVariable String labelId, @PathVariable int page, @PathVariable int size) {
        Page<Problem> p = problemService.findHotListByLabelId(labelId, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Problem>(p.getTotalElements(), p.getContent()));
    }

    /**
     * 查询等待回答列表
     * @param labelId
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("等待回答列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "labelId", value = "标签id", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页大小", required = true, dataType = "int", paramType = "path")
    })
    @RequestMapping(value = "/waitlist/{labelId}/{page}/{size}",method = RequestMethod.GET)
    public Result findWaitListByLabelId(@PathVariable String labelId, @PathVariable int page, @PathVariable int size) {
        Page<Problem> p = problemService.findWaitListByLabelId(labelId, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Problem>(p.getTotalElements(), p.getContent()));
    }

    @RequestMapping(value="/label/{id}", method = RequestMethod.GET)
    public Result findLabelById(@PathVariable("id") String id){
        return labelClient.findById(id);
    }
}
