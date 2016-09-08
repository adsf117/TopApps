package com.pruebaandroid.topapps.DataBase;

import com.pruebaandroid.topapps.DataObjects.Category;
import com.pruebaandroid.topapps.DataObjects.Entry;
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import java.io.IOException;
import java.sql.SQLException;
/**
 * Created by Andres on 07/09/2016.
 */
public class ConfigORM extends OrmLiteConfigUtil {

    private static final Class<?>[] classes = new Class[]{Entry.class, Category.class};

    public static void main(String[] args) throws SQLException, IOException {
        // TODO Auto-generated method stub
        writeConfigFile("ormlite_config.txt", classes);
    }
}
