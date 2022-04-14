package oftenutilbox.viam.psw.example.data.room.tables

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// database 초기화
@Database(entities = [AcccountInfo::class], version = 1)
abstract class AppDatabase:  RoomDatabase() {
    abstract fun accountDao(): AccountDao

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "this-app-databases"
                    ).build()
                }
            }
            return instance
        }
    }
}



// Google migration 예제코드

/*
val MIGRATION_1_2 = object : Migration(1, 2) {
  override fun migrate(database: SupportSQLiteDatabase) {
    database.execSQL("CREATE TABLE `Fruit` (`id` INTEGER, `name` TEXT, " +
      "PRIMARY KEY(`id`))")
  }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
  override fun migrate(database: SupportSQLiteDatabase) {
    database.execSQL("ALTER TABLE Book ADD COLUMN pub_year INTEGER")
  }
}

Room.databaseBuilder(applicationContext, MyDb::class.java, "database-name")
  .addMigrations(MIGRATION_1_2, MIGRATION_2_3).build()


* */