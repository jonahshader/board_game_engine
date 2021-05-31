package jonahshader.systems.engine

import jonahshader.systems.math.VecInt2

typealias ActionPos = VecInt2

// assuming desired action was either verified by
// ActionValid or it came from GetAlloValidActions
typealias Action = (BoardGame, ActionPos, Piece) -> Unit
typealias ActionValid = (BoardGame, ActionPos, Piece) -> Boolean
typealias GetAllValidActionPos = (BoardGame, Piece) -> List<ActionPos>
typealias SpecificAction = (Unit) -> Unit
data class Ability(val action: Action, val actionValid: ActionValid, val getAllValidActionPos: GetAllValidActionPos) {
    fun makeSpecificAction(game: BoardGame, actionPos: ActionPos, piece: Piece): SpecificAction = {action(game, actionPos, piece)}

    companion object {
        // e.g. the knight "jumps" over pieces. bishops do not.
        fun makeJumpMoveAbility(kernel: Kernel): Ability {
            val action: Action = { game, actionPos, piece ->
                game.board.movePiece(piece, actionPos)
            }

            val actionValid: ActionValid = {game, actionPos, piece ->
                game.board.posIsOnBoard(actionPos) &&
                        game.board.getPiece(actionPos) == null &&
                kernel.offsets.contains(actionPos - piece.pos)
            }

            val getAllValidActionPos: GetAllValidActionPos = { game, piece ->
                kernel.makeGlobalPosList(piece.pos).filter {
                    game.board.posIsOnBoard(it) && game.board.getPiece(it) == null
                }
            }

            return Ability(action, actionValid, getAllValidActionPos)
        }

        fun makeJumpCaptureAbility(kernel: Kernel): Ability {
            val action: Action = { game, actionPos, piece ->
//                game.board.movePiece(piece, actionPos)
//                game
                game.capturePiece(piece, actionPos)
            }

            val actionValid: ActionValid = {game, actionPos, piece ->
                game.board.posIsOnBoard(actionPos) &&
                        game.board.getPiece(actionPos) != null &&
                        !game.board.getPiece(actionPos)!!.isOnSameTeam(piece) &&
                        kernel.offsets.contains(actionPos - piece.pos)
            }

            val getAllValidActionPos: GetAllValidActionPos = { game, piece ->
                kernel.makeGlobalPosList(piece.pos).filter { game.board.posIsOnBoard(it) && game.board.getPiece(it) != null
                        && !game.board.getPiece(it)!!.isOnSameTeam(piece) }
            }

            return Ability(action, actionValid, getAllValidActionPos)
        }

        fun makeSlideMoveAbility(kernels: List<Kernel>): Ability {
            val action: Action = { game, actionPos, piece ->
                game.board.movePiece(piece, actionPos)
            }

            val actionValid: ActionValid = {game, actionPos, piece ->
                val relativePos = actionPos - piece.pos
                var kernel: Kernel? = null
                for (k in kernels) {
                    if (k.offsets.contains(relativePos)) {
                        kernel = k
                        break
                    }
                }
                var valid = false
                if (game.board.posIsOnBoard(actionPos) && kernel != null) {
                    for (o in kernel.offsets) {
                        if (game.board.getPiece(o + piece.pos) != null) break
                        if (o == relativePos) {
                            valid = true
                        }
                    }
                }
                valid
            }

            val getAllValidActionPos: GetAllValidActionPos = { game, piece ->
                val validActionPositions = mutableListOf<ActionPos>()
                kernels.forEach { kernel ->
                    for (p in kernel.makeGlobalPosList(piece.pos)) {
                        if (!(game.board.posIsOnBoard(p) && game.board.getPiece(p) == null)) break
                        validActionPositions += p
                    }
                }
                validActionPositions
            }

            return Ability(action, actionValid, getAllValidActionPos)
        }

//        fun makeSlideCaptureAbility(kernels: List<Kernel>): Ability {
//            val action: Action = { board, actionPos, piece ->
//                board.movePiece(piece, actionPos)
//            }
//        }
    }
}
