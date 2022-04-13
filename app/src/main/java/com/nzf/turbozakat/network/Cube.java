package com.nzf.turbozakat.network;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "cube", strict = false)
public class Cube {

    @Element(name = "currency")
    private String currency;

    @Element(name = "rate")
    private Double rate;

}
