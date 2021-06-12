package jonahshader.systems.nn

import kotlin.math.E
import kotlin.math.pow
import kotlin.math.tanh

typealias Activation = (Float) -> Float

//val tanh: Activation = ::tanh
val tanhDerivative: Activation = { x: Float -> 1-tanh(x).pow(2)}

val relu: Activation = { x: Float -> x.coerceAtLeast(0f)}
val reluDerivative: Activation = { x: Float -> if (x > 0f) 1f else 0f}

val prelu: Activation = { x: Float -> if (x > 0f) x else x * .2f}
val preluDerivative: Activation = { x: Float -> if (x > 0f) 1f else .2f}

val linear: Activation = { x: Float -> x}
val linearDerivative: Activation = { _: Float -> 1f}

val sigmoid: Activation = { x: Float -> (1.0/(1+E.pow(-x.toDouble()))).toFloat()}
val sigmoidDerivative: Activation = { x: Float -> sigmoid(x) * (1- sigmoid(x))}

