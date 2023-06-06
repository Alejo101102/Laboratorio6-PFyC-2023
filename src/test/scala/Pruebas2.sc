import kmedianas._
import org.scalameter._

val standardConfig = config(
  Key.exec.minWarmupRuns := 20,
  Key.exec.maxWarmupRuns := 40,
  Key.exec.benchRuns := 25,
  Key.verbose := false
) withWarmer(Warmer.Default())

// Vamos a crear las pruebas con las variaciones automatizadas de una vez

// Variaciones
val variacionesNumPuntos = Seq(500) // Seq(100, 1000, 10000, 1000000)
val variacionesK = Seq(64) // Seq(2, 4, 8, 16, 32, 64)
val variacionesEta = Seq(0.01, 0.001)

// Iterador de variaciones
for {
  numPuntos <- variacionesNumPuntos
  k <- variacionesK
  eta <- variacionesEta
} yield {
  val puntosSeq = generarPuntosSeq(k, numPuntos)
  val medianasSeq = inicializarMedianasSeq(k, puntosSeq)

  val puntosPar = generarPuntosPar(k, numPuntos)
  val medianasPar = inicializarMedianasPar(k, puntosPar)

  println("")
  println("Test corriendo con numPuntos: " + numPuntos + " k: " + k + " eta: " + eta)

  val tiempoSeq = standardConfig measure {
    kMedianasSeq(puntosSeq, medianasSeq, eta)
  }

  val tiempoPar = standardConfig measure {
    kMedianasPar(puntosPar, medianasPar, eta)
  }

  println("Tiempo en secuencial:" + tiempoSeq.value)
  println("Tiempo en paralelo: " + tiempoPar.value)
  println("Ratio de aceleracion " + tiempoSeq.value/tiempoPar.value)
  println("")
}
