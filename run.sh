sbt "v0_10/runMain io.github.gaelrenoux.gaston.benchmarks.heavy.main 6" | tee -a report-20241112-v0.10
sbt  "v1_0/runMain io.github.gaelrenoux.gaston.benchmarks.heavy.main 6" | tee -a report-20241112-v1.0
sbt  "v1_1/runMain io.github.gaelrenoux.gaston.benchmarks.heavy.main 6" | tee -a report-20241112-v1.1
