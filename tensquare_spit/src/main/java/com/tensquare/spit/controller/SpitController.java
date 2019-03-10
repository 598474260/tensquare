package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
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

@Api(tags = "1.API列表-吐槽")
@RestController
@CrossOrigin
@RequestMapping("/spit")
public class SpitController {

    @Autowired
    private SpitService spitService;
    @Autowired
    private HttpServletRequest request;

    /**
     * 添加吐槽
     * @param spit
     * @return
     */
    @ApiOperation("添加吐槽")
    @ApiImplicitParam(name = "spit",value = "吐槽信息",required = true,dataType = "Spit",paramType = "body")
    @RequestMapping(value = "",method = RequestMethod.POST)
    public Result add(@RequestBody Spit spit){
        //从request域对象中获取到载荷信息
        Claims claims = (Claims)request.getAttribute("user_claims");
        //判断
        if (claims == null){
            return new Result(false,StatusCode.LOGINERROR,"请登录");
        }
        spitService.add(spit);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    /**
     * 查询所有吐槽
     * @return
     */
    @ApiOperation("查询所有吐槽列表")
    @RequestMapping(value = "",method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", spitService.findAll());
    }

    /**
     * 根据id查询吐槽
     * @param spitId
     * @return
     */
    @ApiOperation("根据id查询吐槽")
    @ApiImplicitParam(name="spitId",value = "吐槽id",required = true,dataType = "String",paramType = "path")
    @RequestMapping(value = "/{spitId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String spitId){
        return new Result(true,StatusCode.OK,"查询成功",spitService.findByid(spitId));
    }

    /**
     * 修改吐槽
     * @param spit
     * @return
     */
    @ApiOperation("修改吐槽")
    @ApiImplicitParam(name = "spit",value = "吐槽信息",required = true,dataType = "Spit",paramType = "body")
    @RequestMapping(value = "",method = RequestMethod.PUT)
    public Result update(@RequestBody Spit spit){
        //从request域对象中获取到载荷信息
        Claims claims = (Claims)request.getAttribute("user_claims");
        //判断
        if (claims == null){
            return new Result(false,StatusCode.LOGINERROR,"请登录");
        }
        spitService.update(spit);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除吐槽
     * @param spitId
     * @return
     */
    @ApiOperation("删除吐槽")
    @ApiImplicitParam(name="spitId",value = "吐槽id",required = true,dataType = "String",paramType = "path")
    @RequestMapping(value = "/{spitId}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable String spitId){
        //从request域对象中获取到载荷信息
        Claims claims = (Claims)request.getAttribute("user_claims");
        //判断
        if (claims == null){
            return new Result(false,StatusCode.LOGINERROR,"请登录");
        }
        spitService.delete(spitId);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    @ApiOperation("吐槽点赞")
    @ApiImplicitParam(name="spitId",value = "吐槽id",required = true,dataType = "String",paramType = "path")
    @RequestMapping(value = "/thumbup/{spitId}",method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String spitId){
        spitService.updateThumbup(spitId);
        return new Result(true,StatusCode.OK,"点赞成功");
    }

    @ApiOperation("根据上级id查询吐槽数据(分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId",value = "上级id",required = true,dataType = "String",paramType = "path"),
            @ApiImplicitParam(name = "page",value = "页码",required = true,dataType = "int",paramType = "path"),
            @ApiImplicitParam(name = "size",value = "页大小",required = true,dataType = "int",paramType = "path"),
    })
    @RequestMapping(value = "/comment/{parentId}/{page}/{size}",method = RequestMethod.GET)
    public Result comment(@PathVariable String parentId,@PathVariable int page,@PathVariable int size){
        Page<Spit> p = spitService.findByParentid(parentId,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Spit>(p.getTotalElements(),p.getContent()));
    }

    @ApiOperation("增加浏览量")
    @ApiImplicitParam(name="spitId",value = "吐槽id",required = true,dataType = "String",paramType = "path")
    @RequestMapping(value = "/visits/{spitId}",method = RequestMethod.PUT)
    public Result addVisits(@PathVariable String spitId){
        spitService.addVisits(spitId);
        return new Result(true,StatusCode.OK,"浏览量增加成功");
    }

    @ApiOperation("增加回复数")
    @ApiImplicitParam(name="spitId",value = "吐槽id",required = true,dataType = "String",paramType = "path")
    @RequestMapping(value = "/comment/{spitId}",method = RequestMethod.PUT)
    public Result comment(@PathVariable String spitId){
        //从request域对象中获取到载荷信息
        Claims claims = (Claims)request.getAttribute("user_claims");
        //判断
        if (claims == null){
            return new Result(false,StatusCode.LOGINERROR,"请登录");
        }
        spitService.addComment(spitId);
        return new Result(true,StatusCode.OK,"回复数增加成功");
    }
}
