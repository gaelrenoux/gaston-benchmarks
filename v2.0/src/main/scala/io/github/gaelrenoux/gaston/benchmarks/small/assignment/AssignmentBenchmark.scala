package io.github.gaelrenoux.gaston.benchmarks.small.assignment

import fr.renoux.gaston.engine2.AssignmentImprover
import fr.renoux.gaston.input.{InputLoader, InputModel, InputTranscription2, problemFromClassPath}
import fr.renoux.gaston.{engine as oldEngine, model as oldModel}
import fr.renoux.gaston.model2.*
import fr.renoux.gaston.util.Context
import org.openjdk.jmh.annotations.*

import java.util.concurrent.TimeUnit
import scala.util.Random

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 10)
@Measurement(iterations = 20)
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
class AssignmentBenchmark {

  import AssignmentBenchmark.*

  @Benchmark
  @BenchmarkMode(Array(Mode.SingleShotTime))
  @Warmup(iterations = 0)
  @Measurement(iterations = 1)
  def compareBothUdocon(myState: Udocon219): Unit = {
    given Random = new Random(0)

    val newSchedule = myState.newSchedules.head
    val oldSchedule = myState.oldSchedules.head

    myState.newImprover.improve(newSchedule)
    val improvedOldS = myState.oldImprover.improve(oldSchedule)
    val newScore = newSchedule.getTotalScore()
    val oldScore = improvedOldS.score

    if (newScore != oldScore) {
      println(s"Improved scores don't match! Old $oldScore vs New $newScore")
      println("=============================================================================")
      println(improvedOldS.toFormattedString)
      println("=============================================================================")

      given SchedulePrinter = new SchedulePrinter(myState.newProblem)

      println(newSchedule.toPrettyString)
      println("=============================================================================")
      throw new IllegalStateException
    } else {
      println("Improved scores match")
    }
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.SingleShotTime))
  @Warmup(iterations = 0)
  @Measurement(iterations = 1)
  def compareBothR3(myState: R32024): Unit = {

    val newSchedule = myState.newSchedules.head
    val oldSchedule = myState.oldSchedules.head

    myState.newImprover.improve(newSchedule)(using new Random(42))
    val improvedOldS = myState.oldImprover.improve(oldSchedule)(using new Random(42))
    val newScore = newSchedule.getTotalScore()
    val oldScore = improvedOldS.score

    // TODO: Cannot work, because some stuff isn't taken into account in the new engine (like long-duration topics)
    if (newScore != oldScore) {
      println(s"Improved scores don't match! Old $oldScore vs New $newScore")
      println("=============================================================================")
      println(improvedOldS.toFormattedString)
      println("=============================================================================")
      given SchedulePrinter = new SchedulePrinter(myState.newProblem)
      println(newSchedule.toPrettyString)
      println("=============================================================================")
      throw new IllegalStateException
    } else {
      println("Improved scores match")
    }
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.SingleShotTime))
  def improveOldScheduleUdocon(myState: Udocon219): Unit = {
    given Random = new Random(0)

    myState.oldSchedules.foreach { s =>
      myState.oldImprover.improve(s)
    }
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.SingleShotTime))
  def improveOldScheduleR3(myState: R32024): Unit = {
    given Random = new Random(0)

    myState.oldSchedules.foreach { s =>
      myState.oldImprover.improve(s)
    }
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.SingleShotTime))
  def improveNewScheduleUdocon(myState: Udocon219): Unit = {
    given Random = new Random(0)

    myState.newSchedules.foreach { s =>
      myState.newImprover.improve(s)
    }
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.SingleShotTime))
  def improveNewScheduleR3(myState: R32024): Unit = {
    given Random = new Random(0)

    myState.newSchedules.foreach { s =>
      myState.newImprover.improve(s)
    }
  }
}

object AssignmentBenchmark {

  @State(Scope.Benchmark)
  class Udocon219 {
    val Size = 100

    given Context = Context.Default

    private val input: InputModel = InputLoader.fromClassPath("problems/udocon-2019.conf").toOption.get

    given oldProblem: oldModel.Problem = problemFromClassPath("problems/udocon-2019.conf").toOption.get

    val newProblem = InputTranscription2(input).result.toEither.toOption.get

    var oldSchedules: Array[oldModel.Schedule] = null
    var newSchedules: Array[Schedule] = null

    val oldImprover = new oldEngine.assignment.AssignmentImprover
    val newImprover = new AssignmentImprover(newProblem)

