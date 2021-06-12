package jonahshader.systems.nn

class Matrix(private val rows: Int, private val cols: Int, neuronInputs: Int, weightInit: WeightInit = { _, _->0f}) {
    private val data = FloatArray(rows * cols)
    private var transposed = false

    constructor(rows: Int, cols: Int, weightInit: WeightInit = { _, _->0f}) : this(rows, cols, cols, weightInit)

    init {
        for (i in 0 until getRows()) for (j in 0 until getCols()) {
            setElement(i, j, weightInit(neuronInputs.toFloat(), getRows().toFloat()))
        }
    }

    fun transpose() { transposed = !transposed }
    // starting at 0, not 1
    fun getElement(row: Int, col: Int) : Float = data[getIndexFromPos(row, col)]
    fun setElement(row: Int, col: Int, value: Float) { data[getIndexFromPos(row, col)] = value}
    fun incrementElement(row: Int, col: Int, increment: Float) { data[getIndexFromPos(row, col)] += increment}
    fun getRows() = if (transposed) cols else rows
    fun getCols() = if (transposed) rows else cols
    fun applyComponentWiseFunction(function: (Float) -> Float) { for (i in data.indices) { data[i] = function(data[i]) } }
    fun multiplyIntoHere(m1: Matrix, m2: Matrix) {
        assert(m1.getCols() == m2.getRows()) {"Matrix shape mismatch! m1 and m2 are incompatible! m1: (${m1.getRows()}, ${m1.getCols()}) m2: (${m2.getRows()}, ${m2.getCols()}) destination: (${getRows()}, ${getCols()})"}
        assert(m1.getRows() == getRows() && m2.getCols() == getCols()) {"Matrix shape mismatch! m1 * m2 requires destination matrix with size (${m1.getRows()}, ${m2.getCols()}): (${m1.getRows()}, ${m1.getCols()}) m2: (${m2.getRows()}, ${m2.getCols()}) destination: (${getRows()}, ${getCols()})"}
        for (i in 0 until getRows()) for (j in 0 until getCols()) {
            var sum = 0f
            for (k in 0 until m1.getCols()) {
                sum += m1.getElement(i, k) * m2.getElement(k, j)
            }
            setElement(i, j, sum)
        }
    }

    fun subtractIntoHere(m1: Matrix, m2: Matrix) {
        assert(m1.getRows() == m2.getRows() && m1.getCols() == m2.getCols()) {"Matrix shape mismatch!"}
        for (j in 0 until getCols()) for (i in 0 until getRows()) {
            setElement(i, j, m1.getElement(i, j) - m2.getElement(i, j))
        }
    }

    fun hadamardProductIntoHere(m: Matrix) {
        assert(m.getRows() == getRows() && m.getCols() == getCols()) {"Matrix shape mismatch!"}
        for (j in 0 until getCols()) for (i in 0 until getRows()) {
            setElement(i, j, getElement(i, j) * m.getElement(i, j))
        }
    }

    fun copyIntoHere(m: Matrix) {
        assert(m.getRows() == getRows() && m.getCols() == getCols()) {"Matrix shape mismatch!"}
        for (j in 0 until getCols()) for (i in 0 until getRows()) {
            setElement(i, j, m.getElement(i, j))
        }
    }

    fun addVectorToColumns(v: Matrix) {
        assert(v.getCols() == 1) {"Vector expected, but got matrix with cols != 1"}
        for (j in 0 until getCols()) for (i in 0 until getRows()) {
            incrementElement(i, j, v.getElement(i, 0))
        }
    }

    operator fun plusAssign(m: Matrix) {
        assert(getRows() == m.getRows() && getCols() == m.getCols()) {"Matrix shape mismatch!"}
        for (j in 0 until getCols()) for (i in 0 until getRows()) {
            incrementElement(i, j, m.getElement(i, j))
        }
    }

    private fun getIndexFromPos(row: Int, col: Int): Int {
        assert(row >= 0 && row < getRows()) {"Row out of bounds. row = $row, rows = ${getRows()}"}
        assert(col >= 0 && col < getCols()) {"Col out of bounds. col = $col, rows = ${getCols()}"}
        return if (transposed) {
            row + col * cols // rows
        } else {
            col + row * cols
        }
    }

    private fun Float.format(digits: Int) = "%.${digits}f".format(this)
    override fun toString(): String {
        var output = ""
        for (i in 0 until getRows()) {
            for (j in 0 until getCols()) {
                output += getElement(i, j).format(2) + " "
            }
            output += "\n"
        }
        return output
    }
    fun getSizeAsString() : String = "${getRows()}, ${getCols()}"

    fun print() {
        println(toString())
    }

    operator fun timesAssign(scalar: Float) {
        for (i in data.indices) {
            data[i] *= scalar
        }
    }

    fun addScaledIntoHere(m: Matrix, scalar: Float) {
        assert(getRows() == m.getRows() && getCols() == m.getCols()) {"Matrix shape mismatch! m1 and m2 are incompatible! m: (${m.getRows()}, ${m.getCols()}) destination: (${getRows()}, ${getCols()})"}
        for (j in 0 until getCols()) for (i in 0 until getRows()) {
            incrementElement(i, j, m.getElement(i, j) * scalar)
        }
    }

    fun addScaledMatrixToVectorIntoHere(m: Matrix, scalar: Float) {
        assert(getRows() == m.getRows() && getCols() == 1) {"Matrix shape mismatch! m1 and m2 are incompatible! m: (${m.getRows()}, ${m.getCols()}) destination: (${getRows()}, ${getCols()})"}
        for (j in 0 until getCols()) for (i in 0 until getRows()) {
            incrementElement(i, 0, m.getElement(i, j) * scalar)
        }
    }
}