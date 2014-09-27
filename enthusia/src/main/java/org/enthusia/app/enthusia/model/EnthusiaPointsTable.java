package org.enthusia.app.enthusia.model;

import java.io.Serializable;

@SuppressWarnings("unused")
public class EnthusiaPointsTable implements Serializable, Comparable<EnthusiaPointsTable> {

    private String department;
    private int points;

    public EnthusiaPointsTable(String department, int points) {
        this.department = department;
        this.points = points;
    }

    public String getDepartment() { return this.department; }
    public int getPoints() { return this.points; }

    public void setDepartment(String department) { this.department = department; }
    public void setPoints(int points) { this.points = points; }

    @Override
    public int compareTo(EnthusiaPointsTable o) {
        return o.points - this.points;
    }

}
