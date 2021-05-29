package com.redheads.arla.ui.models;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.control.MultipleSelectionModel;

/**
 * A View Model for a selectable list of T
 * @param <T> The type of object we wan to list, and be able to select
 */
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
