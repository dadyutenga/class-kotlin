package com.biglitecode.familyhub

import android.app.Application
import com.biglitecode.familyhub.worker.AppUsageSyncWorker

/**
 * Application entry point.
 *
 * Schedules the periodic app-usage sync worker once the app process starts. The
 * worker itself checks whether the signed-in user is a CHILD before collecting
 * or uploading anything.
 */
class FamilyHubApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppUsageSyncWorker.schedule(this)
    }
}
