package me.kirimin.mitsumine.common.database

import com.activeandroid.query.Select

import me.kirimin.mitsumine.common.domain.model.Account

public class AccountDAO {
    companion object {

        public fun save(account: Account) {
            account.save()
        }

        public fun delete() {
            Select().from(Account::class.java).executeSingle<Account>()?.delete()
        }


        public fun get(): Account? {
            return Select().from(Account::class.java).executeSingle<Account>()
        }
    }
}
