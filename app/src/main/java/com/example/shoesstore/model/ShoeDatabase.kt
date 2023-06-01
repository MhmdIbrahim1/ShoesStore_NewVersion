package com.example.shoesstore.model
import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.shoesstore.model.UserModel.UserDao
import com.example.shoesstore.model.UserModel.UserEntity

@Database(entities = [ShoeListData::class, UserEntity::class], version = 4, exportSchema = false)
abstract class ShoeDatabase: RoomDatabase() {
    abstract fun shoeDao(): ShoeDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: ShoeDatabase? = null
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                   database.execSQL("ALTER TABLE shoe_table ADD COLUMN shoePrice TEXT NOT NULL DEFAULT ''")
            }
        }
        fun getDatabase(context: Application): ShoeDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShoeDatabase::class.java,
                    "shoe_database"
                ).addMigrations(MIGRATION_3_4)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
