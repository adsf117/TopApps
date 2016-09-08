package com.pruebaandroid.topapps.DataObjects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Andres on 07/09/2016.
 */
@DatabaseTable
public class Category {

    @DatabaseField(generatedId = true)
    private int IdCategory = 0;

    @DatabaseField
    private String name;

    public int getIdCategory() {
        return IdCategory;
    }

    public void setIdCategory(int idCategory) {
        IdCategory = idCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
