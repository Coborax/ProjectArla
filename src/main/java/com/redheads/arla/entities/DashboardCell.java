package com.redheads.arla.entities;

public class DashboardCell {

    private int column;
    private int row;
    private int colSpan;
    private int rowSpan;

    private String contentPath;
    private ContentType contentType = ContentType.WEB;

    public DashboardCell(int column, int row, int colSpan, int rowSpan, String contentPath, ContentType contentType) {
        this.column = column;
        this.row = row;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
        this.contentPath = contentPath;
        this.contentType = contentType;
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

    public void setColumn(int column) {
        this.column = column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    public void setContentPath(String contentPath) {
        this.contentPath = contentPath;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "Row: " + row + ", Column: " + column + ", Row span: " + rowSpan + ", Column Span: " + colSpan + ", Content Type: " + contentType;
    }
}
