package com.android.skripsi.carikuliner;

import android.content.Context;

interface ConnectionInterface {

    void checkConnection(Context ctx); //to check whether connection is available or not
    void alertTimeout(); //to give alert when conection is time out
    void alertNoConnection(); //to give alert when connection is not available

    // interface used to give warn when GPS is not enabled
    interface GPS{
        void alertNoGPS();
    }
}
