package me.kirimin.mitsumine.domain.usecase;

import java.util.List;

import me.kirimin.mitsumine.data.database.AccountDAO;
import me.kirimin.mitsumine.data.database.FeedDAO;
import me.kirimin.mitsumine.data.database.KeywordDAO;
import me.kirimin.mitsumine.data.database.UserIdDAO;
import me.kirimin.mitsumine.model.Account;

public class TopUseCase {

    public List<String> getAdditionUsers() {
        return UserIdDAO.Companion.findAll();
    }

    public void deleteAdditionUser(String userId) {
        UserIdDAO.Companion.delete(userId);
    }

    public List<String> getAdditionKeywords() {
        return KeywordDAO.Companion.findAll();
    }

    public void deleteAdditionKeyword(String keyword) {
        KeywordDAO.Companion.delete(keyword);
    }

    public Account getAccount() {
        return AccountDAO.Companion.get();
    }

    public void deleteOldFeedData(){
        long threeDays = 1000 * 60 * 60 * 24 * 3;
        FeedDAO.Companion.deleteOldData(threeDays);
    }
}
