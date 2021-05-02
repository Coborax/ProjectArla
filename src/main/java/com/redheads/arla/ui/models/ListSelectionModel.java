package com.redheads.arla.ui.models;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.control.MultipleSelectionModel;

public class ListSelectionModel<T> {

    private ReadOnlyObjectProperty<T> selectedItem;
    private MultipleSelectionModel<T> selectionModel;

    public ListSelectionModel(MultipleSelectionModel<T> selectionModel) {
        this.selectionModel = selectionModel;
        this.selectedItem = selectionModel.selectedItemProperty();
    }

    public T getSelectedItem() {
        return selectedItem.get();
    }

    public ReadOnlyObjectProperty<T> selectedItemProperty() {
        return selectedItem;
    }

    public MultipleSelectionModel<T> getSelectionModel() {
        return selectionModel;
    }
}
