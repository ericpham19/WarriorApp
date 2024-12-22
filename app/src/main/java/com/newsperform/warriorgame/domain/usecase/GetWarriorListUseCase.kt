package com.newsperform.warriorgame.domain.usecase

import com.newsperform.warriorgame.domain.model.Warrior
import com.newsperform.warriorgame.domain.repository.WarriorRepository
import javax.inject.Inject

class GetWarriorsUseCase @Inject constructor(
    private val repository: WarriorRepository
) {
    suspend operator fun invoke(): Result<List<Warrior>> = runCatching {
        repository.getWarriors()
    }
}