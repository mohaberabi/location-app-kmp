package org.mohaberabi.rpos

import android.app.Application
import custom.KoinInit
import model.AppLocation


class LocationApplication : Application() {

    override fun onCreate() {
        KoinInit(this@LocationApplication).initialize()
        super.onCreate()
    }
}