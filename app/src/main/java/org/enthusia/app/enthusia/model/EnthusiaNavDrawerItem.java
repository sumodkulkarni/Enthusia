package org.enthusia.app.enthusia.model;

import java.io.Serializable;

public class EnthusiaNavDrawerItem implements Serializable {

    private String title;
    private int icon;
    private boolean isSelected;

    public EnthusiaNavDrawerItem (String title, int icon) {
        this.title = title;
        this.icon = icon;
        this.isSelected = false;
    }

    public String getTitle() { return this.title; }
    public int getIcon() { return this.icon; }
    public boolean isSelected() { return this.isSelected; }

    public void setTitle (String title) { this.title = title; }
    public void setIcon (int icon) { this.icon = icon; }
    public void setSelected (boolean selected) { this.isSelected = selected; }

}
