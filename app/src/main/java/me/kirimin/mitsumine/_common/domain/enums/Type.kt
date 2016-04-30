package me.kirimin.mitsumine._common.domain.enums

enum class Type(private val TEXT: String) {
    HOT("hotentry"),
    NEW("entrylist");

    override fun toString() = TEXT
}