package com.example.pixbayphotos.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pixbayphotos.models.Image
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<Image>)

    @Query("SELECT * from Image")
    fun getImages(): Flow<List<Image>>

    @Query("SELECT * from Image")
    fun getImagesNormal() : List<Image>

    @Query("SELECT * from Image ORDER BY pk ASC")
    fun getImagesPaging(): PagingSource<Int, Image>

    @Query("DELETE FROM Image")
    suspend fun clearAll()
}