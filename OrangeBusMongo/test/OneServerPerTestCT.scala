import org.scalatest.{Suite, TestData}
import org.scalatestplus.play.OneServerPerTest
import play.api.Application

/**
  *
  */
trait OneServerPerTestCT extends OneServerPerTest with TestAppComponents{
  this: Suite =>

   override def  newAppForTest(testData: TestData): Application =
    (new AppLoader).load(context)
}
