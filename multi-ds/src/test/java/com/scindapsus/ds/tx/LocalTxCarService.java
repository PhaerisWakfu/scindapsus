package com.scindapsus.ds.tx;

import com.scindapsus.ds.annotation.WithDataSource;
import com.scindapsus.ds.mapper.CarMapper;
import org.springframework.aop.framework.AopContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 通过@WithDataSource(tx = true)来打开本地事务
 * <p>使用aopContext来调用本类其他方法需要设置aop的exposeProxy为true，不然会抛错
 *
 * @author wyh
 * @date 2022/8/24 14:37
 */
@EnableAspectJAutoProxy(exposeProxy = true)
@Service
public class LocalTxCarService {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    private final CarMapper carMapper;

    public LocalTxCarService(CarMapper carMapper) {
        this.carMapper = carMapper;
    }

    @WithDataSource(value = "second", tx = true)
    public void ds2() {
        String format = sdf.format(new Date());
        carMapper.update(3L, "ds2_" + format);
        carMapper.update(4L, "ds2_" + format);
    }

    @WithDataSource(value = "second", tx = true)
    public void ds2Throw() {
        String format = sdf.format(new Date());
        carMapper.update(3L, "ds2_" + format);
        int i = 1 / 0;
        carMapper.update(4L, "ds2_" + format);
    }

    @WithDataSource(value = "first", tx = true)
    public void nested() {
        String format = sdf.format(new Date());
        carMapper.update(1L, "ds1_" + format);
        carMapper.update(2L, "ds1_" + format);
        ((LocalTxCarService) AopContext.currentProxy()).ds2();
    }

    @WithDataSource(value = "first", tx = true)
    public void nestedThrow() {
        String format = sdf.format(new Date());
        carMapper.update(1L, "ds1_" + format);
        carMapper.update(2L, "ds1_" + format);
        ((LocalTxCarService) AopContext.currentProxy()).ds2Throw();
    }
}
