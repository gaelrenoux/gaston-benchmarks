package io.github.gaelrenoux.gaston.benchmarks.harness

import com.google.common.math.Stats

import scala.concurrent.duration._
import scala.util.Try

/** Here we're not measuring the time spent to do an action: we're alloting some time, and checking what score we can reach. */
// scalastyle:off magic.number
abstract class ScoreCompetition[Problem] extends App {

  val availableHours: Int = Try(args.head.toInt).getOrElse(throw new IllegalArgumentException("First argument must be a number of hours"))

  /* Input is the number of hours available. We have two benchmarks to run (Udocon and R3) so half the time for each. Less than 2h isn't possible. */
  val availableMinutesPerBenchmark = 30 * math.max(availableHours, 2)

  /* Trying to balance the number of runs and the duration of them */
  val (engineRunsPerBenchmark, engineRunsDuration) = {
    if (availableMinutesPerBenchmark <= 120) (availableMinutesPerBenchmark / 6, 6.minutes)
    else if (availableMinutesPerBenchmark <= 400) (20, (availableMinutesPerBenchmark / 20).minutes)
    else (availableMinutesPerBenchmark / 20, 20.minutes)
  }
  println(s"Each problem will get $engineRunsPerBenchmark runs, ${engineRunsDuration.toMinutes} minutes each")

  /** Returns the score, the count of iterations, and the number of ms actually spent */
  protected def runOnce(problem: Problem, duration: FiniteDuration, parallelism: Int): (Double, Long, Long)

  protected def runBenchmark(problem: Problem, name: String): Unit = {
    println(s"Running $name (duration: $engineRunsDuration)")
    val scores = new Array[Double](engineRunsPerBenchmark)
    val counts = new Array[Long](engineRunsPerBenchmark)
    val durationsMs = new Array[Long](engineRunsPerBenchmark)
    var i = 0 // scalastyle:ignore var.local
    while (i < engineRunsPerBenchmark) {
      val (score, count, durationMs) = runOnce(problem, engineRunsDuration, 6)
      println(s"$name / Run $i: score $score after $count iterations ($durationMs milliseconds)")
      scores(i) = score
      counts(i) = count
      durationsMs(i) = durationMs
      i += 1
    }

    val scoreStats = Stats.of(scores: _*)
    val countStats = Stats.of(counts: _*)
    val durationStats = Stats.of(durationsMs: _*)

    println(
      s"""
         |===== Run results =====
         |Problem: $name
         |Run planned duration: $engineRunsDuration
         |  Score mean: ${scoreStats.mean()}
         |    variance: ${scoreStats.sampleStandardDeviation()}
         |    min: ${scoreStats.min()}
         |    max: ${scoreStats.max()}
         |  Count mean: ${countStats.mean()}
         |    variance: ${countStats.sampleStandardDeviation()}
         |    min: ${countStats.min()}
         |    max: ${countStats.max()}
         |  Durations: ${durationStats.mean()}
         |    min: ${durationStats.min()}
         |    max: ${durationStats.max()}
         |""".stripMargin)
  }
}

// scalastyle:on magic.number
