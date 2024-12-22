package com.newsperform.warriorgame.domain.usecase

import com.newsperform.warriorgame.domain.factory.WarriorFactory
import com.newsperform.warriorgame.domain.model.WarriorAttributes
import com.newsperform.warriorgame.domain.model.WarriorType
import com.newsperform.warriorgame.domain.repository.WarriorRepository
import javax.inject.Inject

class AddWarriorUseCase @Inject constructor(
    private val repository: WarriorRepository,
    private val warriorFactory: WarriorFactory,
) {
    suspend operator fun invoke(params: Params): Result<Unit> = runCatching {
        val warrior = warriorFactory.createWarrior(
            type = params.type,
            name = params.name,
            stats = params.stats
        )
        repository.addWarrior(warrior)
    }

    data class Params(
        val type: WarriorType,
        val name: String,
        val stats: WarriorAttributes
    )
}