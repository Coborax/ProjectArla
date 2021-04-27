package com.redheads.arla.persistence;

import java.util.List;

public interface IDataAccess<T> {

    void create(T toCreate);
    List<T> readAll();
    T read(int id);
    void updateAll(List<T> toUpdate);
    void update(T toUpdate);
    void delete(T toDelete);

}
