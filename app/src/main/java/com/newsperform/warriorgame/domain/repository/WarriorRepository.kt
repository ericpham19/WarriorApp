package com.newsperform.warriorgame.domain.repository

import com.newsperform.warriorgame.domain.model.Warrior

interface WarriorRepository {
    suspend fun addWarrior(warrior: Warrior)
    suspend fun editWarrior(warrior: Warrior)
    suspend fun deleteWarriro(warriorId: String)
    suspend fun getWarriors(): List<Warrior>
}