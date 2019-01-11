function creataSingleDataSetModel(dsName, dataList , bizModel) {
  var ds = {
    'dataSetName': dsName,
    'data': dataList
  }
  var bm = {
    'modelName': bizModel.modelName,
    'modeTag': bizModel.modeTag,
    'bizData': {}
  }
  bm.bizData[dsName] = ds
  return bm
}
