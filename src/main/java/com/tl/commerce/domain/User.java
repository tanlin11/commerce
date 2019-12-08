package com.tl.commerce.domain;


/**
 * 用户信息
 */
public class User {

  private Integer id;
  private String loginname;
  private String pwd;
  private String nick;
  private String avatar;
  private String avatarbig;
  private String ipid;
  private String remark;
  private Integer status;
  private java.sql.Timestamp createtime;
  private java.sql.Timestamp updatetime;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public String getLoginname() {
    return loginname;
  }

  public void setLoginname(String loginPhone) {
    this.loginname = loginPhone;
  }


  public String getPwd() {
    return pwd;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }


  public String getNick() {
    return nick;
  }

  public void setNick(String nick) {
    this.nick = nick;
  }


  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }


  public String getAvatarbig() {
    return avatarbig;
  }

  public void setAvatarbig(String avatarbig) {
    this.avatarbig = avatarbig;
  }


  public String getIpid() {
    return ipid;
  }

  public void setIpid(String ipid) {
    this.ipid = ipid;
  }


  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }


  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }


  public java.sql.Timestamp getCreatetime() {
    return createtime;
  }

  public void setCreatetime(java.sql.Timestamp createtime) {
    this.createtime = createtime;
  }


  public java.sql.Timestamp getUpdatetime() {
    return updatetime;
  }

  public void setUpdatetime(java.sql.Timestamp updatetime) {
    this.updatetime = updatetime;
  }

}
