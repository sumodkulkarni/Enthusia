package org.enthusia.app.enthusia.model;

import java.io.Serializable;

public class EnthusiaCommittee implements Serializable {

    private String name, post;
    private int position;

    public EnthusiaCommittee (String name, int position) {
        this(name, "", position);
    }

    public EnthusiaCommittee (String name, String post, int position) {
        this.name = name;
        this.post = post;
        this.position = position;
    }

    public String getName() { return this.name; }
    public String getPost() { return this.post; }
    public int getPosition() { return this.position; }

    public void setName(String name) { this.name = name; }
    public void setPost(String post) { this.post = post; }
    public void setPosition(int position) { this.position = position; }

}
