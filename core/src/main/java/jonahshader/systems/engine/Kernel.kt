package jonahshader.systems.engine

import jonahshader.systems.math.VecInt2
import jonahshader.systems.math.zero

data class Kernel(private val size: VecInt2, private val data: MutableList<VecInt2> = mutableListOf()) {
    private val radius = size / 2

    init {
        assert(size.x % 2 == 1 && size.y % 2 == 1) {"Kernel size $size must be odd!"}
    }

    val offsets: List<VecInt2>
        get() = data

    fun clear() {
        data.clear()
    }

    fun add(offset: VecInt2) {
        if (!data.contains(offset) && offset != zero &&
            offset.x >= -radius.x && offset.x <= radius.x &&
            offset.y >= -radius.y && offset.y <= radius.y)
            data += offset.copy()
    }

    fun fill() {
        for (y in (-radius.y)..(radius.y)) for (x in (-radius.x)..(radius.x)) {
            add(VecInt2(x, y))
        }
    }

    fun addCircle(radius: Int) {
        for (y in -radius..radius) for (x in -radius..radius) {
            if (x * x + y * y <= radius * radius)
                add(VecInt2(x, y))
        }
    }

    fun addLine(radius: Int, step: VecInt2) {
        val pos = VecInt2()
        for (i in 1..radius) {
            pos += step
            add(pos)
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
        add(VecInt2(-offset.y, offset.x))
        add(VecInt2(offset.y, -offset.x))
    }
    fun addRotMirrorSymmetric8(offset: VecInt2) {
        add(offset)
        add(-offset)
        add(VecInt2(-offset.y, offset.x))
        add(VecInt2(offset.y, -offset.x))
        add(VecInt2(offset.x, -offset.y))
        add(VecInt2(-offset.x, -offset.y))
        add(VecInt2(offset.y, offset.x))
        add(VecInt2(-offset.y, -offset.x))
    }
    fun addMirrored(offset: VecInt2, acrossVertical: Boolean) {
        add(offset)
        if (acrossVertical) {
            add(VecInt2(-offset.x, offset.y))
        } else {
            add(VecInt2(offset.x, -offset.y))
        }
    }

    fun makeGlobalPosList(piecePos: VecInt2) : List<VecInt2> = data.map { it + piecePos }

    fun mirror(acrossVertical: Boolean) {
        if (acrossVertical)
            data.forEach { it.x = -it.x}
        else
            data.forEach { it.y = -it.y}
    }

    fun makeMirroredCopy(acrossVertical: Boolean): Kernel {
        val mirroredCopy = copy()
        mirroredCopy.mirror(acrossVertical)
        return mirroredCopy
    }

    operator fun plusAssign(kernel: Kernel) {
        kernel.data.forEach { add(it) }
    }
    operator fun plusAssign(offset: VecInt2) {
        add(offset)
    }
}