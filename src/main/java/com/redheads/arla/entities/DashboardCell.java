package com.redheads.arla.entities;

public class DashboardCell {

    private int column;
    private int row;
    private int colSpan;
    private int rowSpan;

    public DashboardCell(int column, int row, int colSpan, int rowSpan) {
        this.column = column;
        this.row = row;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public int getColSpan() {
        return colSpan;
    }

    public int getRowSpan() {
        return rowSpan;
    }
}
