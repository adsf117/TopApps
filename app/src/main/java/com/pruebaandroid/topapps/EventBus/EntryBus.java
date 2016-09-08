package com.pruebaandroid.topapps.EventBus;

import com.squareup.otto.Bus;

/**
 * Created by Andres on 06/09/2016.
 */
public class EntryBus {

    private static Bus instance = null;

    private EntryBus()
    {
        instance = new Bus();
    }

    public static Bus getInstance()
    {
        if(instance == null)
        {
            instance = new Bus();
        }
        return instance;
    }
}
