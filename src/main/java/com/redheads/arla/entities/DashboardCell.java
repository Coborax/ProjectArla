package com.redheads.arla.entities;

public class DashboardCell {

    private int column;
    private int row;
    private int colSpan;
    private int rowSpan;

    private String contentPath;
    private ContentType contentType = ContentType.WEB;

    public DashboardCell(int column, int row, int colSpan, int rowSpan, String contentPath) {
        this.column = column;
        this.row = row;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
        this.contentPath = contentPath;
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

    public String getContentPath() {
        return contentPath;
    }

    public ContentType getContentType() {
        return contentType;
    }
}
