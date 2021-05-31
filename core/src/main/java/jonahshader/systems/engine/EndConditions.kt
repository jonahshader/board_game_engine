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
fun makePieceCaptureLoseCondition(playerPieces: List<List<Piece>>): LoseCondition = { boardGame ->
    var loser = -1
    playerPieces.forEachIndexed { index, player ->
        player.forEach { piece ->
            if (!boardGame.players[index].pieces.contains(piece)) {
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