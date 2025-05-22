//package com.example.chirp.data.database
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import com.example.chirp.data.Dao.AccountDao
//import com.example.chirp.data.model.Account
//
//@Database(entities = [Account::class], version = 1, exportSchema = false)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun accountDao(): AccountDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//        fun getDatabase(context: Context): AppDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "chirp_database"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}