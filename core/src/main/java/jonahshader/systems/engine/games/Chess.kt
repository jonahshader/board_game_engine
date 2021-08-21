package jonahshader.systems.engine.games

import jonahshader.systems.engine.*
import jonahshader.systems.engine.abilities.*
import jonahshader.systems.engine.playercontrollers.PlayerController
import jonahshader.systems.math.VecInt2
import kotlin.math.pow

fun makeChessGame(player1Controller: PlayerController, player2Controller: PlayerController, moveTime: Float = .25f, moveLimit: Int = 200): BoardGame {
    val boardSize = VecInt2(8, 8)
    val game = BoardGame(boardSize, listOf(player1Controller, player2Controller), moveTime, drawCondition = makeOrDraws(listOf(
        noMoveDraw, makeMoveLimitDraw(moveLimit)
    )))

    val queenAbilities = makeQueenAbilities(8)
    val rookAbilities = makeRookAbilities(8)
    val bishopAbilities = makeBishopAbilities(8)
    val knightAbilities = makeKnightAbilities()
    val kingAbilities = makeKingAbilities()


    val rookValueFun = makeRookValueFun()
    val knightValueFun = makeKnightValueFun(boardSize)
    val bishopValueFun = makeBishopValueFun()
    val queenValueFun = makeQueenValueFun()
    val kingValueFun = makeKingValueFun()
    val whitePawnValueFun = makePawnValueFun(true, 8)
    val blackPawnValueFun = makePawnValueFun(false, 8)


    game.addPieceToBoard(rookAbilities, 0, 2, "R", rookValueFun, VecInt2(0, 0))
    game.addPieceToBoard(rookAbilities, 0, 2, "R", rookValueFun, VecInt2(7, 0))
    game.addPieceToBoard(rookAbilities, 1, 3, "R", rookValueFun, VecInt2(0, 7))
    game.addPieceToBoard(rookAbilities, 1, 3, "R", rookValueFun, VecInt2(7, 7))
    game.addPieceToBoard(knightAbilities, 0, 4, "N", knightValueFun, VecInt2(1, 0))
    game.addPieceToBoard(knightAbilities, 0, 4, "N", knightValueFun, VecInt2(6, 0))
    game.addPieceToBoard(knightAbilities, 1, 5, "N", knightValueFun, VecInt2(1, 7))
    game.addPieceToBoard(knightAbilities, 1, 5, "N", knightValueFun, VecInt2(6, 7))
    game.addPieceToBoard(bishopAbilities, 0, 6, "B", bishopValueFun, VecInt2(2, 0))
    game.addPieceToBoard(bishopAbilities, 0, 6, "B", bishopValueFun, VecInt2(5, 0))
    game.addPieceToBoard(bishopAbilities, 1, 7, "B", bishopValueFun, VecInt2(2, 7))
    game.addPieceToBoard(bishopAbilities, 1, 7, "B", bishopValueFun, VecInt2(5, 7))
    val queen0: Piece = game.addPieceToBoard(queenAbilities, 0, 8, "Q", queenValueFun, VecInt2(3, 0))
    val king0 = game.addPieceToBoard(kingAbilities, 0, 9, "K", kingValueFun, VecInt2(4, 0))
    val queen1 = game.addPieceToBoard(queenAbilities, 1, 10, "Q", queenValueFun, VecInt2(3, 7))
    val king1 = game.addPieceToBoard(kingAbilities, 1, 11, "K", kingValueFun, VecInt2(4, 7))

    for (i in 0 until 8) {
        game.addPieceToBoard(makePawnAbilities(true, VecInt2(8, 8), queen0), 0, 0, "P",  whitePawnValueFun,VecInt2(i, 1))
        game.addPieceToBoard(makePawnAbilities(false, VecInt2(8, 8), queen1), 1, 1, "P", blackPawnValueFun, VecInt2(i, 6))
    }

    game.loseCondition = makePieceCaptureLoseCondition(listOf(listOf(king0.typeID), listOf(king1.typeID)))
    return game
}