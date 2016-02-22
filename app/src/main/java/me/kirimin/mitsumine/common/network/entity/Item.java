package me.kirimin.mitsumine.common.network.entity;

import android.support.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "item", strict = false)
public class Item {

    @Element(name = "title")
    public String title;

    @Element(name = "link")
    public String link;

    @Element(name = "description")
    public String description;

    @Nullable
    @Element(name = "encoded", data = true, required = false)
    public String contentEncoded;
}
