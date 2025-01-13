package com.segunfrancis.spacex.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.segunfrancis.spacex.data.local.dto.CompanyInfo
import com.segunfrancis.spacex.data.local.dto.LaunchesItem
import com.segunfrancis.spacex.util.SpaceXConstants.LOCAL_DB_NAME
import kotlinx.coroutines.sync.Mutex

@Database(exportSchema = true, version = 1, entities = [CompanyInfo::class, LaunchesItem::class])
abstract class SpacexRoomDatabase : RoomDatabase() {

    abstract fun getDao(): SpaceXDao

    companion object {
        private val LOCK = Mutex()
        @Volatile
        private var INSTANCE: SpacexRoomDatabase? = null
        fun getDatabase(context: Context): SpacexRoomDatabase {
            synchronized(LOCK) {
                var instance = INSTANCE
                return if (instance != null) {
                    instance
                } else {
                    instance =
                        Room.databaseBuilder(context, SpacexRoomDatabase::class.java, LOCAL_DB_NAME)
                            .build()
                    INSTANCE = instance
                    instance
                }
            }
        }
    }
}
