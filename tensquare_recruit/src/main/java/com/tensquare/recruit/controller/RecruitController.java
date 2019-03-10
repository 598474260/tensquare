package com.tensquare.recruit.controller;

import com.tensquare.recruit.pojo.Recruit;
import com.tensquare.recruit.service.RecruitService;
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

import java.util.List;
import java.util.Map;

/**
 * 控制器层
 *
 * @author Administrator
 */
@Api(tags = "2.API列表-招聘")
@RestController
@CrossOrigin
@RequestMapping("/recruit")
public class RecruitController {

    @Autowired
    private RecruitService recruitService;

    /**
     * 查询全部数据
     *
     * @return
     */
    @ApiOperation("查询全部招聘信息")
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", recruitService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @ApiOperation("根据id查询招聘信息")
    @ApiImplicitParam(name = "id", value = "招聘信息id", required = true, dataTypeClass = String.class, paramType = "path")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", recruitService.findById(id));
    }

    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @ApiOperation("根据条件查询招聘信息列表分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchMap", value = "查询条件", dataType = "Recruit"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataTypeClass = int.class, paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页大小", required = true, dataTypeClass = int.class, paramType = "path"),
    })
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<Recruit> pageList = recruitService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Recruit>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @ApiOperation("根据条件查询招聘信息列表")
    @ApiImplicitParam(name = "searchMap", value = "查询条件", dataType = "Recruit")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", recruitService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param recruit
     */
    @ApiOperation("添加招聘信息")
    @ApiImplicitParam(name = "recruit", value = "招聘信息", dataType = "Recruit")
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Recruit recruit) {
        recruitService.add(recruit);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param recruit
     */
    @ApiOperation("根据id修改招聘信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "recruit", value = "修改信息", dataType = "Recruit"),
            @ApiImplicitParam(name = "id", value = "招聘id", required = true, dataTypeClass = String.class, paramType = "path"),
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Recruit recruit, @PathVariable String id) {
        recruit.setId(id);
        recruitService.update(recruit);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @ApiOperation("根据id删除招聘")
    @ApiImplicitParam(name = "id", value = "招聘信息id", required = true, dataTypeClass = String.class, paramType = "path")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        recruitService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 推荐职位
     * @return
     */
    @ApiOperation("推荐职位")
    @RequestMapping(value = "/search/recommend", method = RequestMethod.GET)
    public Result recommend() {
        List<Recruit> recruitList = recruitService.findTop4ByStateOOrderByCreatetimeDesc();
        return new Result(true, StatusCode.OK, "查询成功", recruitList);
    }

    /**
     * 最新职位
     * @return
     */
    @ApiOperation("最新职位")
    @RequestMapping(value = "/search/newlist",method = RequestMethod.GET)
    public Result newlist() {
        List<Recruit> list = recruitService.findTop12ByStateNotOrderByCreatetimeDesc();
        return new Result(true, StatusCode.OK, "查询成功", list);
    }
}
