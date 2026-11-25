package com.ury.alameenaprivate.data

import android.content.Context
import androidx.room.*

@Entity(tableName = "user_profile")
data class UserProfile(@PrimaryKey val id: Int = 1, val displayName: String)

@Dao
interface ProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun get(): UserProfile?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(p: UserProfile)
}

@Database(entities = [UserProfile::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    companion object {
        @Volatile private var inst: AppDatabase? = null
        fun get(c: Context): AppDatabase = inst ?: synchronized(this) {
            inst ?: Room.databaseBuilder(c.applicationContext, AppDatabase::class.java, "alameena.db").build().also { inst = it }
        }
    }
}
