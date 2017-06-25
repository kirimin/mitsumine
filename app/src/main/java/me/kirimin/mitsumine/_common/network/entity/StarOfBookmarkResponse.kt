package me.kirimin.mitsumine._common.network.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StarOfBookmarkResponse(val entries: List<EntryResponse>): Serializable {

    class EntryResponse(
            val uri: String,
            val stars: List<StarResponse>,
            @SerializedName("colored_stars")
            val coloredStars: List<ColoredStarResponse>
    ): Serializable

    class StarResponse(
            val name: String,
            val quote: String
    ): Serializable

    class ColoredStarResponse(
            val color: ColorResponse,
            val stars: List<StarResponse>
    ): Serializable

    enum class ColorResponse {
        @SerializedName("green")
        GREEN,
        @SerializedName("red")
        RED,
        @SerializedName("blue")
        BLUE,
        @SerializedName("purple")
        PURPLE
    }
}