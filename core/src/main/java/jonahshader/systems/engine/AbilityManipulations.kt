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