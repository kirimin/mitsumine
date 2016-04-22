package me.kirimin.mitsumine.common.domain.enums

enum class Type(private val TEXT: String) {
    HOT("hotentry"),
    NEW("entrylist");

    override fun toString() = TEXT
}