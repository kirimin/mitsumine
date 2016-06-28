package me.kirimin.mitsumine._common.database

import com.activeandroid.query.Select
import me.kirimin.mitsumine._common.domain.model.Account

object AccountDAO {

    fun save(account: Account) {
        account.save()
    }

    fun delete() {
        Select().from(Account::class.java).executeSingle<Account>()?.delete()
    }


    fun get(): Account? {
        return Select().from(Account::class.java).executeSingle<Account>()
    }
}
