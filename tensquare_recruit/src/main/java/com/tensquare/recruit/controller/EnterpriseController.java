package com.tensquare.recruit.controller;

import com.tensquare.recruit.pojo.Enterprise;
import com.tensquare.recruit.service.EnterpriseService;
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
 * @author Administrator
 *
 */
@Api(tags = "1.API列表-企业")
@RestController
@CrossOrigin
@RequestMapping("/enterprise")
public class EnterpriseController {

	@Autowired
	private EnterpriseService enterpriseService;
	
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@ApiOperation("企业全部列表")
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",enterpriseService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@ApiOperation("根据id查询企业")
	@ApiImplicitParam(name = "id",value = "企业id",required = true,dataTypeClass = String.class,paramType = "path")
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",enterpriseService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@ApiOperation("根据条件查询企业分页")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "searchMap",value = "查询条件",dataType = "Enterprise"),
			@ApiImplicitParam(name = "page",value = "页码",required = true,dataTypeClass = int.class,paramType = "path"),
			@ApiImplicitParam(name = "size",value = "页大小",required = true,dataTypeClass = int.class,paramType = "path")
	})
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<Enterprise> pageList = enterpriseService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Enterprise>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
	@ApiOperation("根据条件查询企业")
	@ApiImplicitParam(name = "searchMap",value = "查询条件",dataType = "Enterprise")
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",enterpriseService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param enterprise
	 */
	@ApiOperation("添加企业")
	@ApiImplicitParam(name = "enterprise",value = "添加企业信息",dataType = "Enterprise")
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Enterprise enterprise  ){
		enterpriseService.add(enterprise);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param enterprise
	 */
	@ApiOperation("修改企业")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "enterprise",value = "修改内容",dataType = "Enterprise"),
			@ApiImplicitParam(name = "id",value = "企业id",required = true,dataTypeClass = String.class,paramType = "path")
	})
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Enterprise enterprise, @PathVariable String id ){
		enterprise.setId(id);
		enterpriseService.update(enterprise);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@ApiOperation("根据id删除企业")
	@ApiImplicitParam(name = "id",value = "企业id",required = true,dataTypeClass = String.class,paramType = "path")
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		enterpriseService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 查询热门企业列表
	 * @return
	 */
	@ApiOperation("热门企业列表")
	@RequestMapping(value = "/search/hotlist",method = RequestMethod.GET)
	public Result findByIshot(){
		return new Result(true,StatusCode.OK,"查询成功",enterpriseService.findByIshot());
	}
}
