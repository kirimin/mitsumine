package me.kirimin.mitsumine._common.domain.model

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table

@Table(name = "account")
open class Account() : Model() {

    constructor(token: String, tokenSecret: String, urlName: String, displayName: String, imageUrl: String) : this() {
        this.token = token
        this.tokenSecret = tokenSecret
        this.urlName = urlName
        this.displayName = displayName
        this.imageUrl = imageUrl
    }

    @Column(name = "token")
    open var token: String = ""

    @Column(name = "tokenSecret")
    open var tokenSecret: String = ""

    @Column(name = "url_name")
    open var urlName: String = ""

    @Column(name = "display_name")
    open var displayName: String = ""

    @Column(name = "profile_image_name")
    open var imageUrl: String = ""
}
