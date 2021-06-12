package jonahshader.systems.engine.games

import jonahshader.systems.engine.*
import jonahshader.systems.engine.abilities.*
import jonahshader.systems.engine.playercontrollers.PlayerController
import jonahshader.systems.math.VecInt2

fun makeChessGame(player1Controller: PlayerController, player2Controller: PlayerController, moveTime: Float = .25f, moveLimit: Int = 200): BoardGame {
    val game = BoardGame(VecInt2(8, 8), listOf(player1Controller, player2Controller), moveTime, drawCondition = makeOrDraws(listOf(
        noMoveDraw, makeMoveLimitDraw(moveLimit)
    )))

    val queenAbilities = makeQueenAbilities(8)
    val rookAbilities = makeRookAbilities(8)
    val bishopAbilities = makeBishopAbilities(8)
    val knightAbilities = makeKnightAbilities()
    val kingAbilities = makeKingAbilities()

    val whitePawnAbilities = makePawnAbilities(true)
    val blackPawnAbilities = makePawnAbilities(false)

    for (i in 0 until 8) {
        game.addPieceToBoard(whitePawnAbilities, 0, 0, "P", VecInt2(i, 1))
        game.addPieceToBoard(blackPawnAbilities, 1, 1, "P", VecInt2(i, 6))
    }

    game.addPieceToBoard(rookAbilities, 0, 2, "R", VecInt2(0, 0))
    game.addPieceToBoard(rookAbilities, 0, 2, "R", VecInt2(7, 0))
    game.addPieceToBoard(rookAbilities, 1, 3, "R", VecInt2(0, 7))
    game.addPieceToBoard(rookAbilities, 1, 3, "R", VecInt2(7, 7))
    game.addPieceToBoard(knightAbilities, 0, 4, "N", VecInt2(1, 0))
    game.addPieceToBoard(knightAbilities, 0, 4, "N", VecInt2(6, 0))
    game.addPieceToBoard(knightAbilities, 1, 5, "N", VecInt2(1, 7))
    game.addPieceToBoard(knightAbilities, 1, 5, "N", VecInt2(6, 7))
    game.addPieceToBoard(bishopAbilities, 0, 6, "B", VecInt2(2, 0))
    game.addPieceToBoard(bishopAbilities, 0, 6, "B", VecInt2(5, 0))
    game.addPieceToBoard(bishopAbilities, 1, 7, "B", VecInt2(2, 7))
    game.addPieceToBoard(bishopAbilities, 1, 7, "B", VecInt2(5, 7))
    game.addPieceToBoard(queenAbilities, 0, 8, "Q", VecInt2(3, 0))
    val king0 = game.addPieceToBoard(kingAbilities, 0, 9, "K", VecInt2(4, 0))
    game.addPieceToBoard(queenAbilities, 1, 10, "Q", VecInt2(3, 7))
    val king1 = game.addPieceToBoard(kingAbilities, 1, 11, "K", VecInt2(4, 7))

    game.loseCondition = makePieceCaptureLoseCondition(listOf(listOf(king0.typeID), listOf(king1.typeID)))
    return game
}