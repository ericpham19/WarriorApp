package com.newsperform.warriorgame.data.repository

import com.newsperform.warriorgame.domain.model.Warrior
import com.newsperform.warriorgame.domain.repository.WarriorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WarriorRepositoryImp @Inject constructor(): WarriorRepository {
    private val warriors = mutableListOf<Warrior>()

    override suspend fun addWarrior(warrior: Warrior) {
        withContext(Dispatchers.IO) {
            warriors.add(warrior)
        }
    }

    override suspend fun editWarrior(warrior: Warrior) {
        withContext(Dispatchers.IO) {
            val index = warriors.indexOfFirst { it.id == warrior.id }
            if (index != -1) {
                warriors[index] = warrior
            } else {
                throw IllegalArgumentException("Warrior with id ${warrior.id} not found")
            }
        }
    }

    override suspend fun deleteWarriro(warriorId: String) {
        withContext(Dispatchers.IO) {
            warriors.removeIf { it.id == warriorId }
        }
    }

    override suspend fun getWarriors(): List<Warrior> {
       return withContext(Dispatchers.IO) {
            warriors.toList()
        }
    }
}