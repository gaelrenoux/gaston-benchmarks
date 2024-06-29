package io.github.gaelrenoux.gaston.benchmarks

import cats.Monoid
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
class MapMergeBenchmark {

  import MapMergeBenchmark._

  assert(withMonoid() == manuallyWithMergedKeys())

  @Benchmark
  def withMonoid(): Map[Person, Score] = repeat(100) {
    result = Monoid[Map[Person, Score]].combine(scoresByPerson1, scoresByPerson2)
    result
  }

  @Benchmark
  def manuallySameAsMonoid(): Map[Person, Score] = repeat(100) {
    if (scoresByPerson1.size <= scoresByPerson2.size)
      result = scoresByPerson1.foldLeft(scoresByPerson2) { case (newMap, (person, score1)) =>
        val score2 = scoresByPerson2.getOrElse(person, Score.Zero)
        newMap.updated(person, score1 + score2)
      }
    else
      result = scoresByPerson2.foldLeft(scoresByPerson1) { case (newMap, (person, score2)) =>
        val score1 = scoresByPerson1.getOrElse(person, Score.Zero)
        newMap.updated(person, score1 + score2)
      }
    result
  }

  @Benchmark
  def manuallyWithMergedKeys(): Map[Person, Score] = repeat(100) {
    val keys = scoresByPerson1.keySet ++ scoresByPerson2.keySet
    result = keys.view.map { key =>
      val one = scoresByPerson1.getOrElse(key, Score.Zero)
      val two = scoresByPerson2.getOrElse(key, Score.Zero)
      key -> (one + two)
    }.toMap
    result
  }


}

object MapMergeBenchmark {
  case class Person(name: String, age: Int)

  val rand = new Random()
  val personsArray: Array[Person] = Array.tabulate(30)(i => Person(s"John $i", i))

  val scoresByPerson1: Map[Person, Score] = personsArray.filter(_.age < 5).map(_ -> Score(rand.nextLong(800).toDouble)).toMap
  val scoresByPerson2: Map[Person, Score] = personsArray.filter(_.age > 24).map(_ -> Score(rand.nextLong(800).toDouble)).toMap
  var result: Map[Person, Score] = Map.empty

  var intResult: Int = 0

}
