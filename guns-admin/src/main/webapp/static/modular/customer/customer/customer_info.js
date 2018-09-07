/**
 * 初始化客户管理详情对话框
 */
var CustomerInfoDlg = {
    customerInfoData : {}
};

/**
 * 清除数据
 */
CustomerInfoDlg.clearData = function() {
    this.customerInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CustomerInfoDlg.set = function(key, val) {
    this.customerInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CustomerInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
CustomerInfoDlg.close = function() {
    parent.layer.close(window.parent.Customer.layerIndex);
}

/**
 * 收集数据
 */
CustomerInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name')
    .set('phone')
    .set('age')
    .set('sex')
    .set('idCard')
    .set('address')
    .set('educationBackground')
    .set('schoolTag')
    .set('isMarried')
    .set('emergencyContactor')
    .set('emergencyContactorPhone');
}

/**
 * 提交添加
 */
CustomerInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/customer/add", function(data){
        Feng.success("添加成功!");
        window.parent.Customer.table.refresh();
        CustomerInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.customerInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
CustomerInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/customer/update", function(data){
        Feng.success("修改成功!");
        window.parent.Customer.table.refresh();
        CustomerInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.customerInfoData);
    ajax.start();
}

$(function() {
    layui.use('upload, element', function(){
        var upload = layui.upload;
        var layer = layui.layer;    // 弹层

        //执行实例
        var uploadInst = upload.render({
            elem: '#customUpload' //绑定元素
            ,url: Feng.ctxPath + '/custom/upload' //上传接口
            ,accept: 'file' //允许上传的文件类型
            ,size:10 * 1024 *1024
            ,exts:'xls|xlsx'
            ,xhr:xhrOnProgress
            ,progress:function(value){//上传进度回调 value进度值
                element.progress('demo', value+'%')//设置页面进度条
            }
            ,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
                layer.load(); //上传loading
            }
            ,done: function(res){
                if (res.code == 200) {
                    layer.msg(res.message,function () {
                        CustomInfoDlg.close();
                        // window.parent.Custom.table.refresh();
                        window.parent.Custom.table.reload('CustomTable');
                    });
                } else {
                    layer.msg(res.message,function () {
                        layer.closeAll("loading");
                    });
                }
                //上传完毕回调
            }
            ,error: function(){
                layer.msg("导入异常",function () {
                    layer.closeAll("loading");
                });
            }
        });
    });
});

