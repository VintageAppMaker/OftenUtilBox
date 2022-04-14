package oftenutilbox.viam.psw.example.data.room.tables

import androidx.room.*

@Entity
data class AcccountInfo(
    var account  : String,
    var money    : Int,
    var idNumber : String
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}

@Dao
interface AccountDao {
    @Insert
    fun insert(user: AcccountInfo)

    @Update
    fun update(user: AcccountInfo)

    @Delete
    fun delete(user: AcccountInfo)

    // using query
    @Query("SELECT * FROM AcccountInfo")
    fun getAll(): List<AcccountInfo>

    @Query("delete FROM AcccountInfo")
    fun deleteAll()

}