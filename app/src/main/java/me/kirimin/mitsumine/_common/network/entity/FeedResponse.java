package me.kirimin.mitsumine._common.network.entity;

import android.support.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * フィードRSSの子要素
 */
@Root(name = "item", strict = false)
public class FeedResponse {

    @Element(name = "title")
    public String title;

    @Element(name = "link")
    public String link;

    @Element(name = "description", required = false)
    @Nullable
    public String description;

    @Nullable
    @Element(name = "encoded", data = true, required = false)
    public String contentEncoded;
}
