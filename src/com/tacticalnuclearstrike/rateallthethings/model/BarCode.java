package com.tacticalnuclearstrike.rateallthethings.model;

import java.io.Serializable;

/**
 * User: Fredrik / 2012-01-05
 */
public class BarCode implements Serializable{
    public long Id;

    public String Format;
    public String Code;

    public float Rating;
    public boolean HasRated;

    public String Name;
    public String Manufacturer;
}

