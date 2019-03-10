package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
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
@Api(tags = "2.API列表-标签")
@RestController
@CrossOrigin
@RequestMapping("/label")
public class LabelController {

	@Autowired
	private LabelService labelService;
	
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@ApiOperation("查询全部标签列表")
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",labelService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@ApiOperation("根据id查询标签")
	@ApiImplicitParam(name = "id",value = "标签id",required = true,dataType = "String",paramType = "path")
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",labelService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@ApiOperation("根据条件查询标签列表分页")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "searchMap",value = "查询条件",required = true,dataType = "Label",paramType = "body"),
			@ApiImplicitParam(name = "page",value = "页码",required = true,dataType = "int",paramType = "path"),
			@ApiImplicitParam(name = "size",value = "页大小",required = true,dataType = "int",paramType = "path")
	})
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<Label> pageList = labelService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Label>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
	@ApiOperation("根据条件查询标签列表")
	@ApiImplicitParam(name = "searchMap",value = "查询条件",required = true,dataType = "Label",paramType = "body")
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",labelService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param label
	 */
	@ApiOperation("添加标签")
	@ApiImplicitParam(name = "label",value = "标签信息",required = true,dataType = "Label",paramType = "body")
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Label label  ){
		labelService.add(label);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param label
	 */
	@ApiOperation("根据id修改标签")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "label",value = "标签信息",required = true,dataType = "Label",paramType = "body"),
			@ApiImplicitParam(name = "id",value = "标签id",required = true,dataType = "String",paramType = "path")
	})
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Label label, @PathVariable String id ){
		label.setId(id);
		labelService.update(label);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@ApiOperation("根据id删除标签")
	@ApiImplicitParam(name = "id",value = "标签id",required = true,dataType = "String",paramType = "path")
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		labelService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	
}
