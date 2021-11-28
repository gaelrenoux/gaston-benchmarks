package io.github.gaelrenoux.gaston.benchmarks


import fr.renoux.gaston.engine.{Improver, TabuSearchSlotImprover}
import org.openjdk.jmh.annotations._

import java.util.concurrent.TimeUnit

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
class MainBenchmark {

  @Benchmark
  def run(): Unit = {
    MainHarness.run(1000)
    // [info] Benchmark              Mode  Cnt  Score   Error  Units
    // [info] MainBenchmark.run      avgt   10  7.483 ± 0.637  ms/op
    // [info] MainBenchmark.run      avgt   10  7.131 ± 0.594  ms/op
    // [info] MainBenchmark.run      avgt   10  6.828 ± 0.592  ms/op
  }

  @Benchmark
  def runTabu(): Unit = {
    import MainHarness._
    implicit val improver: Improver = new TabuSearchSlotImprover()
    MainHarness.run(1000)
    // [info] Benchmark              Mode  Cnt  Score   Error  Units
    // [info] MainBenchmark.runTabu  avgt   10  6.957 ± 0.234  ms/op
    // [info] MainBenchmark.runTabu  avgt   10  7.024 ± 0.504  ms/op
    // [info] MainBenchmark.runTabu  avgt   10  6.972 ± 0.644  ms/op
  }


}
