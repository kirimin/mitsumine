package me.kirimin.mitsumine.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "account")
public class Account extends Model {

    @Column(name = "token")
    public String token;

    @Column(name = "tokenSecret")
    public String tokenSecret;

    @Column(name = "url_name")
    public String urlName;

    @Column(name = "display_name")
    public String displayName;

    @Column(name = "profile_image_name")
    public String imageUrl;
}
