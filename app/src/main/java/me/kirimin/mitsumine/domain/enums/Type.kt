package me.kirimin.mitsumine.domain.enums

public enum class Type private constructor(private val TEXT: String) {
    HOT("hotentry"),
    NEW("entrylist");

    override fun toString(): String {
        return TEXT
    }
}