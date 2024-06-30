package io.github.gaelrenoux.gaston.benchmarks

import fr.renoux.gaston.model.Score
import io.github.gaelrenoux.gaston.benchmarks.utils._
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
class ScoreSumBenchmark {

  import ScoreSumBenchmark._

  @Benchmark
  def viewMapSumScores(): Unit = repeat(1000) {
    scoreResult = persons.view.map(_.score).sum
  }

  @Benchmark
  def viewMapSumDoubles(): Unit = repeat(1000) {
    scoreResult = Score(persons.view.map(_.score.value).sum)
  }

  @Benchmark
  def foldScores(): Unit = repeat(1000) {
    scoreResult = persons.foldLeft(Score.Zero)(_ + _.score)
  }

  @Benchmark
  def foldDoubles(): Unit = repeat(1000) {
    scoreResult = Score(persons.foldLeft(0.0)(_ + _.score.value))
  }

  @Benchmark
  def scoreSum(): Unit = repeat(1000) {
    scoreResult = Score.sum(persons)(_.score)
  }

}

object ScoreSumBenchmark {
  case class Person(name: String, score: Score)

  val rand = new Random()
  val persons = Array.fill(100)(Person("John", Score(rand.nextInt(800).toDouble)))
  var scoreResult = Score.Zero

}
