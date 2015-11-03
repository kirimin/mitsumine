package me.kirimin.mitsumine.domain.usecase

import me.kirimin.mitsumine.data.TopData
import me.kirimin.mitsumine.model.Account
import me.kirimin.mitsumine.model.enums.Type

open class TopUseCase {

    val data: TopData

    constructor(topData: TopData) {
        this.data = topData
    }

    open val additionUsers: List<String>
        get() = data.additionUsers

    open fun deleteAdditionUser(userId: String) {
        data.deleteAdditionUser(userId)
    }

    open val additionKeywords: List<String>
        get() = data.additionKeywords

    open fun deleteAdditionKeyword(keyword: String) {
        data.deleteAdditionKeyword(keyword)
    }

    open val account: Account?
        get() = data.account

    open fun deleteOldFeedData() {
        val threeDays = 1000 * 60 * 60 * 24 * 3.toLong()
        data.deleteOldFeedData(threeDays)
    }

    fun getTypeInt(type: Type): Int {
        return if (type == Type.HOT) 0 else 1
    }
}
