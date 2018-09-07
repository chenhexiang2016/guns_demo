package com.stylefeng.guns.modular.customer.service.impl;

import com.stylefeng.guns.core.excel.ExcelLogs;
import com.stylefeng.guns.core.excel.ExcelUtil;
import com.stylefeng.guns.modular.system.model.Customer;
import com.stylefeng.guns.modular.system.dao.CustomerMapper;
import com.stylefeng.guns.modular.customer.service.ICustomerService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-06
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Override
    public Collection parseExcel(String fileName) throws Exception {
        File f = new File(fileName);
        InputStream inputStream= new FileInputStream(f);

        ExcelLogs logs = new ExcelLogs();
        Collection<Map> importExcel = ExcelUtil.importExcel(Map.class, inputStream, "yyyy/MM/dd HH:mm:ss", logs , 0);

        return importExcel;
    }

    /*@Override
    public Integer insertBatchNew(List<Customer> list) {
        return this.baseMapper.insertBatchNew(list);
    }*/
    @Resource(name = "sqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;

    public SqlSessionTemplate getSqlSessionTemplate()
    {
        return sqlSessionTemplate;
    }

    @Override
    public Integer insertBatchNew(List<Customer> members) throws Exception {
        // TODO Auto-generated method stub
        int result = 1;
        SqlSession batchSqlSession = null;
        try {
            batchSqlSession = this.getSqlSessionTemplate()
                    .getSqlSessionFactory()
                    .openSession(ExecutorType.BATCH, false);// 获取批量方式的sqlsession
            int batchCount = 5000;// 每批commit的个数
            int batchLastIndex = batchCount;// 每批最后一个的下标
            for (int index = 0; index < members.size();) {
                if (batchLastIndex >= members.size()) {
                    batchLastIndex = members.size();
                    result = result * batchSqlSession.insert("com.stylefeng.guns.modular.system.dao.CustomerMapper.insertBatchNew",members.subList(index, batchLastIndex));
                    batchSqlSession.commit();
                    System.out.println("index:" + index+ " batchLastIndex:" + batchLastIndex);
                    break;// 数据插入完毕，退出循环
                } else {
                    result = result * batchSqlSession.insert("com.stylefeng.guns.modular.system.dao.CustomerMapper.insertBatchNew",members.subList(index, batchLastIndex));
                    batchSqlSession.commit();
                    System.out.println("index:" + index+ " batchLastIndex:" + batchLastIndex);
                    index = batchLastIndex;// 设置下一批下标
                    batchLastIndex = index + (batchCount - 1);
                }
            }
            batchSqlSession.commit();
        }
        finally {
            batchSqlSession.close();
        }
        return result;
    }
}
