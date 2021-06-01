package jonahshader.systems.engine.abilities

import jonahshader.systems.engine.Ability
import jonahshader.systems.engine.Kernel
import jonahshader.systems.math.VecInt2

// TODO: make pieces able to swap out ability after a condition is met. e.g. pawns will switch move kernels after first move
// TODO: also need to figure out a general mechanism for enable things like en passant and castling. some interaction with other pieces is required.
fun makePawnAbilities(white: Boolean): List<Ability> {
    val direction = if (white) 1 else -1
    val moveKernel = Kernel(VecInt2(3, 3))
    moveKernel += VecInt2(0, direction)
    val captureKernel = Kernel(VecInt2(3, 3))
    captureKernel += VecInt2(-1, direction)
    captureKernel += VecInt2(1, direction)
    return listOf(Ability.makeJumpMoveAbility(moveKernel), Ability.makeJumpCaptureAbility(captureKernel))
}

fun makeRookAbilities(boardSize: Int): List<Ability> {
    val kernels = mutableListOf<Kernel>()
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(boardSize, VecInt2(-1, 0))
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(boardSize, VecInt2(1, 0))
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(boardSize, VecInt2(0, -1))
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(boardSize, VecInt2(0, 1))

    return listOf(Ability.makeSlideMoveAbility(kernels), Ability.makeSlideCaptureAbility(kernels))
}

fun makeKnightAbilities(): List<Ability> {
    val knightKernel = Kernel(VecInt2(5, 5))
    knightKernel.addMirrored(VecInt2(1, 2), true)
    knightKernel.addMirrored(VecInt2(2, 1), true)
    knightKernel.addMirrored(VecInt2(1, -2), true)
    knightKernel.addMirrored(VecInt2(2, -1), true)

    return listOf(Ability.makeJumpMoveAbility(knightKernel), Ability.makeJumpCaptureAbility(knightKernel))
}

fun makeKingAbilities(): List<Ability> {
    val kingKernel = Kernel(VecInt2(3, 3))
    kingKernel.fill()
    return listOf(Ability.makeJumpMoveAbility(kingKernel), Ability.makeJumpCaptureAbility(kingKernel))
}

fun makeBishopAbilities(boardSize: Int): List<Ability> {
    val kernels = mutableListOf<Kernel>()
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(8, VecInt2(1, 1))
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(8, VecInt2(1, -1))
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(8, VecInt2(-1, 1))
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(8, VecInt2(-1, -1))

    return listOf(Ability.makeSlideMoveAbility(kernels), Ability.makeSlideCaptureAbility(kernels))
}

fun makeQueenAbilities(boardSize: Int): List<Ability> {
    val kernels = mutableListOf<Kernel>()
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(8, VecInt2(1, 1))
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(8, VecInt2(1, -1))
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(8, VecInt2(-1, 1))
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(8, VecInt2(-1, -1))
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(boardSize, VecInt2(-1, 0))
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(boardSize, VecInt2(1, 0))
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(boardSize, VecInt2(0, -1))
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(boardSize, VecInt2(0, 1))

    return listOf(Ability.makeSlideMoveAbility(kernels), Ability.makeSlideCaptureAbility(kernels))
}