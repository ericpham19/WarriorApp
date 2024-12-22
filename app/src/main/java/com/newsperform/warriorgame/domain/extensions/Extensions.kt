package com.newsperform.warriorgame.domain.extensions

import com.newsperform.warriorgame.domain.model.Warrior
import com.newsperform.warriorgame.domain.model.WarriorType

fun Warrior.getWarriorType(): WarriorType {
    return when (this) {
        is Warrior.Knight -> WarriorType.KNIGHT
        is Warrior.Hunter -> WarriorType.HUNTER
        is Warrior.Wizard -> WarriorType.WIZARD
    }
}
