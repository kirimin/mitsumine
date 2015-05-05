package me.kirimin.mitsumine.db

import com.activeandroid.query.Select

import me.kirimin.mitsumine.model.Account

public class AccountDAO {
    companion object {

        public fun save(account: Account) {
            account.save()
        }

        public fun delete() {
            val account = Select().from(javaClass<Account>()).executeSingle<Account>()
            if (account != null) {
                account.delete()
            }
        }


        public fun get(): Account? {
            return Select().from(javaClass<Account>()).executeSingle<Account>()
        }
    }
}
