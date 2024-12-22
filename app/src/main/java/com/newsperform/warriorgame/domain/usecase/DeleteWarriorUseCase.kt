package com.newsperform.warriorgame.domain.usecase

import com.newsperform.warriorgame.domain.repository.WarriorRepository
import javax.inject.Inject

class DeleteWarriorUseCase @Inject constructor(
    private val repository: WarriorRepository
) {
    suspend operator fun invoke(params: Params) : Result<Unit> = runCatching {
        repository.deleteWarriro(warriorId = params.id)
    }

    data class Params(
        val id: String
    )
}