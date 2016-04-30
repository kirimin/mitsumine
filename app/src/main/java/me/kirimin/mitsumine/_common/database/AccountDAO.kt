package me.kirimin.mitsumine._common.database

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import com.activeandroid.query.Select

import me.kirimin.mitsumine._common.domain.model.Account

object AccountDAO {

    fun save(account: Account) {
        AccountDBModel(account).save()
    }

    fun delete() {
        Select().from(AccountDBModel::class.java).executeSingle<AccountDBModel>()?.delete()
    }


    fun get(): Account? {
        return Select().from(AccountDBModel::class.java).executeSingle<AccountDBModel>()?.toAccount()
    }

    @Table(name = "account")
    class AccountDBModel(account: Account) : Model() {

        constructor() : this(Account())

        @Column(name = "token")
        var token: String = account.token

        @Column(name = "tokenSecret")
        var tokenSecret: String = account.tokenSecret

        @Column(name = "url_name")
        var urlName: String = account.urlName

        @Column(name = "display_name")
        var displayName: String = account.displayName

        @Column(name = "profile_image_name")
        var imageUrl: String = account.imageUrl

        fun toAccount() = Account(token, tokenSecret, urlName, displayName, imageUrl)
    }

}
