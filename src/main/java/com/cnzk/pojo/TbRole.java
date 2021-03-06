package com.cnzk.pojo;


import java.util.List;

public class TbRole {

  private long roleId;
  private String roleName;
  private Integer roleSort;
  private List<TbMenu> menutblList;

  public TbRole() {
  }


  public long getRoleId() {
    return roleId;
  }

  public void setRoleId(long roleId) {
    this.roleId = roleId;
  }


  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public Integer getRoleSort() {
    return roleSort;
  }

  public void setRoleSort(Integer roleSort) {
    this.roleSort = roleSort;
  }

  public List<TbMenu> getMenutblList() {
    return menutblList;
  }

  public void setMenutblList(List<TbMenu> menutblList) {
    this.menutblList = menutblList;
  }
}
