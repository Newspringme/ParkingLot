package com.cnzk.service;

import com.cnzk.mapper.AdminMapper;
import com.cnzk.mapper.RoleMapper;
import com.cnzk.pojo.Admin;
import com.cnzk.pojo.LayuiData;
import com.cnzk.pojo.TbRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleServeice{

    @Resource
    private RoleMapper roleMapper;
    @Override
    public LayuiData queryRole(int start, int pageSize) {
        List<TbRole> list=roleMapper.queryRole(start, pageSize);
        int count=roleMapper.queryRoleCount();
        LayuiData layuiData=new LayuiData();
        layuiData.setCode(0);
        layuiData.setCount(count);
        layuiData.setData(list);
        return layuiData;
    }

    @Override
    public int addRole(TbRole role) {
        return roleMapper.addRole(role);
    }

    @Override
    public int delecteRole(TbRole role) {
        return roleMapper.deleteRole(role);
    }

    @Override
    public int editRole(TbRole role) {
        return roleMapper.editRole(role);
    }
}