    @Setup(Level.Iteration)
    def setUp(): Unit = {
      val Seq(d1a, d1b, d2a, d2b, d3a) = oldProblem.slotsList
      val Seq(agon, gayOrcs, beton, donjon, selpoivreTolkraft, shadowrun, endOfLine, etoiles, facettes, inflorenza, itras, ixalan, legacy, rivieres, schtroumpfs, monastere, meute, monster, edge, dekaranger, pirateZombi, ribbon, soth, starWars, sprawl, flood, bonneville, epoque, vampire, vastemonde) =
        oldProblem.topicsList
      val Seq(selpoivre, agone, aude, bakemono, bashar, boojum, chestel, cryoban, emojk, eugenie, gabzeta, herlkin, highlandjul, isidore, jdc, jorune, julian, kandjar, leonard, mangon, najael, orfeo, paiji, paradoks, rolapin, saladdin, sammael, tolkraft, udo, virgile, zeben, killerklown) =
        oldProblem.personsList

      oldSchedules = (0 until Size).toArray.map { _ =>
        oldModel.Schedule.from(
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

  @State(Scope.Benchmark)
  class R32024 {
    val Size = 100

    given Context = Context.Default

    private val input: InputModel = InputLoader.fromClassPath("problems/r3-2024.conf").toOption.get

    given oldProblem: oldModel.Problem = problemFromClassPath("problems/r3-2024.conf").toOption.get

    val newProblem = InputTranscription2(input).result.toEither.toOption.get

    var oldSchedules: Array[oldModel.Schedule] = null
    var newSchedules: Array[Schedule] = null

    val oldImprover = new oldEngine.assignment.AssignmentImprover
    val newImprover = new AssignmentImprover(newProblem)

    @Setup(Level.Iteration)
    def setUp(): Unit = {
      val Seq(d1a, d1b, d2a, d2b, d3a, d3b) = oldProblem.slotsList
      val Seq(
      apocalypse, avatar, orcs, bliss1, bliss2, bluebeard, vampire, cyberpunk, bile, chatons, dune, exploirateurs, cthulhu, genese1, genese2, couvee, librete, ventre, london, microscope, minuit, pasion, serpent, shades, soth, synthetiques, wildsea,
      unassignedD1a, unassignedD1b, unassignedD2a, unassignedD2b, unassignedD3a, unassignedD3b
      ) = oldProblem.topicsList
      val Seq(adrien, bpm, cactus, chloe, elmi, fanny, gawel, laetitia, lea, maxime, natacha, noemie, olivier, pacman, rafik, tanguy, tilleul, tiramisu, ulysse, vincent, viviane, vivien) =
        oldProblem.personsList

      oldSchedules = (0 until Size).toArray.map { _ =>
        oldModel.Schedule.from(
          d1a(
            unassignedD1a(olivier, lea, tanguy, bpm, cactus),
            bliss1(adrien, fanny, chloe, tiramisu),
            exploirateurs(vivien, vincent, maxime, rafik),
            vampire(ulysse, noemie, elmi),
            microscope(gawel, pacman, natacha, tilleul)
          ),
          d1b(
            unassignedD1b(olivier, maxime, noemie, lea, bpm),
            bliss2(adrien, fanny, chloe, tiramisu),
            chatons(tilleul, elmi, laetitia, vincent, rafik),
            couvee(cactus, gawel, tanguy),
            synthetiques(pacman, natacha, ulysse, vivien)
          ),
          d2a(
            unassignedD2a(fanny, rafik),
            avatar(natacha, gawel, cactus, adrien),
            dune(pacman, bpm, vincent, ulysse),
            pasion(vivien, maxime, viviane, lea),
            shades(olivier, laetitia, tanguy),
            wildsea(tilleul, chloe, tiramisu, noemie, elmi)
          ),
          d2b(
            unassignedD2b(viviane, pacman, tiramisu, ulysse, tanguy, maxime),
            bluebeard(tilleul, vivien, vincent, natacha, noemie),
            bile(cactus, elmi, lea, laetitia),
            london(olivier, bpm),
            minuit(adrien, rafik, chloe, fanny, gawel)
          ),
          d3a(
            unassignedD3a(viviane, cactus, tilleul),
            orcs(olivier, vincent, chloe, adrien),
            cthulhu(gawel, bpm, tiramisu, laetitia),
            librete(vivien, ulysse, elmi, tanguy),
            serpent(maxime, natacha, pacman, lea, rafik, fanny, noemie)
          ),
          d3b(
            unassignedD3b(rafik, viviane, adrien, maxime),
            apocalypse(cactus, tanguy, vincent, chloe, olivier),
            cyberpunk(elmi, lea, bpm, noemie),
            ventre(natacha, pacman, laetitia, vivien, tilleul),
            soth(gawel, tiramisu, ulysse, fanny)
          )
        )
      }
      newSchedules = oldSchedules.map { s =>
        ScheduleMaker.fromOldSchedule(s, newProblem)
      }
    }
  }
}
