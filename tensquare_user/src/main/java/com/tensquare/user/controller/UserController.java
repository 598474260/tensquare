package com.tensquare.user.controller;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;
import util.RandomCode;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 控制器层
 *
 * @author Administrator
 */
@Api(tags = "1.API列表-用户")
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RandomCode randomCode;

    /**
     * 查询全部数据
     *
     * @return
     */
    @ApiOperation("查询所有用户")
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", userService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @ApiOperation("根据id查询用户")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "String", paramType = "path")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findById(id));
    }

    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @ApiOperation("条件查询用户列表(分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchMap", value = "查询条件", required = true, dataType = "User", paramType = "body"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页dax", required = true, dataType = "int", paramType = "path"),
    })
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<User> pageList = userService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<User>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @ApiOperation("条件查询")
    @ApiImplicitParam(name = "searchMap", value = "查询条件", required = true, dataType = "User", paramType = "body")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param user
     */
    @ApiOperation("添加用户")
    @ApiImplicitParam(name = "user", value = "用户数据", required = true, dataType = "User", paramType = "body")
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody User user) {
        //从request域对象中获取到载荷信息
        Claims claims = (Claims) request.getAttribute("admin_claims");
        //判断
        if (claims == null) {
            return new Result(false, StatusCode.LOGINERROR, "请登录或者权限不足");
        }
        userService.add(user);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param user
     */
    @ApiOperation("根据id修改用户")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "String", paramType = "path")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody User user, @PathVariable String id) {
        // 获取对象
        Claims claims = (Claims) request.getAttribute("admin_claims");
        // 判断
        if (null == claims) {
            return new Result(true, StatusCode.ACCESSERROR, "请登录或者权限不足");
        }
        user.setId(id);
        userService.update(user);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @ApiOperation("根据id删除用户")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "String", paramType = "path")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        // 获取对象
        Claims claims = (Claims) request.getAttribute("admin_claims");
        // 判断
        if (null == claims) {
            return new Result(true, StatusCode.ACCESSERROR, "请登录或者权限不足");
        }
        userService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 发送手机验证码
     *
     * @param mobile
     * @return
     */
    @ApiOperation("发送手机验证码")
    @ApiImplicitParam(name = "mobile", value = "手机号码", required = true, dataType = "String", paramType = "path")
    @RequestMapping(value = "/sendsms/{mobile}", method = RequestMethod.POST)
    public Result sendsms(@PathVariable String mobile) {

        //先生成手机验证码
        String code = randomCode.genCode();
        System.out.println("手机验证码: " + code);
        //给消息队列发送消息,使用rabbitMQ,导入jar包
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("code", code);
        //直接模式发送消息
        //rabbitTemplate.convertAndSend("","sendCode",map);
        //把验证码保存到redis缓存中(过期时间5分钟)
        redisTemplate.opsForValue().set("userSmsCode_" + mobile, code, 5, TimeUnit.MINUTES);
        return new Result(true, StatusCode.OK, "发送成功");
    }

    /**
     * 用户注册
     *
     * @param user
     * @param code
     * @return
     */
    @ApiOperation("用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户数据", required = true, dataType = "User", paramType = "body"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String", paramType = "path")
    })
    @RequestMapping(value = "/register/{code}", method = RequestMethod.POST)
    public Result register(@RequestBody User user, @PathVariable String code) {
        userService.register(user, code);
        return new Result(true, StatusCode.OK, "注册成功");
    }

    /**
     * 用户登录
     *
     * @param user
     * @return
     */
    @ApiOperation("用户登录")
    @ApiImplicitParam(name = "user", value = "用户数据", required = true, dataType = "User", paramType = "body")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody User user) {
        User logUser = userService.login(user);
        //如果为null登录失败
        if (null == logUser) {
            return new Result(false, StatusCode.LOGINERROR, "用户名或密码错误");
        }
        //生成token字符串 admin表示管理员 user表示普通用户
        String token = jwtUtil.createToken(logUser.getId(), logUser.getMobile(), "user");
        //响应
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("name", logUser.getNickname());//昵称
        map.put("avatar", logUser.getAvatar());//头像
        return new Result(true, StatusCode.OK, "登录成功", map);
    }

    /**
     * 增加粉丝数
     *
     * @param userid
     * @param x
     */
    @RequestMapping(value = "/incfans/{userid}/{x}", method = RequestMethod.POST)
    public void incFanscount(@PathVariable String userid, @PathVariable int x) {
        userService.incFanscount(userid, x);
    }

    /**
     * 增加关注数
     *
     * @param userid
     * @param x
     */
    @RequestMapping(value = "/incfollow/{userid}/{x}", method = RequestMethod.POST)
    public void incFollowcount(@PathVariable String userid, @PathVariable int x) {
        userService.incFollowcount(userid, x);
    }
}
