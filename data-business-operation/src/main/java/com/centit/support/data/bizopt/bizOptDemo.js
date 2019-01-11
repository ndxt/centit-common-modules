function doBiz01(bizModel) {
  var item = {};
  item.cust = bizModel.bizData.ds1.data.length
  item.city = bizModel.bizData.ds2.data.length

  return {
    'modelName': 'biz01',
    'modeTag': bizModel.modeTag,
    'bizData': {
      'ret': {
        'dataSetName': 'ds01',
        'data': [item, item]
      }
    }
  }
}
