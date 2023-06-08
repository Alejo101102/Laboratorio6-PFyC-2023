import kmedianas._
import scala.collection.parallel.{ParMap,ParSeq}
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


// Prueba de tiempo

/*
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
*/

// Pruebas de clasificarPar

val puntos1 = ParSeq(new Punto(1.0, 2.0, 3.0), new Punto(4.0, 5.0, 6.0), new Punto(7.0, 8.0, 9.0))
val medianas1 = ParSeq(new Punto(0.0, 0.0, 0.0), new Punto(10.0, 10.0, 10.0))
val resultado1 = clasificarPar(puntos1, medianas1)
// Resultado esperado: ParMap((0.0, 0.0, 0.0) -> ParArray((1.0, 2.0, 3.0), (4.0, 5.0, 6.0)), (10.0, 10.0, 10.0) -> ParArray((7.0, 8.0, 9.0)))

val puntos2 = ParSeq(new Punto(1.0, 1.0, 1.0), new Punto(2.0, 2.0, 2.0), new Punto(3.0, 3.0, 3.0))
val medianas2 = ParSeq(new Punto(0.0, 0.0, 0.0), new Punto(4.0, 4.0, 4.0))
val resultado2 = clasificarPar(puntos2, medianas2)
// Resultado esperado: ParMap((4.0, 4.0, 4.0) -> ParArray((3.0, 3.0, 3.0)), (0.0, 0.0, 0.0) -> ParArray((1.0, 1.0, 1.0), (2.0, 2.0, 2.0)))

val puntos3 = ParSeq(new Punto(1.0, 1.0, 1.0), new Punto(2.0, 2.0, 2.0), new Punto(3.0, 3.0, 3.0))
val medianas3 = ParSeq(new Punto(1.0, 1.0, 1.0), new Punto(2.0, 2.0, 2.0), new Punto(3.0, 3.0, 3.0))
val resultado3 = clasificarPar(puntos3, medianas3)
// Resultado esperado: ParMap((2.0, 2.0, 2.0) -> ParArray((2.0, 2.0, 2.0)), (3.0, 3.0, 3.0) -> ParArray((3.0, 3.0, 3.0)), (1.0, 1.0, 1.0) -> ParArray((1.0, 1.0, 1.0)))

val puntos4 = ParSeq(new Punto(1.0, 1.0, 1.0), new Punto(2.0, 2.0, 2.0), new Punto(3.0, 3.0, 3.0))
val medianas4 = ParSeq(new Punto(4.0, 4.0, 4.0), new Punto(5.0, 5.0, 5.0), new Punto(6.0, 6.0, 6.0))
val resultado4 = clasificarPar(puntos4, medianas4)
// Resultado esperado: ParMap((4.0, 4.0, 4.0) -> ParArray((1.0, 1.0, 1.0), (2.0, 2.0, 2.0), (3.0, 3.0, 3.0)))

val puntos5 = ParSeq(new Punto(1.0, 1.0, 1.0), new Punto(2.0, 2.0, 2.0), new Punto(3.0, 3.0, 3.0))
val medianas5 = ParSeq(new Punto(2.0, 2.0, 2.0), new Punto(4.0, 4.0, 4.0))
val resultado5 = clasificarPar(puntos5, medianas5)
// Resultado esperado: ParMap((2.0, 2.0, 2.0) -> ParArray((1.0, 1.0, 1.0), (2.0, 2.0, 2.0), (3.0, 3.0, 3.0)))


// pruebas de actualizarPar

//Caso de prueba con medianas y clasificación no vacías:

val punto1 = new Punto(1.0, 2.0, 3.0)
val punto2 = new Punto(4.0, 5.0, 6.0)
val punto3 = new Punto(7.0, 8.0, 9.0)

val medianasViejas = ParSeq(punto1, punto2, punto3)

val puntosCluster1 = ParSeq(new Punto(1.5, 2.5, 3.5), new Punto(1.8, 2.8, 3.8))
val puntosCluster2 = ParSeq(new Punto(4.5, 5.5, 6.5), new Punto(4.8, 5.8, 6.8))
val clasificacion = ParMap(punto1 -> puntosCluster1, punto2 -> puntosCluster2)

val resultado = actualizarPar(clasificacion, medianasViejas)
// El resultado debe ser (1.65, 2.65, 3.65), (4.65, 5.65, 6.65), (7.0, 8.0, 9.0)


