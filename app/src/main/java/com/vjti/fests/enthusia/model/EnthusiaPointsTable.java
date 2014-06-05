package com.vjti.fests.enthusia.model;

import java.io.Serializable;

public class EnthusiaPointsTable implements Serializable, Comparable<EnthusiaPointsTable> {

    private String department;
    private float points;

    protected EnthusiaPointsTable() {}

    public EnthusiaPointsTable(String department, float points) {
        this.department = department;
        this.points = points;
    }

    public String getDepartment() { return this.department; }
    public float getPoints() { return this.points; }

    public void setDepartment(String department) { this.department = department; }
    public void setPoints(float points) { this.points = points; }

    @Override
    public int compareTo(EnthusiaPointsTable o) {
        return (int) (this.points < o.points ? this.points : o.points);
    }
}
