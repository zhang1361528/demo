package cn.zl.dao;

import cn.zl.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Administrator
 */
public interface UserDao extends JpaRepository<User,Integer> {
    @Query(value = "select * from user u where u.user_name=:name and u.user_pwd=:pwd",nativeQuery = true)
    public User getuser(@Param("name") String userName, @Param("pwd") int pwd);
    @Query(value = "select * from user u where u.user_name=:name",nativeQuery = true)
    public User getUserByName(@Param("name") String name);
    /**
     * 邮箱激活
     */
    @Modifying
    @Query(value = "update user set user.activate_mark=1 where user.user_id=:id",nativeQuery = true)
    int update(@Param("id") int id);
}