val punto1 = new Punto(1.0, 2.0, 3.0)
val punto2 = new Punto(4.0, 5.0, 6.0)
val punto3 = new Punto(7.0, 8.0, 9.0)

val medianasViejas = ParSeq(punto1, punto2, punto3)

val puntosCluster1 = ParSeq(new Punto(2.5, 6.5, 3.5), new Punto(2.8, 6.8, 3.8))
val puntosCluster2 = ParSeq(new Punto(5.5, 6.5, 7.5), new Punto(3.8, 4.8, 5.8))
val clasificacion = ParMap(punto1 -> puntosCluster1, punto2 -> puntosCluster2)


val resultado = actualizarPar(clasificacion, medianasViejas)
// El resultado debe ser (2.65, 6.65, 3.65), (4.65, 5.65, 6.65), (7.0, 8.0, 9.0)

val punto1 = new Punto(1.0, 2.0, 3.0)
val punto2 = new Punto(4.0, 5.0, 6.0)
val punto3 = new Punto(7.0, 8.0, 9.0)

val medianasViejas = ParSeq(punto1, punto2, punto3)

val puntosCluster1 = ParSeq(new Punto(2.5, 6.5, 3.5), new Punto(2.8, 6.8, 3.8))
val puntosCluster2 = ParSeq(new Punto(3.5, 7.5, 4.5), new Punto(10.8, 14.8, 15.8))
val puntosCluster3 = ParSeq(new Punto(11.5, 11.5, 13.5), new Punto(12.8, 5.8, 7.8))
val clasificacion = ParMap(punto1 -> puntosCluster1, punto2 -> puntosCluster2, punto3 -> puntosCluster3)


val resultado = actualizarPar(clasificacion, medianasViejas)
// El resultado debe ser (2.65, 6.65, 3.65), (7.15, 11.15, 10.15), (12.15, 8.65, 10.65)

val punto1 = new Punto(1.0, 2.0, 3.0)
val punto2 = new Punto(4.0, 5.0, 6.0)
val punto3 = new Punto(7.0, 8.0, 9.0)

val medianasViejas = ParSeq(punto1, punto2, punto3)

val puntosCluster1 = ParSeq(new Punto(12.5, 7.5, 3.5), new Punto(12.8, 6.8, 13.8))
val puntosCluster2 = ParSeq(new Punto(7.5, 8.5, 9.5), new Punto(5.8, 6.8, 8.8))
val clasificacion = ParMap(punto1 -> puntosCluster1, punto2 -> puntosCluster2)


val resultado = actualizarPar(clasificacion, medianasViejas)
// El resultado debe ser (12.65, 7.15, 8.65), (6.65, 7.65, 9.15), (7.0, 8.0, 9.0)

val punto1 = new Punto(1.0, 2.0, 3.0)
val punto2 = new Punto(4.0, 5.0, 6.0)
val punto3 = new Punto(7.0, 8.0, 9.0)

val medianasViejas = ParSeq(punto1, punto2, punto3)

val puntosCluster1 = ParSeq(new Punto(22.5, 6.5, 23.5), new Punto(2.8, 16.8, 3.8))
val puntosCluster2 = ParSeq(new Punto(15.5, 16.5, 18.5), new Punto(12.8, 16.8, 19.8))
val puntosCluster3 = ParSeq(new Punto(13.5, 11.5, 12.5), new Punto(2.8, 16.8, 9.8))
val clasificacion = ParMap(punto1 -> puntosCluster1, punto2 -> puntosCluster2, punto3 -> puntosCluster3)


val resultado = actualizarPar(clasificacion, medianasViejas)
// El resultado debe ser (12.65, 11.65, 13.65), (14.15, 16.64, 19.14), (8.15, 14.15, 11.15)



// Pruebas función hay convergencia

val medianasViejas = ParSeq(new Punto(1.0, 2.0, 3.0), new Punto(4.0, 5.0, 6.0))
val medianasNuevas = ParSeq(new Punto(1.01, 1.99, 3.01), new Punto(3.99, 5.01, 6.0))
val eta = 0.01
val resultado = hayConvergenciaPar(eta, medianasViejas, medianasNuevas)
// Se espera que resultado sea true

val medianasViejas = ParSeq(new Punto(1.0, 2.0, 3.0), new Punto(4.0, 5.0, 6.0))
val medianasNuevas = ParSeq(new Punto(1.01, 1.99, 3.01), new Punto(4.02, 5.01, 6.0))
val eta = 0.01
val resultado = hayConvergenciaPar(eta, medianasViejas, medianasNuevas)
// Se espera que resultado sea true

