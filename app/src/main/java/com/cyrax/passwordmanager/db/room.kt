package com.cyrax.passwordmanager.db

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.cyrax.passwordmanager.R

@Entity(tableName = "Accounts" )
data class Accounts(
    @PrimaryKey(autoGenerate = true )
    @ColumnInfo("id" )  var id:Long = 0,
    @ColumnInfo("title")  var title:String = "",
    @ColumnInfo("lastUpdated")  var lastUpdated:Long = System.currentTimeMillis(),
    @ColumnInfo("userName") var userName:String = "",
    @ColumnInfo("pass") var pass:String = "",
    @ColumnInfo("email") var email:String = "",
)

@Entity(tableName = "Cards")
data class Cards(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")  var id:Long = 0,
    @ColumnInfo("title") var title:String = "Card",
    @ColumnInfo("lastUpdated")  var lastUpdated:Long = System.currentTimeMillis(),
    @ColumnInfo("cardNumber") var cardNumber:String = "",
    @ColumnInfo("cvv") var cvv:String = "",
    @ColumnInfo("validity") var validity:String = "",
)

@Entity(tableName = "PIN" )
data class Pin(
    @PrimaryKey(autoGenerate = true )
    @ColumnInfo("id" )  var id:Long = 0,
    @ColumnInfo("pin") var pin:String = "",
    @ColumnInfo("hint") var hint:String = "",
)

@Dao
interface PinDao{
    @Query("SELECT * FROM PIN")
    suspend fun getPin():Pin?

    @Insert
    suspend fun insertPin(pin :  Pin)

    @Update
    suspend fun updatePin(pin :  Pin)

    @Query("DELETE FROM PIN ")
    suspend fun removePin()
}

@Dao
interface AccountDao{
    @Query("SELECT * FROM Accounts ORDER BY LOWER(title)")
    suspend fun getAllAcc():List<Accounts>

    @Insert
    suspend fun insertAcc(data : Accounts)

    @Update
    suspend fun updateAcc(data : Accounts)

    @Query("DELETE FROM Accounts WHERE id = :id")
    suspend fun removeAcc(id:Long)
}

@Dao
interface CardDao{
    @Query("SELECT * FROM Cards ORDER BY lastUpdated DESC")
    suspend fun getAllCards():List<Cards>

    @Insert
    suspend fun insertCard(data : Cards)

    @Update
    suspend fun updateCard(data : Cards)

    @Query("DELETE FROM Cards WHERE id = :id")
    suspend fun removeCard(id:Long)
}

@Database(entities = [Accounts::class , Cards::class , Pin::class], version = 1, exportSchema = false)
abstract class db():RoomDatabase(){
    abstract fun acc():AccountDao
    abstract fun card():CardDao
    abstract fun pin():PinDao
    companion object {
        @Volatile
        private var Instance: db? = null

        fun getDatabase(context: Context): db {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, db::class.java, "database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

