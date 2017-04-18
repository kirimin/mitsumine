package me.kirimin.mitsumine._common.network.entity;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * フィードのRSSルート
 */
@Root(name = "rdf:RDF", strict = false)
public class FeedRssRoot {

    @ElementList(required = false, inline = true, empty = true, entry = "item", name = "item")
    public List<FeedEntity> itemList;
}