val medianasViejas = ParSeq(new Punto(1.0, 2.0, 3.0), new Punto(4.0, 5.0, 6.0))
val medianasNuevas = ParSeq(new Punto(1.01, 2.99, 4.01), new Punto(7.02, 5.01, 6.0))
val eta = 0.01
val resultado = hayConvergenciaPar(eta, medianasViejas, medianasNuevas)
// Se espera que resultado sea false

val medianasViejas = ParSeq(new Punto(1.0, 2.0, 3.0), new Punto(4.0, 5.0, 6.0))
val medianasNuevas = ParSeq(new Punto(2.01, 1.99, 3.01), new Punto(4.02, 8.01, 5.0))
val eta = 0.01
val resultado = hayConvergenciaPar(eta, medianasViejas, medianasNuevas)
// Se espera que resultado sea false

val medianasViejas = ParSeq(new Punto(4.0, 6.0, 3.0), new Punto(7.0, 5.0, 8.0))
val medianasNuevas = ParSeq(new Punto(3.01, 1.99, 5.01), new Punto(6.02, 5.01, 7.0))
val eta = 0.01
val resultado = hayConvergenciaPar(eta, medianasViejas, medianasNuevas)
// Se espera que resultado sea false

val medianasViejas = ParSeq(new Punto(1.0, 2.0, 3.0), new Punto(4.0, 5.0, 6.0))
val medianasNuevas = ParSeq(new Punto(1.001, 1.999, 3.0), new Punto(4.0, 5.0, 6.0))
val eta = 0.001
val resultado = hayConvergenciaPar(eta, medianasViejas, medianasNuevas)
// Se espera que resultado sea true

val medianasViejas = ParSeq(new Punto(1.0, 2.0, 3.0), new Punto(4.0, 5.0, 6.0))
val medianasNuevas = ParSeq(new Punto(1.001, 1.999, 3.0), new Punto(4.01, 5.0, 6.0))
val eta = 0.001
val resultado = hayConvergenciaPar(eta, medianasViejas, medianasNuevas)
// Se espera que resultado sea true

val medianasViejas = ParSeq(new Punto(1.0, 2.0, 3.0), new Punto(4.0, 5.0, 6.0))
val medianasNuevas = ParSeq(new Punto(1.001, 2.999, 3.0), new Punto(4.01, 5.0, 8.0))
val eta = 0.001
val resultado = hayConvergenciaPar(eta, medianasViejas, medianasNuevas)
// Se espera que resultado sea false

val medianasViejas = ParSeq(new Punto(1.0, 2.0, 3.0), new Punto(4.0, 5.0, 6.0))
val medianasNuevas = ParSeq(new Punto(1.001, 5.999, 6.0), new Punto(3.01, 7.0, 6.0))
val eta = 0.001
val resultado = hayConvergenciaPar(eta, medianasViejas, medianasNuevas)
// Se espera que resultado sea false

val medianasViejas = ParSeq(new Punto(4.0, 7.0, 3.0), new Punto(4.0, 5.0, 6.0))
val medianasNuevas = ParSeq(new Punto(12.001, 14.999, 3.0), new Punto(4.01, 15.0, 9.0))
val eta = 0.001
val resultado = hayConvergenciaPar(eta, medianasViejas, medianasNuevas)
// Se espera que resultado sea false


// Pruebas función kMedianasPar

val eta1 = 0.01
val eta2 = 0.001


val puntos1 = ParSeq(new Punto(1.0, 2.0, 3.0), new Punto(4.0, 5.0, 6.0), new Punto(7.0, 8.0, 9.0), new Punto(6.0, 8.0, 2.0))
val medianas1 = ParSeq(new Punto(0.0, 0.0, 0.0), new Punto(10.0, 10.0, 10.0))
val resultado1 = kMedianasPar(puntos1, medianas1, eta1)
// Resultado esperado (2.5, 3.5, 4.5), (6.5, 8.0, 5.5)

val puntos2 = ParSeq(new Punto(3.0, 1.0, 3.0), new Punto(2.0, 1.0, 2.0), new Punto(3.0, 6.0, 9.0), new Punto(7.0, 8.0, 9.0))
val medianas2 = ParSeq(new Punto(2.0, 2.0, 2.0), new Punto(4.0, 4.0, 4.0))
val resultado2 = kMedianasPar(puntos2, medianas2,eta1)
// Resultado esperado (2.5, 1.0, 2.5), (5.0, 7.0, 9.0)

