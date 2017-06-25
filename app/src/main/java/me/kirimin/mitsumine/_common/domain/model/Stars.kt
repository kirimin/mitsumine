package me.kirimin.mitsumine._common.domain.model

import me.kirimin.mitsumine._common.network.entity.StarOfBookmarkResponse
import java.io.Serializable

class Stars(response: StarOfBookmarkResponse? = null) : Serializable {

    val yellowStarsCount = response?.entries?.get(0)?.stars?.size ?: 0
    val allStarsCount get() = yellowStarsCount
}