package me.kirimin.mitsumine.top

import me.kirimin.mitsumine._common.database.AccountDAO
import me.kirimin.mitsumine._common.database.FeedDAO
import me.kirimin.mitsumine._common.database.KeywordDAO
import me.kirimin.mitsumine._common.database.UserIdDAO
import me.kirimin.mitsumine._common.domain.model.Account

 class TopUseCase {

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

     fun deleteOldFeedData(days: Int) {
        FeedDAO.deleteOldData(days)
    }
}