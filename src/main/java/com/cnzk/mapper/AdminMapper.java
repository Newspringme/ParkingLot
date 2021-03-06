package com.cnzk.mapper;

import com.cnzk.pojo.Admin;
import com.cnzk.pojo.Charge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author su
 * @date 2020/6/5-15:29
 */
@Mapper
public interface AdminMapper
{
	//	管理员登录
	Admin adminlogin(Map<String, Object> map);

	//	查询管理员记录数，包括带条件
	int queryAdminCount(Admin admin);

	//	查询管理员，包括带条件及分页
	List<Admin> queryAdmin(@Param("admin") Admin admin, @Param("start") int start, @Param("pageSize") int pageSize);

	//  添加管理员
	int addAdmin(Admin admin);

	//	删除管理员
	int deleteAdmin(int[] array);

	//	更新管理员信息
	int updateAdmin(Admin admin);

	//	上传头像
	int uploadAdminImg(@Param("headImg")String headImg,@Param("adminName")String adminName);

	//更新状态
	Integer updateState(Admin admin);


}
