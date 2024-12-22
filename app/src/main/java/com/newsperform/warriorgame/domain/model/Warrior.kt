package com.newsperform.warriorgame.domain.model

import java.util.UUID

enum class WarriorType {
    KNIGHT,
    HUNTER,
    WIZARD;

    companion object {
        fun fromString(type: String): WarriorType {
            return when (type.uppercase()) {
                "KNIGHT" -> KNIGHT
                "HUNTER" -> HUNTER
                "WIZARD" -> WIZARD
                else -> throw IllegalArgumentException("Unknown warrior type: $type")
            }
        }
    }
}

sealed class Warrior(
    open val id: String = UUID.randomUUID().toString(),
    open val name: String,
    open val style: String,
    open val hp: Int,
    open val type: WarriorType
) {
    data class Knight(
        override val id: String = UUID.randomUUID().toString(),
        override val name: String,
        val strength: Int,
        override val hp: Int
    ) : Warrior(
        id = id,
        name = name,
        style = "Melee",
        hp = hp,
        type = WarriorType.KNIGHT
    )

    data class Hunter(
        override val id: String = UUID.randomUUID().toString(),
        override val name: String,
        val dexterity: Int,
        override val hp: Int
    ) : Warrior(
        id = id,
        name = name,
        style = "Range",
        hp = hp,
        type = WarriorType.HUNTER
    )

    data class Wizard(
        override val id: String = UUID.randomUUID().toString(),
        override val name: String,
        val intelligence: Int,
        val mp: Int,
        override val hp: Int
    ) : Warrior(
        id = id,
        name = name,
        style = "Semi-Range",
        hp = hp,
        type = WarriorType.WIZARD
    )
}