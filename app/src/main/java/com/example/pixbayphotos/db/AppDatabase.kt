package com.example.pixbayphotos.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pixbayphotos.models.Image
import com.example.pixbayphotos.models.RemoteKeys

@Database(entities = [Image::class, RemoteKeys::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun imagesDao(): ImageDao

    abstract fun keysDao(): RemoteKeysDao

}