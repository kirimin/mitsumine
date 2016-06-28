package me.kirimin.mitsumine._common.domain.model

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table

@Table(name = "account")
class Account() : Model() {

    constructor(token: String, tokenSecret: String, urlName: String, displayName: String, imageUrl: String) : this() {
        this.token = token
        this.tokenSecret = tokenSecret
        this.urlName = urlName
        this.displayName = displayName
        this.imageUrl = imageUrl
    }

    @Column(name = "token")
    var token: String = ""

    @Column(name = "tokenSecret")
    var tokenSecret: String = ""

    @Column(name = "url_name")
    var urlName: String = ""

    @Column(name = "display_name")
    var displayName: String = ""

    @Column(name = "profile_image_name")
    var imageUrl: String = ""
}
