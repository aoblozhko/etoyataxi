package com.ostdlabs.etoyataxi.providers.impl.dto;


import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "table")
@XmlAccessorType(XmlAccessType.FIELD)
public class RbtStatResponse {

    @XmlElement(name = "tr")
    private RbtStatResponseRow[] rows;

    public RbtStatResponseRow[] getRows() {
        return rows;
    }

    public void setRows(RbtStatResponseRow[] rows) {
        this.rows = rows;
    }
}
