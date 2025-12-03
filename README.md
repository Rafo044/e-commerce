# E-commerce Data Analysis Project

## Introduction

This project helps us understand customer shopping habits by looking at e-commerce data. We use special tools to clean the data, find important information, and then save it for later use. This helps businesses make better decisions.

## Tools We Use

Here are the main tools for this project:

*   **Scala** <img src="https://www.scala-lang.org/resources/img/old/logo.png" alt="Scala Logo" width="20" height="20"/>: This is the programming language we write our code in. It's good for big data tasks.
*   **Apache Spark** <img src="https://spark.apache.org/docs/latest/img/spark-logo-landing.png" alt="Apache Spark Logo" width="20" height="20"/>: This is a very powerful tool that helps us work with large amounts of data quickly.
*   **sbt (Scala Build Tool)** <img src="https://www.scala-sbt.org/assets/img/sbt-logo.svg" alt="sbt Logo" width="20" height="20"/>: This tool helps us build our Scala project and manage its parts.

## How the Project Works (Step-by-Step)

This project takes raw e-commerce data and makes it useful. Here's what happens:

1.  **Read the Data**:
    First, the program reads the raw shopping data. This data is usually in a CSV file (like a spreadsheet).

    ```e-commerce/src/main/scala/reader.scala#L8-18
    def loadEvents(spark: SparkSession, path: String): DataFrame = {

      val df: DataFrame = spark.read
        .option("header", "true")
        .option("timestampFormat", "M/d/yyyy H:mm")
        .option("inferSchema", "true")
        .csv(path)

        df
    }
    ```

2.  **Clean the Data**:
    We clean the data to make sure it's correct. We remove bad records, like:
    *   Items with zero or negative quantity (you can't buy -1 items).
    *   Items with zero or negative price.
    *   Records where we don't know who the customer is (missing Customer ID).

    ```e-commerce/src/main/scala/Main.scala#L18-22
        // Filter ,Cleasing
        val filtered_df = df.filter(col("Quantity") > 0 && col("UnitPrice") > 0)
        // CustomerID not null
        val cleasing_df = filtered_df.filter(col("CustomerID").isNotNull)
    ```

3.  **Add New Information**:
    We create a new column called "SalesAmount". This tells us the total money spent for each item by multiplying the `Quantity` (how many) by the `UnitPrice` (price per item).

    ```e-commerce/src/main/scala/Main.scala#L23
        // Feature engineering : Add SalesAmount columns
        val feature_df = cleasing_df.withColumn("SalesAmount", col("Quantity") * col("UnitPrice"))
    ```

4.  **Calculate Customer Totals**:
    Now, we group all the purchases by each customer. For every customer, we find:
    *   `TotalSpent`: How much money they spent in total. We round this to two decimal places.
    *   `OrderCount`: How many orders they placed.

    ```e-commerce/src/main/scala/Main.scala#L25-28
        // Transformations : Aggregations TotalSpent , OrderCount
        val transformed_df = feature_df.groupBy("CustomerID")
          .agg(sum("SalesAmount").alias("TotalSpent"), count("InvoiceNo").alias("OrderCount"))
        val sorted_df = transformed_df.withColumn("TotalSpent", round(col("TotalSpent"), 2))
        .orderBy(col("TotalSpent").desc)
    ```

5.  **Save the Results**:
    Finally, we save the clean and summarized data into a special file type called Parquet. This type of file is good for storing large data and is easy for other programs to read quickly.

    ```e-commerce/src/main/scala/Main.scala#L29
        // Save to parquet
        sorted_df.write.parquet(path_parquet)
    ```

## How to Use This Project

This is a standard sbt project. You can:
*   Compile the code: `sbt compile`
*   Run the project: `sbt run`
*   Start a Scala 3 console: `sbt console`
