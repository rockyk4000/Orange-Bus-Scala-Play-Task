import org.scalatest.{Suite, TestData}
import org.scalatestplus.play.OneAppPerTest
import play.api.Application

/**
  *
  */
trait OneAppPerTestCT extends OneAppPerTest with TestAppComponents{
  this: Suite =>

   override def  newAppForTest(testData: TestData): Application =
    (new AppLoader).load(context)
}
