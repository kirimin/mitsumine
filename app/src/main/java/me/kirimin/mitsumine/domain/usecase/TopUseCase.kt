package me.kirimin.mitsumine.domain.usecase

import me.kirimin.mitsumine.data.database.AccountDAO
import me.kirimin.mitsumine.data.database.FeedDAO
import me.kirimin.mitsumine.data.database.KeywordDAO
import me.kirimin.mitsumine.data.database.UserIdDAO
import me.kirimin.mitsumine.model.Account
import me.kirimin.mitsumine.model.enums.Type

open class TopUseCase {

    open val additionUsers: List<String>
        get() = UserIdDAO.findAll()

    open fun deleteAdditionUser(userId: String) {
        UserIdDAO.delete(userId)
    }

    open val additionKeywords: List<String>
        get() = KeywordDAO.findAll()

    open fun deleteAdditionKeyword(keyword: String) {
        KeywordDAO.delete(keyword)
    }

    open fun getAccount(): Account? {
        return AccountDAO.get()
    }

    open fun deleteOldFeedData() {
        val threeDays = 1000 * 60 * 60 * 24 * 3.toLong()
        FeedDAO.deleteOldData(threeDays)
    }

    open fun getTypeInt(type: Type): Int {
        return if (type == Type.HOT) 0 else 1
    }
}
