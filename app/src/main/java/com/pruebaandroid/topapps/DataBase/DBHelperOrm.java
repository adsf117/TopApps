package com.pruebaandroid.topapps.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.pruebaandroid.topapps.DataObjects.Category;
import com.pruebaandroid.topapps.DataObjects.Entry;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.pruebaandroid.topapps.R;

import java.sql.SQLException;
/**
 * Created by Andres on 07/09/2016.
 */
public class DBHelperOrm extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "TopApps.db";
    private static final int DATABASE_VERSION = 2;


    //---------------------------- DAO  Entry.---------------------------------
    private Dao<Entry, Integer> EntryDAO = null;
    private RuntimeExceptionDao<Entry, Integer> EntryRuntimeDAO= null;


    public Dao<Entry, Integer> GetEntryDAO() throws SQLException {
        if(EntryDAO ==null)
            EntryDAO = getDao(Entry.class);
        return EntryDAO;
    }

    public RuntimeExceptionDao<Entry, Integer> GetEntryRuntimeDAO() {
        if(EntryRuntimeDAO == null)
            EntryRuntimeDAO = getRuntimeExceptionDao(Entry.class);
        return EntryRuntimeDAO;

    }
    //---------------------------- DAO  Entry.---------------------------------
    private Dao<Category, Integer> CategoryDAO = null;
    private RuntimeExceptionDao<Category, Integer> CategoryRuntimeDAO= null;


    public Dao<Category, Integer> GetCategoryDAO() throws SQLException {
        if(CategoryDAO ==null)
            CategoryDAO = getDao(Category.class);
        return CategoryDAO;
    }

    public RuntimeExceptionDao<Category, Integer> GetCategoryRuntimeDAO() {
        if(CategoryRuntimeDAO == null)
            CategoryRuntimeDAO = getRuntimeExceptionDao(Category.class);
        return CategoryRuntimeDAO;

    }
    public DBHelperOrm(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
        // TODO Auto-generated constructor stub
    }
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        // TODO Auto-generated method stub
        try {

            TableUtils.createTable(connectionSource, Entry.class);
            TableUtils.createTable(connectionSource, Category.class);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int arg2,
                          int arg3) {
        // TODO Auto-generated method stub
        try {

            TableUtils.dropTable(connectionSource, Entry.class,true);
            TableUtils.dropTable(connectionSource, Category.class,true);
            onCreate(db,connectionSource);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
