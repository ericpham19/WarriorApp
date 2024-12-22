package com.newsperform.warriorgame.domain.factory

import com.newsperform.warriorgame.domain.model.Warrior
import com.newsperform.warriorgame.domain.model.WarriorAttributes
import com.newsperform.warriorgame.domain.model.WarriorType

fun interface WarriorFactory {
    fun createWarrior(
        type: WarriorType,
        name: String,
        stats: WarriorAttributes
    ): Warrior
}

class WarriorFactoryImp() : WarriorFactory {
    override fun createWarrior(
        type: WarriorType,
        name: String,
        stats: WarriorAttributes
    ): Warrior {
        return when (type) {
            WarriorType.KNIGHT -> Warrior.Knight(
                name = name,
                strength = stats.extraStat,
                hp = stats.hp
            )

            WarriorType.HUNTER -> Warrior.Hunter(
                name = name,
                dexterity = stats.extraStat,
                hp = stats.hp
            )

            WarriorType.WIZARD -> Warrior.Wizard(
                name = name,
                intelligence = stats.extraStat,
                mp = stats.mp ?: 0,
                hp = stats.hp
            )
        }
    }
}