package com.growly.app

import android.app.Application
import androidx.room.Room
import com.growly.app.data.database.GrowlyDatabase
import com.google.firebase.FirebaseApp

class GrowlyApplication : Application() {
    
    val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            GrowlyDatabase::class.java,
            "growly_database"
        ).build()
    }
    
    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
    }
}
