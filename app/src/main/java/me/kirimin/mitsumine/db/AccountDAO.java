package me.kirimin.mitsumine.db;

import com.activeandroid.query.Select;

import me.kirimin.mitsumine.model.Account;

public class AccountDAO {

    public static void save(Account account) {
        account.save();
    }

    public static Account get() {
        return new Select().from(Account.class).executeSingle();
    }
}
