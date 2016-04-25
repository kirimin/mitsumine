package me.kirimin.mitsumine._common.domain.model

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table

class Account(val token: String = "", val tokenSecret: String = "", val urlName: String = "", val displayName: String = "", val imageUrl: String = "")