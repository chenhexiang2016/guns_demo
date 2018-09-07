package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.Customer;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.mybatis.spring.SqlSessionTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 客户表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-06
 */
public interface CustomerMapper extends BaseMapper<Customer> {

    Integer insertBatchNew(List<Customer> list);
}
