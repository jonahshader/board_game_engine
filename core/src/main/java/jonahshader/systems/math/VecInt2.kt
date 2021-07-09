package jonahshader.systems.math

import com.badlogic.gdx.math.Vector2

data class VecInt2(var x: Int, var y: Int) {
    constructor(): this(0, 0)
    constructor(v: VecInt2): this(v.x, v.y)
    constructor(v: Vector2): this(v.x.toInt(), v.y.toInt())

    operator fun plus(v: VecInt2): VecInt2 = VecInt2(x + v.x, y + v.y)
    operator fun plusAssign(v: VecInt2) {
        x += v.x
        y += v.y
    }
    operator fun minus(v: VecInt2): VecInt2 = VecInt2(x - v.x, y - v.y)
    operator fun minusAssign(v: VecInt2) {
        x -= v.x
        y -= v.y
    }
    operator fun times(s: Int): VecInt2 = VecInt2(x * s, y * s)
    operator fun timesAssign(s: Int) {
        x *= s
        y *= s
    }
    operator fun times(s: Float): VecInt2 = VecInt2((x * s).toInt(), (y * s).toInt())
    operator fun timesAssign(s: Float) {
        x = (x * s).toInt()
        y = (y * s).toInt()
    }
    operator fun div(s: Int): VecInt2 = VecInt2(x / s, y / s)
    operator fun divAssign(s: Int) {
        x /= s
        y /= s
    }

    operator fun unaryMinus(): VecInt2 = VecInt2(-x, -y)
    fun set(to: VecInt2) {
        x = to.x
        y = to.y
    }
}

val zero = VecInt2(0, 0)
