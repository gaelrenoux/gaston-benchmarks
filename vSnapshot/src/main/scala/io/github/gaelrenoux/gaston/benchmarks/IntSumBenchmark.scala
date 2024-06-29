package io.github.gaelrenoux.gaston.benchmarks

import org.openjdk.jmh.annotations._

import java.util.concurrent.TimeUnit
import scala.util.Random

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1, jvmArgs = Array(
  "-server",
  "-Xms1g",
  "-Xmx1g",
  "-XX:NewSize=512m",
  "-XX:MaxNewSize=512m",
  "-XX:InitialCodeCacheSize=256m",
  "-XX:ReservedCodeCacheSize=256m",
  "-XX:+UseParallelGC",
  "-XX:-UseAdaptiveSizePolicy",
  "-XX:MaxInlineLevel=18",
  "-XX:-UseBiasedLocking",
  "-XX:+AlwaysPreTouch",
  "-XX:+UseNUMA",
  "-XX:-UseAdaptiveNUMAChunkSizing"
))
class IntSumBenchmark {

  import IntSumBenchmark._

  @Benchmark
  def arrayViewMapSum(): Unit = repeat(1000) {
    intResult = personsArray.view.map(_.age).sum
  }

  @Benchmark
  def arrayFoldLeft(): Unit = repeat(1000) {
    intResult = personsArray.foldLeft(0)(_ + _.age)
  }

  @Benchmark
  def seqViewMapSum(): Unit = repeat(1000) {
    intResult = personsSeq.view.map(_.age).sum
  }

  @Benchmark
  def seqFoldLeft(): Unit = repeat(1000) {
    intResult = personsSeq.foldLeft(0)(_ + _.age)
  }

  @Benchmark
  def listViewMapSum(): Unit = repeat(1000) {
    intResult = personsList.view.map(_.age).sum
  }

  @Benchmark
  def listFoldLeft(): Unit = repeat(1000) {
    intResult = personsList.foldLeft(0)(_ + _.age)
  }

  @Benchmark
  def setViewMapSum(): Unit = repeat(1000) {
    intResult = personsSet.view.map(_.age).sum
  }

  @Benchmark
  def setFoldLeft(): Unit = repeat(1000) {
    intResult = personsSet.foldLeft(0)(_ + _.age)
  }

}

object IntSumBenchmark {
  case class Person(name: String, age: Int)

  val rand = new Random()
  val personsArray: Array[Person] = Array.tabulate(100)(i => Person(s"John $i", rand.nextInt(80)))
  val personsSeq: Seq[Person] = personsArray.toIndexedSeq
  val personsList: List[Person] = personsArray.toList
  val personsSet: Set[Person] = personsArray.toSet
  var intResult: Int = 0

  def repeat(n: Int)(f: => Unit) = {
    var i = n
    while (i > 0) {
      f
      i -= 1
    }
  }
}
