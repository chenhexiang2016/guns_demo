package com.stylefeng.guns.modular.customer.service;

import com.stylefeng.guns.modular.system.model.Customer;
import com.baomidou.mybatisplus.service.IService;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 客户表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-06
 */
public interface ICustomerService extends IService<Customer> {

    /**
     * 解析数据表
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    Collection parseExcel(String fileName) throws Exception;

    Integer insertBatchNew(List<Customer> list) throws Exception;
}
