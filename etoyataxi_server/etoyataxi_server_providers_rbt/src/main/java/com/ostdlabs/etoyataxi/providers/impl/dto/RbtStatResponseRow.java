package com.ostdlabs.etoyataxi.providers.impl.dto;


import javax.xml.bind.annotation.XmlElement;

public class RbtStatResponseRow {

    @XmlElement(name = "td")
    public String[] col;

}
