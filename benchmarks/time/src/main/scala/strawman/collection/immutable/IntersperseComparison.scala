package strawman.collection.immutable

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._
import org.openjdk.jmh.infra.Blackhole

import scala.{Any, AnyRef, Int, Long, Unit}
import scala.Predef.intWrapper


@BenchmarkMode(scala.Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
@Warmup(iterations = 12)
@Measurement(iterations = 12)
@State(Scope.Benchmark)
class IntersperseComparison {
  
  //@Param(scala.Array("0", "1", "2", "3", "4", "7", "8", "15", "16", "17", "39", "282", "73121", "7312102"))
  @Param(scala.Array("0", "1", "2", "4", "8", "16", "39", "282", "73121", "7312102"))
  var size: Int = _
  
  var xs: List[Long] = _
  
  @Setup(Level.Trial)
  def initData(): Unit = {
    def freshCollection() = List((1 to size).map(_.toLong): _*)
    xs = freshCollection()
  }
  
  @Benchmark
  def iteratorIntersperse(bh: Blackhole): Unit = bh.consume(xs.intersperse(-1, -2, -3))
  
  @Benchmark
  def viewIntersperse(bh: Blackhole): Unit = bh.consume(xs.viewIntersperse(-1, -2, -3))
}
