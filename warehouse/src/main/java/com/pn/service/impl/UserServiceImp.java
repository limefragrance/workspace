package com.pn.service.impl;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.pn.entity.Result;
import com.pn.entity.User;
import com.pn.mapper.UserMapper;
import com.pn.page.Page;
import com.pn.service.UserService;
import com.pn.utils.DigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Service
public class UserServiceImp implements UserService {

	//注入UserMapper
	@Autowired
	private UserMapper userMapper;

	//根据用户名查找用户的业务方法
	@Override
	public User findUserByCode(String userCode) {
		return userMapper.findUserByCode(userCode);
	}

	//分页查询用户的业务方法
	@Override
	public Page queryUserPage(Page page, User user) {

		//查询用户总行数
		int userCount = userMapper.selectUserCount(user);

		//分页查询用户
		List<User> userList = userMapper.selectUserPage(page, user);

		//将查询到的总行数和当前页数据组装到Page对象
		page.setTotalNum(userCount);
		page.setResultList(userList);

		return page;
	}

	//添加用户的业务方法
	@Override
	public Result saveUser(User user) {
		//根据用户名查询用户
		User oldUser = userMapper.findUserByCode(user.getUserCode());
		if(oldUser!=null){//用户已存在
			return Result.err(Result.CODE_ERR_BUSINESS, "该用户已存在！");
		}
		//用户不存在,对密码加密,添加用户
		String userPwd = DigestUtil.hmacSign(user.getUserPwd());
		user.setUserPwd(userPwd);
		userMapper.insertUser(user);
		return Result.ok("添加用户成功！");
	}

	//修改用户状态的业务方法
	@Override
	public Result updateUserState(User user) {
		//根据用户id修改用户状态
		int i = userMapper.updateUserState(user);
		if(i>0){
			return Result.ok("修改成功！");
		}
		return Result.err(Result.CODE_ERR_BUSINESS, "修改失败！");
	}

	//根据用户ids删除用户的业务方法
	@Override
	public Result removeUserByIds(List<Integer> userIdList) {
		int i = userMapper.setIsDeleteByUids(userIdList);
		if(i>0){
			return Result.ok("用户删除成功！");
		}
		return Result.err(Result.CODE_ERR_BUSINESS,"用户删除失败！");
	}

	//修改用户昵称的业务方法
	@Override
	public Result updateUserName(User user) {
		//根据用户id修改用户昵称
		int i = userMapper.updateNameById(user);
		if(i>0){//修改成功
			return Result.ok("用户修改成功！");
		}
		//修改失败
		return Result.err(Result.CODE_ERR_BUSINESS, "用户修改失败！");
	}

	//重置密码的业务方法
	@Override
	public Result resetPwd(Integer userId) {

		//创建User对象并保存用户id和加密后的重置密码123456
		User user = new User();
		user.setUserId(userId);
		user.setUserPwd(DigestUtil.hmacSign("123456"));

		//根据用户id修改密码
		int i = userMapper.updatePwdById(user);

		if(i>0){//密码修改成功
			return Result.ok("密码重置成功！");
		}
		//密码修改失败
		return Result.err(Result.CODE_ERR_BUSINESS, "密码重置失败！");
	}

	//查询所有用户
	@Override
	public void selectUser(HttpServletResponse response,Page page) throws IOException {
		List<User> userList = userMapper.selectUser(page);
		System.out.println(userList);
		//在内存操作，写出到浏览器
		ExcelWriter writer = ExcelUtil.getWriter(true);
		//自定义标题别名
		writer.addHeaderAlias("userId", "ID");
		writer.addHeaderAlias("userCode", "活动名称");
		writer.addHeaderAlias("userName", "开始时间");
		writer.addHeaderAlias("userPwd", "结束时间");
		writer.addHeaderAlias("userType", "礼品");
		writer.addHeaderAlias("userState", "库存");
		writer.addHeaderAlias("isDelete", "是否开始");
		writer.addHeaderAlias("createBy", "是否开始");
		writer.addHeaderAlias("createTime", "是否开始");
		writer.addHeaderAlias("updateTime", "是否开始");
		//一次性写出list内的对象到excel，使用默认样式，强制输出标题
		writer.write(userList, true);
		//设置浏览器响应格式
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
		String fileName = URLEncoder.encode("用户信息", "UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
		//输出流
		ServletOutputStream out = response.getOutputStream();
		//刷新到输出流
		writer.flush(out, true);
		out.close();
		writer.close();
	}
}
