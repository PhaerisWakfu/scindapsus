package com.scindapsus.ds.mapper;


import com.scindapsus.ds.model.Car;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author wyh
 * @since 1.0
 */
public interface CarMapper {

    @Select("select * from car where id=#{id}")
    Car getOne(@Param("id") Long id);

    @Update("update car set name=#{name} where id=#{id}")
    void update(@Param("id") Long id, @Param("name") String name);
}
