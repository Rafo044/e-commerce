import java.sql.Timestamp
import org.apache.spark.sql.{SparkSession, Dataset, Encoders}

object Reader {

  case class Event(
    InvoiceNo: String,
    StockCode: String,
    Description: String,
    Quantity: Int,
    InvoiceDate: Timestamp,
    UnitPrice: Double,
    CustomerID: Option[String],
    Country: String
  )

  def loadEvents(spark: SparkSession, path: String): Dataset[Event]  = {

    implicit val eventEncoder = Encoders.product[Event]

    val df = spark.read
      .option("header", "true")
      .option("timestampFormat", "M/d/yyyy H:mm")
      .schema(Encoders.product[Event].schema)
      .csv(path)
      .as[Event]

      df
  }
}
