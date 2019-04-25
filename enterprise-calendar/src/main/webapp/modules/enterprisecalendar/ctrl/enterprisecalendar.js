layui.config({
  base: "apprFlow/modules/util/"//自定义加载的模块
}).use(['form', 'layer', 'jquery', 'table', 'laydate', 'util'], function () {
  var form = layui.form,
    layer = parent.layer === undefined ? layui.layer : parent.layer,
    $ = layui.jquery;
  var table = layui.table;
  var laydate = layui.laydate;
  var util = layui.util;
  var preDate = util.toDateString(new Date(), "yyyy-MM");//记录上次点击日期，用于拦截日期点击事件

  //日历基本设置
  laydate.set({
    showBottom: false
    , position: "static"
    , change: changeDate
    , done: doneDay
    , theme: 'grid'
  });

  laydate.render({
    elem: "#test"
  });

  $(document).ready(function () {
    findMarkDay();
  })

  /**
   * 切换日期时触发，更新当前月的日期标记
   * @param value 得到日期生成的值，如：2017-08-18
   * @param date  得到日期时间对象：{year: 2017, month: 8, date: 18, hours: 0, minutes: 0, seconds: 0}
   */
  function changeDate(value, date) {
    //过滤点击日期事件
    if (value.indexOf(preDate) == -1) {
      //更新日期字符串
      preDate = util.toDateString(new Date(value), "yyyy-MM");
      //获取当前选择月份的起始日期
      var startDate = preDate + "-01";
      var endDate = preDate + "-" + laydate.getEndDate(date.month, date.year);
      //获取特殊日期集合
      findMarkDay(startDate, endDate, value);
    }
  }

  /**
   * 点击日期时触发
   * @param value 得到日期生成的值，如：2017-08-18
   * @param date  得到日期时间对象：{year: 2017, month: 8, date: 18, hours: 0, minutes: 0, seconds: 0}
   */
  function doneDay(value, date) {
    //打开日期标记页
    // openDiv("500px", "400px", basepath + "/modules/enterprisecalendar/enterprisecalendar-edit.jsp?checkDate=" + value, "日期标记");
    layer.open({
      type: 2,
      title: "日期标记",
      skin: 'layui-layer-molv',
      shadeClose: true,
      offset: '20px',
      shade: false,
      anim: 3, //动画类型
      maxmin: true, //开启最大化最小化按钮
      area: ["500px", "400px"],
      content: basepath + "/workflow/enterpriseCalendar/getCurrData?curDate=" + value
    });
  }

  /**
   * 获取范围内标记日期集合
   * @param curDate   当前时间，用于日历初始化赋值
   */
  function findMarkDay(curDate) {
    $.ajax({
      url: basepath + "/workflow/enterpriseCalendar/findMarkDay",
      type: "GET",
      data: {
        "curDate": curDate
      },
      success: function (result) {
        //每次渲染之前先删除无效日历控件
        $("#enterpriseCalendar").children("div").filter(".layui-laydate-static").remove();
        laydate.render({
          elem: "#enterpriseCalendar"
          , mark: result.data
          , value: curDate
        });
      }
    });
  }

  /**
   * 初始化企业日历
   * @param value 日历初始值
   */
  window.initCalendar = function initCalendar(value) {
    findMarkDay(value);
  }
})
