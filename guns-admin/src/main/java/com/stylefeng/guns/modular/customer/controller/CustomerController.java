package com.stylefeng.guns.modular.customer.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.config.properties.GunsProperties;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.shiro.ShiroUser;
import com.stylefeng.guns.modular.common.JsonResult;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.beetl.ext.fn.Json;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.system.model.Customer;
import com.stylefeng.guns.modular.customer.service.ICustomerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.*;

/**
 * 客户管理控制器
 *
 * @author fengshuonan
 * @Date 2018-09-06 10:50:02
 */
@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController {

    private String PREFIX = "/customer/customer/";

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private GunsProperties gunsProperties;

    /**
     * 跳转到客户管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "customer.html";
    }

    /**
     * 跳转到添加客户管理
     */
    @RequestMapping("/customer_add")
    public String customerAdd() {
        return PREFIX + "customer_add.html";
    }

    /**
     * 跳转到修改客户管理
     */
    @RequestMapping("/customer_update/{customerId}")
    public String customerUpdate(@PathVariable Integer customerId, Model model) {
        Customer customer = customerService.selectById(customerId);
        model.addAttribute("item",customer);
        LogObjectHolder.me().set(customer);
        return PREFIX + "customer_edit.html";
    }

    /**
     * 获取客户管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return customerService.selectList(null);
    }

    /**
     * 新增客户管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Customer customer) {
        return customer;
//        customerService.insert(customer);
//        return SUCCESS_TIP;
    }

    /**
     * 删除客户管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer customerId) {
        customerService.deleteById(customerId);
        return SUCCESS_TIP;
    }

    /**
     * 修改客户管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Customer customer) {
        customerService.updateById(customer);
        return SUCCESS_TIP;
    }

    /**
     * 客户管理详情
     */
    @RequestMapping(value = "/detail/{customerId}")
    @ResponseBody
    public Object detail(@PathVariable("customerId") Integer customerId) {
        return customerService.selectById(customerId);
    }

    /**
     * 跳转到批量上传
     */
    @RequestMapping("/custom_upload")
    public String customUpload() {
        return PREFIX + "custom_upload.html";
    }

    @RequestMapping(method = RequestMethod.POST, path = "/customer_import")
    @ResponseBody
    public Object customerImport(@RequestParam(required = true) String filelink) {
        try {
            ShiroUser shiroUser = ShiroKit.getUser();
            String file_path = gunsProperties.getFileUploadPath() + filelink;
            Collection<Map> list = customerService.parseExcel(file_path);

            List<Customer> new_list = new ArrayList();
            for (Map map : list) {
                Map new_map = new HashMap();
                for(Object map_key: map.keySet()){
                    String new_key = "";
                    Integer isMarried = 0;
                    Integer sex = 0;
                    switch (map_key.toString()) {
                        case "紧急联系人" :
                            new_key = "emergencyContactor";
                            break;
                        case "姓名" :
                            new_key = "name";
                            break;
                        case "婚姻状况" :
                            new_key = "isMarried";
                            if (map.get("婚姻状况").equals("已婚")) {
                                isMarried = 1;
                            }
                            break;
                        case "家庭住址" :
                            new_key = "address";
                            break;
                        case "电话" :
                            new_key = "phone";
                            break;
                        case "毕业院校" :
                            new_key = "schoolTag";
                            break;
                        case "紧急联系人电话" :
                            new_key = "emergencyContactorPhone";
                            break;
                        case "学历" :
                            new_key = "educationBackground";
                            break;
                        case "年龄" :
                            new_key = "age";
                            break;
                        case "身份证号" :
                            new_key = "idCard";
                            break;
                        case "性别" :
                            new_key = "sex";
                            if (map.get("性别").equals("男")) {
                                sex = 1;
                            } else if (map.get("性别").equals("女")) {
                                sex = 2;
                            }
                            break;
                    }
//                    map.remove(map_key);
                    if (new_key.equals("isMarried")) {
                        new_map.put(new_key, isMarried);
                    } else if (new_key.equals("sex")) {
                        new_map.put(new_key, sex);
                    } else {
                        new_map.put(new_key, map.get(map_key));
                    }
                }
                Object customer = this.parseMap2Object(new_map, Customer.class);
                new_list.add((Customer)customer);
            }

//            customerService.insertBatch(new_list);
            customerService.insertBatchNew(new_list);
            return new JsonResult(200,"成功","成功");

//            return new_list;
        } catch (Exception e) {
            return new JsonResult(1,"32",e);
        }
//        return new JsonResult(100, "33", "rfew");
    }

    /**
     * 将Map转换为对象
     * @param paramMap
     * @param cls
     * @return
     */
    public static <T> T parseMap2Object(Map<String, Object> paramMap, Class<T> cls) {
        return JSONObject.parseObject(JSONObject.toJSONString(paramMap), cls);
    }

}
