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
// TODO: strange bug where all pawns inherit queen ability when only one of them queens.
fun changePieceAtRow(initialAbility: Ability, nextPiece: Piece, row: Int): Ability {
    val moddedAbility: Ability
    val moddedAction: Action = {game, pos, piece, ability ->
        initialAbility.action(game, pos, piece, ability)
        println("ran changePieceAtRow. pos.y: ${pos.y}, row: $row")
        if (pos.y == row) {
            piece.abilities.clear()
            piece.abilities += nextPiece.abilities
            piece.typeID = nextPiece.typeID
            piece.symbol = nextPiece.symbol
            piece.value = nextPiece.value
            println("changed!")
        } else {
            println("unchanged!")
        }
    }
    moddedAbility = Ability(moddedAction, initialAbility.actionValid, initialAbility.getAllValidActionPos)
    return moddedAbility
}