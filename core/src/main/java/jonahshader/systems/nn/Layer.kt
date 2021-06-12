package jonahshader.systems.nn

import jonahshader.systems.nn.WeightInitializations.normalizedXavierInit
import kotlin.math.tanh

class Layer(
    inputs: Int, private val layerSize: Int, private val activation: (Float) -> Float = ::tanh,
    private val activationDerivative: (Float) -> Float = tanhDerivative,
    weightInit: (Float, Float) -> Float = normalizedXavierInit,
    private var batchSize: Int,
    var learningRate: Float, var momentum: Float
) {
    val weights = Matrix(layerSize, inputs, inputs + 1, weightInit)
    private val weightDerivatives = Matrix(layerSize, inputs)
    private val weightChanges = Matrix(layerSize, inputs)
    private val bias = Matrix(layerSize, 1, inputs + 1, weightInit)
    private val biasChanges = Matrix(layerSize, 1)
    private var z = Matrix(layerSize, batchSize)
    var outputs = Matrix(layerSize, batchSize)
    private var outputsMinusExpected = Matrix(layerSize, batchSize)
    var error = Matrix(layerSize, batchSize)
    private var nextWeightsTimesNextError = Matrix(layerSize, batchSize)

    fun changeBatchSize(batchSize: Int) {
        this.batchSize = batchSize
        z = Matrix(layerSize, batchSize)
        outputs = Matrix(layerSize, batchSize)
        outputsMinusExpected = Matrix(layerSize, batchSize)
        error = Matrix(layerSize, batchSize)
        nextWeightsTimesNextError = Matrix(layerSize, batchSize)
    }

    fun calculateOutput(input: Matrix) {
        z.multiplyIntoHere(weights, input)
        z.addVectorToColumns(bias)
        outputs.copyIntoHere(z)
        outputs.applyComponentWiseFunction(activation)
    }

    fun lastLayerCalculateError(expectedOutput: Matrix) {
        outputsMinusExpected.subtractIntoHere(outputs, expectedOutput)
        error.copyIntoHere(z)
        error.applyComponentWiseFunction(activationDerivative)
        error.hadamardProductIntoHere(outputsMinusExpected)
    }

    fun calculateError(nextWeights: Matrix, nextError: Matrix) {
        error.copyIntoHere(z)
        error.applyComponentWiseFunction(activationDerivative)

        nextWeights.transpose()
        nextWeightsTimesNextError.multiplyIntoHere(nextWeights, nextError)
        nextWeights.transpose()

        error.hadamardProductIntoHere(nextWeightsTimesNextError)
    }

    fun calculateDerivatives(previousOutputs: Matrix) {
        previousOutputs.transpose()
        weightDerivatives.multiplyIntoHere(error, previousOutputs)
        previousOutputs.transpose()
    }

    fun calculateWeightChanges() {
        weightChanges *= momentum
        weightChanges.addScaledIntoHere(weightDerivatives, -learningRate / batchSize)

        biasChanges *= momentum
        biasChanges.addScaledMatrixToVectorIntoHere(error, -learningRate / batchSize)
    }

    fun applyWeightChanges() {
        weights += weightChanges
        bias += biasChanges
    }
}