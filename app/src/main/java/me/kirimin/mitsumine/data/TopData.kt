package me.kirimin.mitsumine.data

import me.kirimin.mitsumine.data.database.AccountDAO
import me.kirimin.mitsumine.data.database.FeedDAO
import me.kirimin.mitsumine.data.database.KeywordDAO
import me.kirimin.mitsumine.data.database.UserIdDAO
import me.kirimin.mitsumine.domain.model.Account

class TopData {

    val account: Account?
        get() = AccountDAO.get()

    val additionKeywords: List<String>
        get() = KeywordDAO.findAll()

    val additionUsers: List<String>
        get() = UserIdDAO.findAll()

    fun deleteAdditionUser(userId: String) {
        UserIdDAO.delete(userId)
    }

    fun deleteAdditionKeyword(keyword: String) {
        KeywordDAO.delete(keyword)
    }

    fun deleteOldFeedData(days: Long) {
        FeedDAO.deleteOldData(days)
    }
}