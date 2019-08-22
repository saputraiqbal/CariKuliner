package com.android.skripsi.carikuliner;

import android.content.Context;

interface ConnectionInterface {

    void checkConnection(Context ctx);
    void alertTimeout();
    void alertNoConnection();

    interface GPS{
        void alertNoGPS();
    }
}
