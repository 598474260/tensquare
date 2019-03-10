package com.tensquare.user.controller;

import com.tensquare.user.pojo.Admin;
import com.tensquare.user.service.AdminService;
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
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制器层
 *
 * @author Administrator
 */
@Api(tags = "2.API列表-管理员")
@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private HttpServletRequest request;

    /**
     * 查询全部数据
     *
     * @return
     */
    @ApiOperation("管理员全部列表")
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", adminService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @ApiOperation("根据id查询管理员")
    @ApiImplicitParam(name = "id", value = "管理员id", required = true, dataType = "String", paramType = "path")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", adminService.findById(id));
    }

    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @ApiOperation("条件查询管理员(分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchMap", value = "查询条件", required = true, dataType = "Admin", paramType = "body"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页大小", required = true, dataType = "int", paramType = "path")
    })
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<Admin> pageList = adminService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Admin>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @ApiOperation("条件查询管理员")
    @ApiImplicitParam(name = "searchMap", value = "查询条件", required = true, dataType = "Admin", paramType = "body")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", adminService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param admin
     */
    @ApiOperation("添加管理员")
    @ApiImplicitParam(name = "admin", value = "管理员数据", required = true, dataType = "Admin", paramType = "body")
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Admin admin) {
        adminService.add(admin);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param admin
     */
    @ApiOperation("根据id修改管理员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "admin", value = "管理员数据", required = true, dataType = "Admin", paramType = "body"),
            @ApiImplicitParam(name = "id", value = "管理员id", required = true, dataType = "String", paramType = "path")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Admin admin, @PathVariable String id) {
        admin.setId(id);
        adminService.update(admin);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @ApiOperation("根据id删除管理员")
    @ApiImplicitParam(name = "id", value = "管理员id", required = true, dataType = "String", paramType = "path")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        //获取header
        String header = request.getHeader("Authorization");

        if (null == header){
            return new Result(false,StatusCode.LOGINERROR,"请登录");
        }
        //解析header,获取token
        String token = header.substring(7);
        //解析token获取载荷信息
        Claims claims = jwtUtil.parseToken(token);

        if (null == claims){
            return new Result(false,StatusCode.LOGINERROR,"请登录");
        }

        //获取角色信息
        String roles = (String) claims.get("roles");
        if (!"admin".equals(roles)){
            return new Result(false,StatusCode.ACCESSERROR,"权限不足");
        }
        adminService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 管理员登录
     * @param admin
     * @return
     */
    @ApiOperation("管理员登录")
    @ApiImplicitParam(name = "admin", value = "管理员数据", required = true, dataType = "Admin", paramType = "body")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Result login(@RequestBody Admin admin) {

        Admin logAdmin = adminService.login(admin);
        if (null == logAdmin){
            return new Result(false,StatusCode.ERROR,"登录失败");
        }
        String token = jwtUtil.createToken(logAdmin.getId(), logAdmin.getLoginname(), "admin");
        token = "Bearer "+token;
        Map<String,String> map = new HashMap<>();
        map.put("token",token);
        map.put("name",logAdmin.getLoginname());
        return new Result(true, StatusCode.OK, "登录成功",map);
    }

}
