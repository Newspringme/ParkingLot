package com.cnzk.service;

import com.cnzk.mapper.AdminMapper;
import com.cnzk.mapper.RoleMapper;
import com.cnzk.pojo.*;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
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

    //  可修改的角色的权限列表
    @Override
    public LayuiData queryRolesList(HashMap<String, Object> condition){

        Integer roleId = Integer.parseInt(condition.get("roleId").toString());
        TbRole tbRole = roleMapper.querySort(roleId);
        condition.put("roleSort",tbRole.getRoleSort());

        List<TbRole> tbRoleList = roleMapper.queryRolesList(condition);

        int count = roleMapper.queryCount(condition);
        LayuiData layuiData=new LayuiData();
        layuiData.setCode(0);
        layuiData.setCount(count);
        layuiData.setData(tbRoleList);
        return layuiData;
    }

    @Override
    public Integer updateMenuTree(String treeStr, Integer roleId) {
        Integer row = 0;
//        Gson gson = new Gson();
//        MenuVo[] treeData = gson.fromJson(treeStr, MenuVo[].class);
//        System.out.println(treeData);
//        //总的二级菜单
//        List<TbMenu> menutblList = new ArrayList<>();
//        for (int i=0;i<treeData.length;i++){
//            for (int j=0;j<treeData[i].getChildren().size();j++){
//                TbMenu tbMenu = new TbMenu();
//                tbMenu.setMenuId(treeData[i].getChildren().get(j).getId());
//                tbMenu.setRoleId(rolesid);
//                menutblList.add(tbMenu);
//            }
//        }
//        System.out.println("------------");
//        System.out.println(menutblList.toString());
//        System.out.println("------------");
//        row += roleMapper.deleRolesMenu(rolesid);
//        row += roleMapper.addRolesMenu(menutblList);
        return row;
    }
}
