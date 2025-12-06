package readfile

import java.sql.Timestamp
import org.apache.spark.sql.{SparkSession, DataFrame, Encoders}

object Reader {


  def loadEvents(spark: SparkSession, path: String): DataFrame = {

    val df: DataFrame = spark.read
      .option("header", "true")
      .option("timestampFormat", "M/d/yyyy H:mm")
      .option("inferSchema", "true")
      .csv(path)

      df
  }
}
