layui.config({
  base: "apprFlow/modules/util/"//自定义加载的模块
}).use(['form', 'layer', 'jquery', 'table', 'laydate', 'util'], function () {
  var form = layui.form,
    layer = parent.layer === undefined ? layui.layer : parent.layer,
    $ = layui.jquery;
  var table = layui.table;
  var laydate = layui.laydate;
  var util = layui.util;
  var preDate = util.toDateString(new Date(), "yyyy-MM");//记录上次点击日期，拦截日期点击事件

  form.on("submit(submitBtn)", function (data) {
    $.ajax({
      url: basepath + "/workflow/enterpriseCalendar/saveData",
      type: "POST",
      data: data.field,
      success: function (result) {
        if (result.code == 0) {
          layer.msg("操作成功", {anim: 0, time: 1000}, function () {
            var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
            parent.layer.close(index); //再执行关闭
            parent.initCalendar(data.field.workDay);//刷新企业日历
          })
        } else {
          layer.msg("操作失败", {icon: 0})
        }
      }
    });
  })

  //关闭页面
  window.CloseWin = function CloseWin() {
    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
    parent.layer.close(index); //再执行关闭
  }

})
