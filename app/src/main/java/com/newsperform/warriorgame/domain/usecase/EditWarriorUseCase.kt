package com.newsperform.warriorgame.domain.usecase

import com.newsperform.warriorgame.domain.factory.WarriorFactory
import com.newsperform.warriorgame.domain.model.Warrior
import com.newsperform.warriorgame.domain.model.WarriorAttributes
import com.newsperform.warriorgame.domain.model.WarriorType
import com.newsperform.warriorgame.domain.repository.WarriorRepository
import javax.inject.Inject

class EditWarriorUseCase @Inject constructor(
    private val repository: WarriorRepository,
    private val warriorFactory: WarriorFactory
) {
    suspend operator fun invoke(params: Params): Result<Unit> = runCatching {
        // Create the WarriorAttributes based on the warrior type
        val attributes = when (params.type) {
            WarriorType.KNIGHT, WarriorType.HUNTER -> WarriorAttributes(
                hp = params.hp,
                extraStat = params.extraStat
            )
            WarriorType.WIZARD -> WarriorAttributes(
                hp = params.hp,
                mp = params.mp,
                extraStat = params.extraStat
            )
        }

        // Create updated warrior using the factory
        val updatedWarrior = when (val newWarrior = warriorFactory.createWarrior(
            type = params.type,
            name = params.name,
            stats = attributes
        )) {
            is Warrior.Knight -> newWarrior.copy(id = params.id)
            is Warrior.Hunter -> newWarrior.copy(id = params.id)
            is Warrior.Wizard -> newWarrior.copy(id = params.id)
        }

        repository.editWarrior(updatedWarrior)
    }

    data class Params(
        val id: String,
        val type: WarriorType,
        val name: String,
        val hp: Int,
        val extraStat: Int,
        val mp: Int? = null  // Only used for Wizard
    )
}