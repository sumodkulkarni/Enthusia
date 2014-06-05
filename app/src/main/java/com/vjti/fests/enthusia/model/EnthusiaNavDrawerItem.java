package com.vjti.fests.enthusia.model;

import java.io.Serializable;

public class EnthusiaNavDrawerItem implements Serializable {

    private String title;
    private int icon;

    protected EnthusiaNavDrawerItem() {}

    public EnthusiaNavDrawerItem (String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() { return this.title; }
    public int getIcon() { return this.icon; }

    public void setTitle (String title) { this.title = title; }
    public void setIcon (int icon) { this.icon = icon; }

}
