package io.github.gaelrenoux.gaston.benchmarks

package object utils {

  def repeat[A](n: Int)(f: => A): A = {
    var i = n
    while (i > 1) {
      f
      i -= 1
    }
    f
  }

}
