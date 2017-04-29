package me.kirimin.mitsumine._common.domain.model

import me.kirimin.mitsumine._common.network.entity.StarOfBookmarkResponse
import java.io.Serializable

class Star(val response: StarOfBookmarkResponse) : Serializable {

    val yellowStarsCount = response.entries[0].stars.size
    val allStarsCount get() = yellowStarsCount
}