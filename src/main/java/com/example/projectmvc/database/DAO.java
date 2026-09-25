package com.example.projectmvc.database;

import com.example.projectmvc.model.Coffee;
import com.example.projectmvc.model.CoffeeMenuRow;
import javafx.collections.ObservableList;

public interface DAO {

    boolean addCoffee(Coffee coffee);

    ObservableList<CoffeeMenuRow> getAllCoffeeMenuRows();

    boolean updateCoffee(
            String oldName,
            String newName,
            double smallPrice,
            double mediumPrice,
            double largePrice
    );

    boolean deleteCoffee(String name);
}