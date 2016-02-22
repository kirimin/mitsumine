package me.kirimin.mitsumine.common.domain.model

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table

@Table(name = "account")
public class Account : Model() {

    @Column(name = "token")
    public var token: String = ""

    @Column(name = "tokenSecret")
    public var tokenSecret: String = ""

    @Column(name = "url_name")
    public var urlName: String = ""

    @Column(name = "display_name")
    public var displayName: String = ""

    @Column(name = "profile_image_name")
    public var imageUrl: String = ""
}
