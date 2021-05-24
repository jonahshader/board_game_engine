package jonahshader.systems.engine

import jonahshader.systems.math.VecInt2
import jonahshader.systems.math.zero

class Kernel(private val size: VecInt2) {
    private val radius = size / 2
    private val offsets = mutableListOf<VecInt2>()

    fun clear() {
        offsets.clear()
    }

    fun add(offset: VecInt2) {
        if (!offsets.contains(offset) && offset != zero &&
            offset.x >= -radius.x && offset.x <= radius.x &&
            offset.y >= -radius.y && offset.y <= radius.y)
            offsets += offset.copy()
    }


    fun addCircle(radius: Int) {
        for (y in -radius..radius) for (x in -radius..radius) {
            if (x * x + y * y <= radius * radius)
                add(VecInt2(x, y))
        }
    }

    fun addLine(radius: Int, horizontal: Boolean) {
        if (horizontal) {
            for (i in -radius..radius) {
                add(VecInt2(i, 0))
            }
        } else {
            for (i in -radius..radius) {
                add(VecInt2(0, i))
            }
        }
    }

    fun addDiagonalLine(radius: Int, topLeft: Boolean) {
        if (topLeft) {
            for (i in -radius..radius) {
                add(VecInt2(-i, i))
            }
        } else {
            for (i in -radius..radius) {
                add(VecInt2(i, i))
            }
        }
    }

    fun addRing(radius: Int) {
        val maxRadiusSquared = radius * radius
        val minRadiusSquared = (radius - 1) * (radius - 1)

        for (y in -radius..radius) for (x in -radius..radius) {
            val distSquared = x * x + y * y
            if (distSquared in (minRadiusSquared + 1)..maxRadiusSquared)
                add(VecInt2(x, y))
        }
    }

    fun addRotSymmetric4(offset: VecInt2) {
        add(offset)
        add(-offset)
        add(VecInt2(-offset.y, offset.x)) //? does this work lol
        add(VecInt2(offset.y, -offset.x))
    }
}