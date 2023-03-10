package com.example.shoesstore.model
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShoeListData::class], version = 1, exportSchema = false)
abstract class ShoeDatabase: RoomDatabase() {
    abstract fun shoeDao(): ShoeDao

    companion object {
        @Volatile
        private var INSTANCE: ShoeDatabase? = null


        fun getDatabase(context: Context): ShoeDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShoeDatabase::class.java,
                    "shoe_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
