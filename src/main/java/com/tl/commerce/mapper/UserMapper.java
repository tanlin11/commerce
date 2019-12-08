package com.tl.commerce.mapper;

import com.tl.commerce.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    /**
     * 查找用户
     * @param login_phone
     * @return
     */
    @Select("select * from user where loginname=#{loginname}")
    User getUserByLoginPhone(String login_phone);

    /**
     * 保存用户记录
     * @param user
     * @return
     */
    @Insert("INSERT INTO `tl`.`user` (`loginname`, `pwd`, `nick`, `avatar`, `ipid`, `remark`, `status`)" +
            " VALUES (#{loginname}, #{pwd}, #{nick}, #{avatar}, #{ipid}, #{remark}, #{status})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int save(User user);


}
