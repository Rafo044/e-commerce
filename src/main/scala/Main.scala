import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._


object Main {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("MyApp")
      .master("local[*]")
      .getOrCreate()


    spark.sparkContext.setLogLevel("ERROR")
    val path_csv = "src/main/resources/data/data.csv"
    val path_parquet = "src/main/resources/data/transformed_data.parquet"

    val df = Reader.loadEvents(spark, path_csv)

    // Filter ,Cleasing
    val filtered_df = df.filter(col("Quantity") > 0 && col("UnitPrice") > 0)
    // CustomerID not null
    val cleasing_df = filtered_df.filter(col("CustomerID").isNotNull)
    // Feature engineering : Add SalesAmount columns
    val feature_df = cleasing_df.withColumn("SalesAmount", col("Quantity") * col("UnitPrice"))
    // Transformations : Aggregations TotalSpent , OrderCount
    val transformed_df = feature_df.groupBy("CustomerID")
      .agg(sum("SalesAmount").alias("TotalSpent"), count("InvoiceNo").alias("OrderCount"))
    val sorted_df = transformed_df.withColumn("TotalSpent", round(col("TotalSpent"), 2))
    .orderBy(col("TotalSpent").desc)
    // Save to parquet
    sorted_df.write.parquet(path_parquet)
    sorted_df.show()
  }
}
