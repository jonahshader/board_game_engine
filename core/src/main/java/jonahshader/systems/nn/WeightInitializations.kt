package jonahshader.systems.nn

import java.util.*
import kotlin.math.sqrt

typealias WeightInit = (Float, Float) -> Float

object WeightInitializations {
    // TODO: add 1 to inputs to account for bias?
    private val rand = Random()
    // xavier is for tanh or sigmoid
    val normalizedXavierInit: WeightInit = { inputs: Float, outputs: Float -> (if(rand.nextBoolean()) 1 else -1) * rand.nextFloat() * sqrt(6f) / sqrt(inputs + outputs)}
    // he is for relu
    val heInit: WeightInit = { inputs: Float, _: Float -> rand.nextGaussian().toFloat() * sqrt(2/inputs)}

    fun setSeed(seed: Long) {
        rand.setSeed(seed)
    }
}
