package me.kirimin.mitsumine.top

import me.kirimin.mitsumine.top.TopData
import me.kirimin.mitsumine.common.domain.model.Account
import me.kirimin.mitsumine.common.domain.enums.Type

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
        data.deleteOldFeedData(3)
    }

    fun getTypeInt(type: Type): Int {
        return if (type == Type.HOT) 0 else 1
    }
}
