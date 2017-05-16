package me.kirimin.mitsumine._common.network.entity

import com.google.gson.annotations.SerializedName

data class StarOfBookmarkResponse(val entries: List<EntryResponse>) {

    class EntryResponse(
            val uri: String,
            val stars: List<StarResponse>,
            @SerializedName("colored_stars")
            val coloredStars: List<ColoredStarResponse>
    )

    class StarResponse(
            val name: String,
            val quote: String
    )

    class ColoredStarResponse(
            val color: ColorResponse,
            val stars: List<StarResponse>
    )

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