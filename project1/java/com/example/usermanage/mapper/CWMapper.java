package com.example.usermanage.mapper;
import com.example.usermanage.entity.CarWash;
import org.apache.ibatis.annotations.*;
import java.util.List;


@Mapper
public interface CWMapper {
    @Delete("delete from t_carwash " +
            "where cw_id=#{cw_id}")
    int delCW(@Param("cw_id") int cw_id);
    @Select("Select * from t_carwash " +
            "where cw_name like #{cw_name} and location like #{location} limit #{pageNum},#{pageSize}")
    List<CarWash> findPage(int pageNum, int pageSize,String cw_name,String location);
    @Select("Select * from t_carwash " +
            "where cw_id=#{cw_id}")
    List<CarWash> findPageById(int cw_id);
    @Select("SELECT count(*) from t_carwash " +
            "where cw_name like #{cw_name} and location like #{location}")
    int countCW(String cw_name,String location);
    @Update("UPDATE t_carwash SET cw_name=#{cw_name},cw_phone=#{cw_phone},openhour=#{openhour},location=#{location},description=#{description} " +
            "WHERE cw_id=#{cw_id}")
    int updateCW(CarWash carWash);
    @Insert("insert into t_carwash(cw_name,owner,cw_phone,openhour,location,description,cw_image) " +
            "values(#{cw_name},#{owner},#{cw_phone},#{openhour},#{location},#{description},#{cw_image})")
    int insertCW(CarWash carWash);
}
