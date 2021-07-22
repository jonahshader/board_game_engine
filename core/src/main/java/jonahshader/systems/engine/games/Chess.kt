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

    game.addPieceToBoard(rookAbilities, 0, 2, "R", 5f, VecInt2(0, 0))
    game.addPieceToBoard(rookAbilities, 0, 2, "R", 5f, VecInt2(7, 0))
    game.addPieceToBoard(rookAbilities, 1, 3, "R", 5f, VecInt2(0, 7))
    game.addPieceToBoard(rookAbilities, 1, 3, "R", 5f, VecInt2(7, 7))
    game.addPieceToBoard(knightAbilities, 0, 4, "N", 3f, VecInt2(1, 0))
    game.addPieceToBoard(knightAbilities, 0, 4, "N", 3f, VecInt2(6, 0))
    game.addPieceToBoard(knightAbilities, 1, 5, "N", 3f, VecInt2(1, 7))
    game.addPieceToBoard(knightAbilities, 1, 5, "N", 3f, VecInt2(6, 7))
    game.addPieceToBoard(bishopAbilities, 0, 6, "B", 3.1f, VecInt2(2, 0))
    game.addPieceToBoard(bishopAbilities, 0, 6, "B", 3.1f, VecInt2(5, 0))
    game.addPieceToBoard(bishopAbilities, 1, 7, "B", 3.1f, VecInt2(2, 7))
    game.addPieceToBoard(bishopAbilities, 1, 7, "B", 3.1f, VecInt2(5, 7))
    val queen0: Piece = game.addPieceToBoard(queenAbilities, 0, 8, "Q", 9f, VecInt2(3, 0))
    val king0 = game.addPieceToBoard(kingAbilities, 0, 9, "K", 9999f, VecInt2(4, 0))
    val queen1 = game.addPieceToBoard(queenAbilities, 1, 10, "Q", 9f, VecInt2(3, 7))
    val king1 = game.addPieceToBoard(kingAbilities, 1, 11, "K", 9999f, VecInt2(4, 7))

    for (i in 0 until 8) {
        game.addPieceToBoard(makePawnAbilities(true, VecInt2(8, 8), queen0), 0, 0, "P",  1f,VecInt2(i, 1))
        game.addPieceToBoard(makePawnAbilities(false, VecInt2(8, 8), queen1), 1, 1, "P", 1f, VecInt2(i, 6))
    }

    game.loseCondition = makePieceCaptureLoseCondition(listOf(listOf(king0.typeID), listOf(king1.typeID)))
    return game
}