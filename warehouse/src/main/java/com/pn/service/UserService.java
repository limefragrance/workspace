package com.pn.service;

import com.pn.entity.Result;
import com.pn.entity.User;
import com.pn.page.Page;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService {

	//根据用户名查找用户的业务方法
	public User findUserByCode(String userCode);

	//分页查询用户的业务方法
	public Page queryUserPage(Page page, User user);

	//添加用户的业务方法
	public Result saveUser(User user);

	//修改用户状态的业务方法
	public Result updateUserState(User user);

	//根据用户ids删除用户的业务方法
	public Result removeUserByIds(List<Integer> userIdList);

	//修改用户昵称的业务方法
	public Result updateUserName(User user);

	//重置密码的业务方法
	public Result resetPwd(Integer userId);

	//查询所有用户
	public void selectUser(HttpServletResponse response,Page page) throws IOException;
}
