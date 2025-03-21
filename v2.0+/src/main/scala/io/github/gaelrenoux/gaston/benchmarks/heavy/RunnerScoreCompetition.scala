package io.github.gaelrenoux.gaston.benchmarks.heavy

import fr.renoux.gaston.command.{Output, ParallelRunner}
import fr.renoux.gaston.engine.*
import fr.renoux.gaston.input.*
import fr.renoux.gaston.model.*
import fr.renoux.gaston.util.*
import fr.renoux.gaston.util.CanAddDuration.given
import io.github.gaelrenoux.gaston.benchmarks.harness

import java.time.Instant
import scala.concurrent.duration._
import scala.util.Random

/** Here we're not measuring the time spent to do an action: we're alloting some time, and checking how long the action takes. */
// scalastyle:off magic.number
object RunnerScoreCompetition extends harness.ScoreCompetition[Problem] {

  private val udocon2019: Problem = problemFromClassPath("problems/udocon-2019.conf").toOption.get
  private val r32024: Problem = problemFromClassPath("problems/r3-2024.conf").toOption.get
  private implicit val context: Context = Context.Default
  private implicit val random: Random = new Random

  @main
  def main(args: String*): Unit = run(args *)

  override def runOnce(problem: Problem, duration: FiniteDuration, parallelism: Int): (Double, Long, Long) = {
    implicit val p: Problem = problem
    implicit val engine: Engine = new GreedyEngine
    implicit val output: Output = Output.silent
    val seed = random.nextLong()
    val timeout = Instant.now() + duration
    println(s"Running with seed $seed. Timeout is $timeout.")

    val runner = new ParallelRunner(seed = seed, parallelism = parallelism)
    val termination: Termination = Termination(timeout = Some(timeout))
    val nanoTime = System.nanoTime()
    val (schedule, count) = runner.run(termination)
    val actualDurationMs = (System.nanoTime() - nanoTime) / 1000000
    (schedule.score.value, count, actualDurationMs)
  }

  override val benchmarksToRun: List[(String, Problem)] = List(
    "Udocon 2019" -> udocon2019,
    "R3 2024" -> r32024
  )
}

// scalastyle:on magic.number
