package com.scindapsus.ds.tx;

import com.scindapsus.ds.annotation.WithDataSource;
import com.scindapsus.ds.mapper.CarMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wyh
 * @since 2022/7/4
 */
@Service
public class SpringTxCarService {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    private final CarMapper carMapper;

    public SpringTxCarService(CarMapper carMapper) {
        this.carMapper = carMapper;
    }

    /**
     * 单数据源无事务抛错
     */
    public void tx1OffAndThrow() {
        String format = sdf.format(new Date());
        carMapper.update(1L, "hello1_" + format);
        int i = 1 / 0;
        carMapper.update(2L, "world1_" + format);
    }

    /**
     * 加事务注解更新库1
     */
    @WithDataSource("first")
    @Transactional
    public void tx1On() {
        String format = sdf.format(new Date());
        carMapper.update(1L, "hello1_" + format);
        carMapper.update(2L, "world1_" + format);
    }

    /**
     * 加事务注解更新库2
     */
    @Transactional
    @WithDataSource("second")
    public void tx2On() {
        String format = sdf.format(new Date());
        carMapper.update(3L, "hello2_" + format);
        carMapper.update(4L, "world2_" + format);
    }

    /**
     * 加事务注解更新库2并抛错
     */
    @Transactional
    @WithDataSource("second")
    public void tx2OnAndThrow() {
        String format = sdf.format(new Date());
        carMapper.update(3L, "hello2_" + format);
        int i = 1 / 0;
        carMapper.update(4L, "world2_" + format);
    }
}
