package com.yukw

import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}

import scala.collection.mutable


object RecurseJsonArr {

  /**
    * 初始化JSONArr对象
    * @return
    */
  def initJsonArr: JSONArray = {
    val jsonArr = new JSONArray()
    for (i <- 0 to 5) {
      val initJsonObj = new JSONObject()
      initJsonObj.put("rowkey", "20200715")
      initJsonObj.put("id", i)
      jsonArr.add(initJsonObj)
    }
    jsonArr
  }

  /**
    * 添加单一参数
    * @param jsonArr
    * @param fieldName
    * @param value
    * @return
    */
  def addSingleValue(jsonArr: JSONArray, fieldName: String, value: Object): JSONArray = {
    for (x: JSONObject <- jsonArr) {
      x.put(fieldName, value)
    }
    jsonArr
  }

  /**
    * 添加list参数
    * @param jsonArr
    * @param inValue
    * @return
    */
  def addMultiValue(jsonArr: JSONArray, inValue: mutable.MultiMap[String, List[Object]]): JSONArray = {
    val outPutJsonArr = new JSONArray()
    inValue.foreach(line => {
      val partJsonArr = cloneJsonArr(jsonArr)
      addSingleValue(partJsonArr, line._1, line._2)
      outPutJsonArr.addAll(partJsonArr)
    })
    outPutJsonArr
  }

  /**
    * 克隆fastJSONArray对象
    *
    * @param jsonArr
    * @return
    */
  def cloneJsonArr(jsonArr: JSONArray): JSONArray = {
    val outPutJsonArr = new JSONArray();
    for (x <- jsonArr) {
      val jsonObj = JSON.parseObject(JSON.toJSONString(x))
      outPutJsonArr.add(jsonObj)
    }
    outPutJsonArr
  }


  def main(args: Array[String]): Unit = {
    val jsonArr = RecurseJsonArr.initJsonArr
    print(addSingleValue(jsonArr,"name","alex"))
  }

}
