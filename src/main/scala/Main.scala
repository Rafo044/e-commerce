import org.apache.spark.sql.SparkSession

object Main {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("MyApp")
      .master("local[*]")
      .getOrCreate()


    spark.sparkContext.setLogLevel("ERROR")
    val path = "src/main/resources/data/data.csv"

    val df = Reader.loadEvents(spark, path)

    // Filter ,Cleasing
    df.filter(row => row.Quantity > 0 and row.UnitPrice > 0)
    val cleaned = df.na.drop("CustomerID")
  }
}
