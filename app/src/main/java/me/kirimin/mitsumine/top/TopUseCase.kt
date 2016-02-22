package me.kirimin.mitsumine.top

import me.kirimin.mitsumine.top.TopRepository
import me.kirimin.mitsumine.common.domain.model.Account
import me.kirimin.mitsumine.common.domain.enums.Type

open class TopUseCase {

    val repository: TopRepository

    constructor(topRepository: TopRepository) {
        this.repository = topRepository
    }

    open val additionUsers: List<String>
        get() = repository.additionUsers

    open fun deleteAdditionUser(userId: String) {
        repository.deleteAdditionUser(userId)
    }

    open val additionKeywords: List<String>
        get() = repository.additionKeywords

    open fun deleteAdditionKeyword(keyword: String) {
        repository.deleteAdditionKeyword(keyword)
    }

    open val account: Account?
        get() = repository.account

    open fun deleteOldFeedData() {
        repository.deleteOldFeedData(3)
    }

    fun getTypeInt(type: Type): Int {
        return if (type == Type.HOT) 0 else 1
    }
}
