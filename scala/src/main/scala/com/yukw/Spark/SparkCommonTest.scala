package com.yukw.Spark

import org.apache.spark.sql.SparkSession


object SparkCommonTest {
  def main(args: Array[String]): Unit = {
    val ss = SparkSession.builder().master("local[1]").getOrCreate()
    ss.stop()
  }
}
