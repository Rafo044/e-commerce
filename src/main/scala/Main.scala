import org.apache.spark.sql.SparkSession

object Main {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("MyApp")
      .master("local[*]")
      .getOrCreate()


    spark.sparkContext.setLogLevel("ERROR")
    val path = "src/main/resources/data/data.csv"
    if (path == null) {
      println("CSV tapılmadı! Yol səhvdir.")
      System.exit(1)
    }


    val df = Reader.loadEvents(spark, path)
    df.show()
  }
}
