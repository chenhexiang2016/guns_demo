/**
 * 客户管理管理初始化
 */
var Customer = {
    id: "CustomerTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Customer.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '客户姓名', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '客户电话', field: 'phone', visible: true, align: 'center', valign: 'middle'},
            {title: '年龄', field: 'age', visible: true, align: 'center', valign: 'middle'},
            {title: '性别：0 未知；1 男； 2女', field: 'sex', visible: true, align: 'center', valign: 'middle'},
            {title: '身份证号', field: 'idCard', visible: true, align: 'center', valign: 'middle'},
            {title: '家庭住址', field: 'address', visible: true, align: 'center', valign: 'middle'},
            {title: '学历', field: 'educationBackground', visible: true, align: 'center', valign: 'middle'},
            {title: '毕业院校', field: 'schoolTag', visible: true, align: 'center', valign: 'middle'},
            {title: '婚姻状况：0 未婚；1已婚', field: 'isMarried', visible: true, align: 'center', valign: 'middle'},
            {title: '紧急联系人', field: 'emergencyContactor', visible: true, align: 'center', valign: 'middle'},
            {title: '紧急联系人电话', field: 'emergencyContactorPhone', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Customer.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Customer.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加客户管理
 */
Customer.openAddCustomer = function () {
    var index = layer.open({
        type: 2,
        title: '添加客户管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/customer/customer_add'
    });
    this.layerIndex = index;
};

/**
 * 点击导入客户管理
 */
Customer.openImportCustomer = function () {
    var index = layer.open({
        type: 2,        //iframe层
        title: '客户导入',
        area: ['80%', '60%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/customer/custom_upload'
    });
    Customer.layerIndex = index;
    return false;
};

/**
 * 打开查看客户管理详情er
 */
Customer.openCustomerDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '客户管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/customer/customer_update/' + Customer.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除客户管理
 */
Customer.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/customer/delete", function (data) {
            Feng.success("删除成功!");
            Customer.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("customerId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询客户管理列表
 */
Customer.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Customer.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Customer.initColumn();
    var table = new BSTable(Customer.id, "/customer/list", defaultColunms);
    table.setPaginationType("client");
    Customer.table = table.init();
});
