package jonahshader.systems.engine

import jonahshader.systems.math.VecInt2

typealias ActionPos = VecInt2

// assuming desired action was either verified by
// ActionValid or it came from GetAlloValidActions
typealias Action = (Board, ActionPos, Piece) -> Unit
typealias ActionValid = (Board, ActionPos, Piece) -> Boolean
typealias GetAllValidActionPos = (Board, Piece) -> List<ActionPos>
typealias SpecificAction = (Unit) -> Unit
data class Ability(val action: Action, val actionValid: ActionValid, val getAllValidActionPos: GetAllValidActionPos) {
    fun makeSpecificAction(board: Board, actionPos: ActionPos, piece: Piece): SpecificAction = {action(board, actionPos, piece)}

    companion object {
        // e.g. the knight "jumps" over pieces. bishops do not.
        fun makeJumpMoveAbility(kernel: Kernel): Ability {
            val action: Action = { board, actionPos, piece ->
                board.movePiece(piece, actionPos)
            }

            val actionValid: ActionValid = {board, actionPos, piece ->
                board.posIsOnBoard(actionPos) &&
                board.getPiece(actionPos) == null &&
                kernel.offsets.contains(actionPos - piece.pos)
            }

            val getAllValidActionPos: GetAllValidActionPos = { board, piece ->
                kernel.makeGlobalPosList(piece.pos).filter {
                    board.posIsOnBoard(it) && board.getPiece(it) == null
                }
            }

            return Ability(action, actionValid, getAllValidActionPos)
        }

        fun makeJumpCaptureAbility(kernel: Kernel): Ability {
            val action: Action = { board, actionPos, piece ->
                board.movePiece(piece, actionPos)
            }

            val actionValid: ActionValid = {board, actionPos, piece ->
                board.posIsOnBoard(actionPos) &&
                        board.getPiece(actionPos) != null &&
                        !board.getPiece(actionPos)!!.isOnSameTeam(piece) &&
                        kernel.offsets.contains(actionPos - piece.pos)
            }

            val getAllValidActionPos: GetAllValidActionPos = { board, piece ->
                kernel.makeGlobalPosList(piece.pos).filter { board.posIsOnBoard(it) && board.getPiece(it) != null
                        && !board.getPiece(it)!!.isOnSameTeam(piece) }
            }

            return Ability(action, actionValid, getAllValidActionPos)
        }

        fun makeSlideMoveAbility(kernels: List<Kernel>): Ability {
            val action: Action = { board, actionPos, piece ->
                board.movePiece(piece, actionPos)
            }

            val actionValid: ActionValid = {board, actionPos, piece ->
                val relativePos = actionPos - piece.pos
                var kernel: Kernel? = null
                for (k in kernels) {
                    if (k.offsets.contains(relativePos)) {
                        kernel = k
                        break
                    }
                }
                var valid = false
                if (board.posIsOnBoard(actionPos) && kernel != null) {
                    for (o in kernel.offsets) {
                        if (board.getPiece(o + piece.pos) != null) break
                        if (o == relativePos) {
                            valid = true
                        }
                    }
                }
                valid
            }

            val getAllValidActionPos: GetAllValidActionPos = { board, piece ->
                val validActionPositions = mutableListOf<ActionPos>()
                kernels.forEach { kernel ->
                    for (p in kernel.makeGlobalPosList(piece.pos)) {
                        if (!(board.posIsOnBoard(p) && board.getPiece(p) == null)) break
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
