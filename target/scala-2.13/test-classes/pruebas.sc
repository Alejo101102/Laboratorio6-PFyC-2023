import kmedianas._
import org.scalameter._

val standardConfig = config (
  Key.exec.minWarmupRuns := 20 ,
  Key.exec.maxWarmupRuns := 40 ,
  Key.exec.benchRuns := 25 ,
  Key.verbose := false
) withWarmer (Warmer.Default ( ) )

val numPuntos = 500
val eta = 0.01
val k = 64
val puntosSeq = generarPuntosSeq ( k , numPuntos )
val medianasSeq = inicializarMedianasSeq ( k , puntosSeq )
val puntosPar = generarPuntosPar ( k , numPuntos )
val medianasPar = inicializarMedianasPar ( k , puntosPar )
println ( "hola" )
val tiempoSeq = standardConfig measure {
  kMedianasSeq (puntosSeq , medianasSeq , eta)
}
val tiempoPar = standardConfig measure {
  kMedianasPar (puntosPar , medianasPar , eta)
}
