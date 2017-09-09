package strawman.collection

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._
import org.openjdk.jmh.infra.Blackhole
import strawman.collection.immutable.Range
import scala.math.BigInt

@BenchmarkMode(scala.Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
@Warmup(iterations = 12)
@Measurement(iterations = 12)
@State(Scope.Benchmark)
class IteratorBenchmark {
  
  @Param(scala.Array("0", "1", "5", "10", "25", "50", "75", "100"))
  var size: Int = _
  
  val maxSize: Int = 1 << 20
  val xs: strawman.collection.Iterable[BigInt] = Range(0, maxSize).map(BigInt(_)).toIterable
  
  var cutoff: BigInt = _
  var correctAnswer: BigInt = _
  
  def foldFunction(c: BigInt)(x: BigInt, y: BigInt): Option[BigInt] = if (y > c) None else Some(x + y)
  def foldFunctionNaive(c: BigInt)(x: BigInt, y: BigInt): BigInt = if (y > c) x else x + y
  
  @Setup(Level.Trial)
  def initData(): Unit = {
    cutoff = BigInt(maxSize / 100 * size)
    correctAnswer = cutoff * (cutoff + 1) / 2
  }
  
  @Benchmark
  def foldLeftWhile1(bh: Blackhole): Unit = {
    val answer: BigInt = xs.iterator().foldLeftWhile1(BigInt(0))(foldFunction(cutoff))
    assert(correctAnswer == answer)
    bh.consume(answer)
  }
  
  @Benchmark
  def foldLeftWhile2(bh: Blackhole): Unit = {
    val answer = xs.iterator().foldLeftWhile2(BigInt(0))(foldFunction(cutoff))
    assert(correctAnswer == answer)
    bh.consume(answer)
  }
  
  @Benchmark
  def foldLeftNaive(bh: Blackhole): Unit = {
    val answer = xs.iterator().foldLeft(BigInt(0))(foldFunctionNaive(cutoff))
    assert(correctAnswer == answer)
    bh.consume(answer)
  }
}