val puntos3 = ParSeq(new Punto(11.0, 1.0, 11.0), new Punto(7.0, 2.0, 7.0), new Punto(8.0, 3.0, 8.0), new Punto(3.0, 1.0, 3.0))
val medianas3 = ParSeq(new Punto(1.01, 1.99, 3.01), new Punto(4.02, 5.01, 6.0))
val resultado3 = kMedianasPar(puntos3, medianas3, eta1)
//Resultado esperado (3.0, 1.0, 3.0), (8.66, 2.0, 8.66)

val puntos4 = ParSeq(new Punto(1.0, 21.0, 1.0), new Punto(4.0, 2.0, 12.0), new Punto(13.0, 6.0, 3.0), new Punto(7.0, 2.0, 7.0))
val medianas4 = ParSeq(new Punto(1.01, 1.99, 3.01), new Punto(3.99, 5.01, 6.0))
val resultado4 = kMedianasPar(puntos4, medianas4, eta1)
// Resultado esperado (1.01, 1.99, 3.01), (6.25, 7.75, 5.75)

val puntos5 = ParSeq(new Punto(11.0, 12.0, 13.0), new Punto(2.0, 2.0, 2.0), new Punto(13.0, 13.0, 13.0), new Punto(1.0, 21.0, 1.0))
val medianas5 = ParSeq(new Punto(2.0, 2.0, 2.0), new Punto(4.0, 4.0, 4.0))
val resultado5 = kMedianasPar(puntos5, medianas5, eta1)
// Resultado esperado (2.0, 2.0, 2.0), (8.33, 15.33, 9.0)

val puntos6 = ParSeq(new Punto(8.0, 9.0, 1.0), new Punto(7.0, 2.0, 2.0), new Punto(4.0, 3.0, 4.0), new Punto(11.0, 12.0, 13.0))
val medianas6 = ParSeq(new Punto(2.0, 2.0, 2.0), new Punto(4.0, 4.0, 4.0))
val resultado6 = kMedianasPar(puntos6, medianas6, eta2)
// Resultado esperado (5.5, 2.5, 3.0), (9.5, 10.5, 7.0)

val puntos7 = ParSeq(new Punto(13.0, 1.0, 9.0), new Punto(12.0, 14.0, 2.0), new Punto(3.0, 3.0, 3.0), new Punto(2.0, 2.0, 2.0))
val medianas7 = ParSeq(new Punto(2.01, 1.99, 3.01), new Punto(4.02, 8.01, 5.0))
val resultado7 = kMedianasPar(puntos7, medianas7, eta2)
// Resultado esperado (2.5, 2.5, 2.5), (12.5, 7.5, 5.5)

val puntos8 = ParSeq(new Punto(1.0, 6.0, 11.0), new Punto(2.0, 2.0, 2.0), new Punto(13.0, 3.0, 5.0), new Punto(12.0, 14.0, 2.0))
val medianas8 = ParSeq(new Punto(1.001, 1.999, 3.0), new Punto(4.0, 5.0, 6.0))
val resultado8 = kMedianasPar(puntos8, medianas8, eta2)
// Resultado esperado (2.0, 2.0, 2.0), (8.66, 7.66, 6.0)

val puntos9 = ParSeq(new Punto(6.0, 1.0, 9.0), new Punto(2.0, 12.0, 12.0), new Punto(3.0, 6.0, 6.0), new Punto(13.0, 3.0, 5.0))
val medianas9 = ParSeq(new Punto(1.001, 5.999, 6.0), new Punto(3.01, 7.0, 6.0))
val resultado9= kMedianasPar(puntos9, medianas9, eta2)
// Resultado esperado (2.5, 9.0, 9.0), (9.5, 2.0, 7.0)

val puntos10 = ParSeq(new Punto(9.0, 1.0, 3.0), new Punto(4.0, 2.0, 6.0), new Punto(2.0, 13.0, 7.0), new Punto(6.0, 1.0, 9.0))
val medianas10 = ParSeq(new Punto(12.001, 14.999, 3.0), new Punto(4.01, 15.0, 9.0))
val resultado10 = kMedianasPar(puntos10, medianas10, eta2)
// Resultado esperado (9.0, 1.0, 3.0), (4.0, 5.33, 7.33)