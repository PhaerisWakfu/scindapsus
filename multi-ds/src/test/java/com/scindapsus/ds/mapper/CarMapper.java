package com.scindapsus.ds.mapper;


import com.scindapsus.ds.model.Car;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author wyh
 * @date 2022/7/5 14:01
 */
public interface CarMapper {

    @Select("select * from car where id=#{id}")
    Car getOne(@Param("id") Long id);
}
