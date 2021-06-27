package io.github.gaelrenoux.gaston.benchmarks

import fr.renoux.gaston.command.Runner
import fr.renoux.gaston.engine._
import fr.renoux.gaston.input._
import fr.renoux.gaston.model.Problem
import fr.renoux.gaston.util.Context


// scalastyle:off magic.number

object MainHarness {

  implicit val udoConProblem: Problem = problemFromClassPath("udocon2017/uc17-completed.conf").toOption.get
  implicit val context: Context = Context.Default

  /** Runs the engine with some values */
  def run(iterations: Long = 1, seed: Long = 0L)(implicit improver: Improver = new GreedySlotImprover): Unit = {
    implicit val engine: Engine = new Engine(backtrackInitialSchedule = true)

    val runner = new Runner(parallelRunCount = 1)
    val params: OptimParams = OptimParams(maxIterations = Some(iterations))
    runner.run(seed = seed, params)

    ()
  }

}

// scalastyle:on magic.number
