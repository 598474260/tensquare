package com.tensquare.base.controller;

import com.tensquare.base.pojo.City;
import com.tensquare.base.service.CityService;
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

import java.util.Map;

/**
 * 控制器层
 *
 * @author Administrator
 */
@Api(tags = "1.API列表-城市")
@RestController
@CrossOrigin
@RequestMapping("/city")
public class CityController {

    @Autowired
    private CityService cityService;

    /**
     * 查询全部数据
     *
     * @return
     */
    @ApiOperation("查询全部城市列表")
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", cityService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @ApiOperation("根据id查询城市")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", cityService.findById(id));
    }

    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @ApiOperation("根据条件查询城市列表分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchMap",value = "查询条件",required = true,dataType = "City",paramType = "body"),
            @ApiImplicitParam(name = "page",value = "页码",required = true,dataType = "int",paramType = "path"),
            @ApiImplicitParam(name = "size",value = "页dax",required = true,dataType = "int",paramType = "path")
    })
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<City> pageList = cityService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<City>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @ApiOperation("根据条件查询城市列表")
    @ApiImplicitParam(name = "searchMap",value = "查询条件",required = true,dataType = "City",paramType = "body")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", cityService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param city
     */
    @ApiOperation("添加城市")
    @ApiImplicitParam(name = "city",value = "城市信息",required = true,dataType = "City",paramType = "body")
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody City city) {
        cityService.add(city);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param city
     */
    @ApiOperation("根据id修改城市")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "city",value = "城市信息",required = true,dataType = "City",paramType = "body"),
            @ApiImplicitParam(name = "id",value = "城市id",required = true,dataType = "String",paramType = "path")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody City city, @PathVariable String id) {
        city.setId(id);
        cityService.update(city);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @ApiOperation("根据id删除城市")
    @ApiImplicitParam(name = "id",value = "城市id",required = true,dataType = "String",paramType = "path")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        cityService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

}
