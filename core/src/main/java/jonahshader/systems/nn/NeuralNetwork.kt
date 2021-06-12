package jonahshader.systems.nn

import kotlin.math.pow
import kotlin.math.tanh

class NeuralNetwork(inputs: Int, learningRate: Float = 0.001f, momentum: Float = 0.0f, batchSize: Int = 1, layers: List<LayerDescription>) {
    enum class LayerActivation{
        TANH,
        SIGMOID,
        RELU,
        PRELU,
        LINEAR
    }
    class LayerDescription(val size: Int, layerActivation: LayerActivation, dropout: Float = 0f) {
        val activation: Activation
        val activationDerivative: Activation
        val weightInit: WeightInit

        init {
            when (layerActivation) {
                LayerActivation.TANH -> {
                    activation = ::tanh
                    activationDerivative = tanhDerivative
                    weightInit = WeightInitializations.normalizedXavierInit
                }
                LayerActivation.SIGMOID -> {
                    activation = sigmoid
                    activationDerivative = sigmoidDerivative
                    weightInit = WeightInitializations.normalizedXavierInit
                }
                LayerActivation.RELU -> {
                    activation = relu
                    activationDerivative = reluDerivative
                    weightInit = WeightInitializations.heInit
                }
                LayerActivation.PRELU -> {
                    activation = prelu
                    activationDerivative = preluDerivative
                    weightInit = WeightInitializations.heInit
                }
                LayerActivation.LINEAR -> {
                    activation = linear
                    activationDerivative = linearDerivative
                    weightInit = WeightInitializations.heInit
                }
            }
        }
    }

    private val layers = mutableListOf<Layer>()
    init {
        for (i in layers.indices) {
            this.layers += when {
                i == layers.size - 1 -> Layer(
                    layers[i-1].size,
                    layers[i].size,
                    layers[i].activation,
                    layers[i].activationDerivative,
                    layers[i].weightInit,
                    batchSize,
                    learningRate,
                    momentum
                )
                i > 0 -> Layer(
                    layers[i-1].size,
                    layers[i].size,
                    layers[i].activation,
                    layers[i].activationDerivative,
                    layers[i].weightInit,
                    batchSize,
                    learningRate,
                    momentum
                )
                else -> Layer(
                    inputs,
                    layers[i].size,
                    layers[i].activation,
                    layers[i].activationDerivative,
                    layers[i].weightInit,
                    batchSize,
                    learningRate,
                    momentum
                )
            }
        }
    }

    fun train(input: Matrix, expectedOutput: Matrix) {
        layers[0].calculateOutput(input)
        for (i in 1 until layers.size) {
            layers[i].calculateOutput(layers[i-1].outputs)
        }
        layers.last().lastLayerCalculateError(expectedOutput)
        for (i in layers.size - 2 downTo(1)) { // downTo(1) or downTo(0)?
            layers[i].calculateError(layers[i+1].weights, layers[i+1].error)
        }
        layers[0].calculateDerivatives(input)
        for (i in 1 until layers.size) {
            layers[i].calculateDerivatives(layers[i-1].outputs)
        }
        for (l in layers) {
            l.calculateWeightChanges()
            l.applyWeightChanges()
        }
    }

    fun getError(input: Matrix, expectedOutput: Matrix): Float {
        val output = predict(input)
        var mse = 0f
        for (j in 0 until expectedOutput.getCols()) for (i in 0 until expectedOutput.getRows()) {
            mse += 0.5f * (expectedOutput.getElement(i, j) - output.getElement(i, j)).pow(2)
        }
        mse /= input.getCols()
        return mse
    }

    fun predict(input: Matrix): Matrix {
        layers[0].calculateOutput(input)
        for (i in 1 until layers.size) {
            layers[i].calculateOutput(layers[i-1].outputs)
        }
        return layers.last().outputs
    }

    fun changeBatchSize(batchSize: Int) {
        layers.forEach{ it.changeBatchSize(batchSize) }
    }

    fun setLearningRate(learningRate: Float) {
        layers.forEach {it.learningRate = learningRate}
    }
    fun setMomentum(momentum: Float) {
        layers.forEach {it.momentum = momentum}
    }
}