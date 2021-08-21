package jonahshader.systems.engine

fun changeAbilityAfterMove(initialAbility: Ability, nextAbility: Ability): Ability {
    val moddedAbility: Ability
    val moddedAction: Action = {game, pos, piece, ability ->
        initialAbility.action(game, pos, piece, ability)
        ability.action = nextAbility.action
        ability.actionValid = nextAbility.actionValid
        ability.getAllValidActionPos = nextAbility.getAllValidActionPos
    }
    moddedAbility = Ability(moddedAction, initialAbility.actionValid, initialAbility.getAllValidActionPos)
    return moddedAbility
}

fun changePieceAtRow(initialAbility: Ability, nextPiece: Piece, row: Int): Ability {
    val moddedAbility: Ability
    val moddedAction: Action = {game, pos, piece, ability ->
        initialAbility.action(game, pos, piece, ability)
        if (pos.y == row) {
            piece.abilities.clear()
            piece.abilities += nextPiece.abilities
            piece.typeID = nextPiece.typeID
            piece.symbol = nextPiece.symbol
            piece.valueFun = nextPiece.valueFun
        }
    }
    moddedAbility = Ability(moddedAction, initialAbility.actionValid, initialAbility.getAllValidActionPos)
    return moddedAbility
}