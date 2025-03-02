package io.github.gaelrenoux.gaston.benchmarks.small.score

import fr.renoux.gaston.input.{InputLoader, InputModel, InputTranscription2, problemFromClassPath}
import fr.renoux.gaston.model as old
import fr.renoux.gaston.model2.*
import fr.renoux.gaston.util.Context
import org.openjdk.jmh.annotations.*

import java.util.concurrent.TimeUnit

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 50)
@Measurement(iterations = 100)
@Fork(value = 1, jvmArgs = Array(
  "-server",
  "-Xms1g",
  "-Xmx1g",
  "-XX:NewSize=512m",
  "-XX:MaxNewSize=512m",
  "-XX:InitialCodeCacheSize=256m",
  "-XX:ReservedCodeCacheSize=256m",
  "-XX:+UseParallelGC",
  "-XX:MaxInlineLevel=18",
  "-XX:+AlwaysPreTouch"
))
class ScoringBenchmark {

  import ScoringBenchmark.*

  @Benchmark
  @BenchmarkMode(Array(Mode.SingleShotTime))
  def scoreOldSchedule(myState: MyState): Unit = {
    myState.oldSchedules.foreach { s =>
      val _ = s.scoreCalculator.globalScore
    }
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.SingleShotTime))
  def scoreNewSchedule(myState: MyState): Unit = {
    myState.newSchedules.foreach { s =>
      s.getTotalScore()
    }
  }

// TODO Sub-scoring is not really possible anymore. Maybe later.
//
//  @Benchmark
//  @BenchmarkMode(Array(Mode.SingleShotTime))
//  def calculateBaseScores(myState: MyState): Unit = {
//    myState.newSchedules.foreach { s =>
//      s.calculateBaseScores(myState.newProblem)
//    }
//  }
//
//  @Benchmark
//  @BenchmarkMode(Array(Mode.SingleShotTime))
//  def calculateWishesTopicScores(myState: MyState): Unit = {
//    myState.newSchedules.foreach { s =>
//      s.calculateWishesTopicScores(myState.newProblem)
//    }
//  }
//
//  @Benchmark
//  @BenchmarkMode(Array(Mode.SingleShotTime))
//  def calculateWishesPersonScores(myState: MyState): Unit = {
//    myState.newSchedules.foreach { s =>
//      s.calculateWishesPersonScores(myState.newProblem)
//    }
//  }
//
//  @Benchmark
//  @BenchmarkMode(Array(Mode.SingleShotTime))
//  def calculateExclusiveScores(myState: MyState): Unit = {
//    myState.newSchedules.foreach { s =>
//      s.calculateExclusiveScores(myState.newProblem)
//    }
//  }
//
//  @Benchmark
//  @BenchmarkMode(Array(Mode.SingleShotTime))
//  def calculateLinkedScores(myState: MyState): Unit = {
//    myState.newSchedules.foreach { s =>
//      s.calculateLinkedScores(myState.newProblem)
//    }
//  }

}

object ScoringBenchmark {

  @State(Scope.Benchmark)
  class MyState {
    val Size = 1000

    private val udocon2019Input: InputModel = InputLoader.fromClassPath("problems/udocon-2019.conf").toOption.get

    given oldProblem: old.Problem = problemFromClassPath("problems/udocon-2019.conf").toOption.get

    val newProblem = InputTranscription2(udocon2019Input).result.toEither.toOption.get

    var oldSchedules: Array[old.Schedule] = null
    var newSchedules: Array[Schedule] = null

    @Setup(Level.Iteration)
    def setUp(): Unit = {
      given Context = Context.Default

      val Seq(d1a, d1b, d2a, d2b, d3a) = oldProblem.slotsList
      val Seq(agon, gayOrcs, beton, donjon, selpoivreTolkraft, shadowrun, endOfLine, etoiles, facettes, inflorenza, itras, ixalan, legacy, rivieres, schtroumpfs, monastere, meute, monster, edge, dekaranger, pirateZombi, ribbon, soth, starWars, sprawl, flood, bonneville, epoque, vampire, vastemonde) =
        oldProblem.topicsList
      val Seq(selpoivre, agone, aude, bakemono, bashar, boojum, chestel, cryoban, emojk, eugenie, gabzeta, herlkin, highlandjul, isidore, jdc, jorune, julian, kandjar, leonard, mangon, najael, orfeo, paiji, paradoks, rolapin, saladdin, sammael, tolkraft, udo, virgile, zeben, killerklown) =
        oldProblem.personsList

      oldSchedules = (0 until Size).toArray.map { _ =>
        old.Schedule.from(
          d1a(
            agon(highlandjul, agone, najael, herlkin, udo, kandjar),
            gayOrcs(paradoks, cryoban, jdc, gabzeta),
            itras(emojk, virgile, chestel, saladdin, bashar, leonard),
            schtroumpfs(julian, bakemono, tolkraft, paiji, mangon),
            pirateZombi(sammael, boojum, orfeo, jorune, isidore, aude),
            vampire(selpoivre, killerklown, rolapin, zeben, eugenie)
          ),
          d1b(
            beton(paradoks, jdc, jorune, highlandjul, julian),
            donjon(sammael, boojum, gabzeta, kandjar),
            shadowrun(rolapin, zeben, killerklown, agone, najael, mangon),
            inflorenza(eugenie, cryoban, orfeo, emojk, chestel),
            sprawl(virgile, bakemono, tolkraft, paiji, herlkin, leonard),
            flood(udo, bashar, selpoivre, isidore, saladdin, aude)
          ),
          d2a(
            endOfLine(julian, killerklown, boojum, isidore, eugenie),
            rivieres(sammael, herlkin, orfeo, jorune, highlandjul, saladdin),
            monster(bashar, paiji, najael, gabzeta, chestel, aude),
            dekaranger(kandjar, bakemono, jdc, mangon, selpoivre),
            ribbon(paradoks, virgile, emojk, leonard, rolapin),
            starWars(cryoban, udo, zeben, agone, tolkraft)
          ),
          d2b(
            legacy(cryoban, tolkraft, orfeo, paradoks, aude, eugenie),
            monastere(emojk, killerklown, bashar, mangon, rolapin),
            meute(jorune, jdc, leonard, highlandjul, isidore, julian),
            edge(saladdin, virgile, sammael, herlkin, gabzeta, selpoivre),
            soth(boojum, bakemono, chestel, udo),
            vastemonde(agone, paiji, najael, zeben, kandjar)
          ),
          d3a(
            selpoivreTolkraft(tolkraft, selpoivre, boojum, bashar, paradoks, julian),
            etoiles(isidore, najael, mangon, aude),
            facettes(leonard, cryoban, udo, highlandjul, eugenie),
            ixalan(killerklown, jorune, rolapin, zeben, saladdin, kandjar),
            bonneville(chestel, agone, jdc, paiji, emojk),
            epoque(sammael, virgile, bakemono, herlkin, gabzeta, orfeo)
          )
        )
      }
      newSchedules = oldSchedules.map { s =>
        ScheduleMaker.fromOldSchedule(s, newProblem)
      }
    }
  }
}
