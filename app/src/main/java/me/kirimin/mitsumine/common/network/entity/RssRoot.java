package me.kirimin.mitsumine.common.network.entity;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "rdf:RDF", strict = false)
public class RssRoot {

    @ElementList(required = false, inline = true, empty = true, entry = "item", name = "item")
    public List<Item> itemList;
}
