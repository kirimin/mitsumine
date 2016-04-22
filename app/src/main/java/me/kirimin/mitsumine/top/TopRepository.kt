package me.kirimin.mitsumine.top

import me.kirimin.mitsumine.common.database.AccountDAO
import me.kirimin.mitsumine.common.database.FeedDAO
import me.kirimin.mitsumine.common.database.KeywordDAO
import me.kirimin.mitsumine.common.database.UserIdDAO
import me.kirimin.mitsumine.common.domain.model.Account

open class TopRepository {

    open val account: Account?
        get() = AccountDAO.get()

    open val additionKeywords: List<String>
        get() = KeywordDAO.findAll()

    open val additionUsers: List<String>
        get() = UserIdDAO.findAll()

    open fun deleteAdditionUser(userId: String) {
        UserIdDAO.delete(userId)
    }

    open fun deleteAdditionKeyword(keyword: String) {
        KeywordDAO.delete(keyword)
    }

    open fun deleteOldFeedData(days: Int) {
        FeedDAO.deleteOldData(days)
    }
}