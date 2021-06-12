package jonahshader.systems.engine

typealias LoseCondition = (BoardGame) -> Int // output is winner, -1 for no win yet
typealias DrawCondition = (BoardGame) -> Boolean


val noMoveDraw: DrawCondition = {
    var draw = false
    it.players.forEach { player ->
        if (player.getAllMoves().isEmpty()) {
            draw = true
            return@forEach
        }
    }
    draw
}

fun makeOrDraws(draws: List<DrawCondition>): DrawCondition = {
    var output = false
    loop@
    for (d in draws) if (d(it)) {
        output = true
        break@loop
    }
    output
}

fun makeMoveLimitDraw(limit: Int): DrawCondition = {
    it.totalMoves >= limit
}


val noDraw: DrawCondition = {false}

val missingAllPiecesLoseCondition: LoseCondition = {
    var loser = -1
    it.players.forEachIndexed { index, player ->
        if (player.pieces.isEmpty()) {
            loser = index
            return@forEachIndexed
        }
    }
    loser
}

// if any pieces in this list is missing, that player loses
fun makePieceCaptureLoseCondition(playerPieces: List<List<Int>>): LoseCondition = { boardGame ->
    var loser = -1
    playerPieces.forEachIndexed { index, player ->
        player.forEach { typeID ->
            if (0 == boardGame.players[index].pieces.fold(0) { acc: Int, piece: Piece -> acc + if (piece.typeID == typeID) 1 else 0 }) {
                loser = index
                return@forEachIndexed
            }
        }
    }
    loser
}



// TODO: make checkmate win condition. too complicated to shit out on the spot
// i guess i can iterate over all legal moves for the "king" and then loop
// through all opponent moves to see if there is a way to capture the king
// in all king move variations