package jonahshader.systems.engine.abilities

import jonahshader.systems.engine.*
import jonahshader.systems.math.VecInt2
import kotlin.math.pow

typealias ValueFun = (Piece) -> Float

// TODO: make pieces able to swap out ability after a condition is met. e.g. pawns will switch move kernels after first move
// TODO: also need to figure out a general mechanism for enable things like en passant and castling. some interaction with other pieces is required.
fun makePawnAbilities(white: Boolean, boardSize: VecInt2, queen: Piece): MutableList<Ability> {
    val direction = if (white) 1 else -1

    val firstMoveKernel = Kernel(VecInt2(5, 5))
    firstMoveKernel += VecInt2(0, direction)
    firstMoveKernel += VecInt2(0, direction * 2)

    val secondMoveKernel = Kernel(VecInt2(3, 3))
    secondMoveKernel += VecInt2(0, direction)

    val captureKernel = Kernel(VecInt2(3, 3))
    captureKernel += VecInt2(-1, direction)
    captureKernel += VecInt2(1, direction)

    val firstMoveAbility = Ability.makeSlideMoveAbility(listOf(firstMoveKernel))
    val secondMoveAbility = Ability.makeJumpMoveAbility(secondMoveKernel)
    val captureAbility = changePieceAtRow(Ability.makeJumpCaptureAbility(captureKernel), queen, if (white) boardSize.y - 1 else 0)

    //changeAbilityAfterMove(firstMoveAbility, secondMoveAbility)
//    val moveAbility = changePieceAtRow(, queen, if (white) boardSize.y - 1 else 0)
    /*TODO: instead of using changeAbilityAfterMove, just change it when at y=1 or height-1 for white and black respectively.
        this is how its handled in lichess for custom positions*/
    val moveAbility = changeAbilityAfterMove(firstMoveAbility, changePieceAtRow(secondMoveAbility, queen, if (white) boardSize.y - 1 else 0))

    return mutableListOf(moveAbility, captureAbility)
}

fun makePawnValueFun(white: Boolean, boardSize: Int): ValueFun = if (white) { piece ->
    .8f + (piece.pos.y / (boardSize - 1f)).pow(3) * 1.5f
} else { piece ->
    .8f + (((boardSize - 1) -piece.pos.y) / (boardSize - 1f)).pow(3) * 1.5f
}

fun makeRookAbilities(boardSize: Int): MutableList<Ability> {
    val kernels = mutableListOf<Kernel>()
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(boardSize, VecInt2(-1, 0))
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(boardSize, VecInt2(1, 0))
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(boardSize, VecInt2(0, -1))
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(boardSize, VecInt2(0, 1))

    return mutableListOf(Ability.makeSlideMoveAbility(kernels), Ability.makeSlideCaptureAbility(kernels))
}

fun makeRookValueFun(): ValueFun = { _ -> 5f }

fun makeKnightAbilities(): MutableList<Ability> {
    val knightKernel = Kernel(VecInt2(5, 5))
    knightKernel.addMirrored(VecInt2(1, 2), true)
    knightKernel.addMirrored(VecInt2(2, 1), true)
    knightKernel.addMirrored(VecInt2(1, -2), true)
    knightKernel.addMirrored(VecInt2(2, -1), true)

    return mutableListOf(Ability.makeJumpMoveAbility(knightKernel), Ability.makeJumpCaptureAbility(knightKernel))
}

fun makeKnightValueFun(boardSize: VecInt2): ValueFun = { piece -> 3f *
        (if (piece.pos.x == 0 || piece.pos.x == boardSize.x - 1) .9f else 1f) *
        (if (piece.pos.y == 0 || piece.pos.y == boardSize.y - 1) .9f else 1f)
}

fun makeKingAbilities(): MutableList<Ability> {
    val kingKernel = Kernel(VecInt2(3, 3))
    kingKernel.fill()
    return mutableListOf(Ability.makeJumpMoveAbility(kingKernel), Ability.makeJumpCaptureAbility(kingKernel))
}

fun makeKingValueFun(): ValueFun = { _ -> 9999f }

fun makeBishopAbilities(boardSize: Int): MutableList<Ability> {
    val kernels = mutableListOf<Kernel>()
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(8, VecInt2(1, 1))
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(8, VecInt2(1, -1))
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(8, VecInt2(-1, 1))
    kernels += Kernel(VecInt2(boardSize * 2 - 1, boardSize * 2 - 1))
    kernels.last().addLine(8, VecInt2(-1, -1))

    return mutableListOf(Ability.makeSlideMoveAbility(kernels), Ability.makeSlideCaptureAbility(kernels))
}

fun makeBishopValueFun(): ValueFun = { _ -> 3.1f }

fun makeQueenAbilities(boardSize: Int): MutableList<Ability> {
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

    return mutableListOf(Ability.makeSlideMoveAbility(kernels), Ability.makeSlideCaptureAbility(kernels))
}

fun makeQueenValueFun(): ValueFun = { _ -> 9f }