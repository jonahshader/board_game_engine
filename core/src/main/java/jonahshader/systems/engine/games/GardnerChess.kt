package jonahshader.systems.engine.games

import jonahshader.systems.engine.*
import jonahshader.systems.engine.abilities.*
import jonahshader.systems.engine.playercontrollers.PlayerController
import jonahshader.systems.math.VecInt2

fun makeGardnerChessGame(player1Controller: PlayerController, player2Controller: PlayerController, moveTime: Float = .25f, moveLimit: Int = 200): BoardGame {
    val boardSize = VecInt2(5, 5)
    val game = BoardGame(VecInt2(5, 5), listOf(player1Controller, player2Controller), moveTime, drawCondition = makeOrDraws(listOf(
        noMoveDraw, makeMoveLimitDraw(moveLimit))))

    val queenAbilities = makeQueenAbilities(5)
    val rookAbilities = makeRookAbilities(5)
    val bishopAbilities = makeBishopAbilities(5)
    val knightAbilities = makeKnightAbilities()
    val kingAbilities = makeKingAbilities()

    val rookValueFun = makeRookValueFun()
    val knightValueFun = makeKnightValueFun(boardSize)
    val bishopValueFun = makeBishopValueFun()
    val queenValueFun = makeQueenValueFun()
    val kingValueFun = makeKingValueFun()
    val whitePawnValueFun = makePawnValueFun(true, 5)
    val blackPawnValueFun = makePawnValueFun(false, 5)


    game.addPieceToBoard(rookAbilities, 0, 2, "R", rookValueFun, VecInt2(0, 0))
    game.addPieceToBoard(rookAbilities, 1, 3, "R", rookValueFun, VecInt2(0, 4))
    game.addPieceToBoard(knightAbilities, 0, 4, "N", knightValueFun, VecInt2(1, 0))
    game.addPieceToBoard(knightAbilities, 1, 5, "N", knightValueFun, VecInt2(1, 4))
    game.addPieceToBoard(bishopAbilities, 0, 6, "B", bishopValueFun, VecInt2(2, 0))
    game.addPieceToBoard(bishopAbilities, 1, 7, "B", bishopValueFun, VecInt2(2, 4))
    val queen0 = game.addPieceToBoard(queenAbilities, 0, 8, "Q", queenValueFun, VecInt2(3, 0))
    val king0 = game.addPieceToBoard(kingAbilities, 0, 9, "K", kingValueFun, VecInt2(4, 0))
    val queen1 = game.addPieceToBoard(queenAbilities, 1, 10, "Q", queenValueFun, VecInt2(3, 4))
    val king1 = game.addPieceToBoard(kingAbilities, 1, 11, "K", kingValueFun, VecInt2(4, 4))

    val whitePawnAbilities = makePawnAbilities(true, VecInt2(5, 5), queen0)
    val blackPawnAbilities = makePawnAbilities(false, VecInt2(5, 5), queen1)

    for (i in 0 until 5) {
        game.addPieceToBoard(whitePawnAbilities, 0, 0, "P", whitePawnValueFun, VecInt2(i, 1))
        game.addPieceToBoard(blackPawnAbilities, 1, 1, "P", blackPawnValueFun, VecInt2(i, 3))
    }

    game.loseCondition = makePieceCaptureLoseCondition(listOf(listOf(king0.typeID), listOf(king1.typeID)))
    return game
}