package quality

import com.amazon.deequ.analyzers.runners.{AnalysisRunner, AnalyzerContext}
import com.amazon.deequ.analyzers.runners.AnalyzerContext.successMetricsAsDataFrame
import com.amazon.deequ.analyzers.{Compliance, Correlation, Size, Completeness, Mean, ApproxCountDistinct}
import com.amazon.deequ.{VerificationSuite, VerificationResult}
import com.amazon.deequ.VerificationResult.checkResultsAsDataFrame
import com.amazon.deequ.checks.{Check, CheckLevel}
import org.apache.spark.sql.DataFrame

object Quality {
  def analysis(df: DataFrame): DataFrame = {
    val analysisContext: AnalyzerContext = AnalysisRunner.onData(df)
      .addAnalyzer(Size())
      .addAnalyzer(Completeness("id"))
      .addAnalyzer(Mean("price"))
      .addAnalyzer(ApproxCountDistinct("name"))
      .addAnalyzer(Correlation("price", "quantity"))
      .addAnalyzer(Compliance("price", "price > 0"))
      .run()
    val metricsDf = AnalyzerContext.successMetricsAsDataFrame(analysisContext, spark)
    metricsDf.show()
  }

  def verification(df: DataFrame): DataFrame = {
    val verificationResult: VerificationResult = VerificationSuite()
      .onData(df)
      .addCheck(Check(CheckLevel.Error, "Price should be positive")
        .isNonNegative("price"))
      .run()
    val checkResultsDf = VerificationResult.checkResultsAsDataFrame(verificationResult, spark)
    checkResultsDf.show()
  }

}
