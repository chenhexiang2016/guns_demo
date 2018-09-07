package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 客户表
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-06
 */
@TableName("customer")
public class Customer extends Model<Customer> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 客户姓名
     */
    private String name;
    /**
     * 客户电话
     */
    private String phone;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 性别：0 未知；1 男； 2女
     */
    private Integer sex;
    /**
     * 身份证号
     */
    @TableField("id_card")
    private String idCard;
    /**
     * 家庭住址
     */
    private String address;
    /**
     * 学历
     */
    @TableField("education_background")
    private String educationBackground;
    /**
     * 毕业院校
     */
    @TableField("school_tag")
    private String schoolTag;
    /**
     * 婚姻状况：0 未婚；1已婚
     */
    @TableField("is_married")
    private Integer isMarried;

    public String getEmergencyContactor() {
        return emergencyContactor;
    }

    public void setEmergencyContactor(String emergencyContactor) {
        this.emergencyContactor = emergencyContactor;
    }

    /**
     * 紧急联系人
     */
    @TableField("emergency_contactor")
    private String emergencyContactor;
    /**
     * 紧急联系人电话
     */
    @TableField("emergency_contactor_phone")
    private String emergencyContactorPhone;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEducationBackground() {
        return educationBackground;
    }

    public void setEducationBackground(String educationBackground) {
        this.educationBackground = educationBackground;
    }

    public String getSchoolTag() {
        return schoolTag;
    }

    public void setSchoolTag(String schoolTag) {
        this.schoolTag = schoolTag;
    }

    public Integer getIsMarried() {
        return isMarried;
    }

    public void setIsMarried(Integer isMarried) {
        this.isMarried = isMarried;
    }

    public String getEmergencyContactorPhone() {
        return emergencyContactorPhone;
    }

    public void setEmergencyContactorPhone(String emergencyContactorPhone) {
        this.emergencyContactorPhone = emergencyContactorPhone;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Customer{" +
        "id=" + id +
        ", name=" + name +
        ", phone=" + phone +
        ", age=" + age +
        ", sex=" + sex +
        ", idCard=" + idCard +
        ", address=" + address +
        ", educationBackground=" + educationBackground +
        ", schoolTag=" + schoolTag +
        ", isMarried=" + isMarried +
        ", emergencyContactor=" + emergencyContactor +
        ", emergencyContactorPhone=" + emergencyContactorPhone +
        "}";
    }
}
