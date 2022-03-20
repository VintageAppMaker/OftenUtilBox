package oftenutilbox.viam.psw.Test.serverApi

import oftenutilbox.viam.psw.Test.serverApi.data.User
import okhttp3.ResponseBody
import retrofit2.http.*

interface TestApi {
    // 각 유형별 API Example

    // Simple get
    @GET("/json/gson")
    suspend fun getUserInfo(): User

    // Query parameter와 path 값지정
    @GET("/param/{action}")
    suspend fun addByParam(@Path("action") action : String, @Query("value1") value1 : Int): ResponseBody

    // post by jsonObject
    // 반드시 JsonObject 규격으로 해야한다.
    @Headers( "Content-Type: application/json; charset=utf-8")
    @POST("/post/addObject")
    suspend fun addObjectWithDataClass (@Body userInfo: User) : ResponseBody

    // post by form data
    @FormUrlEncoded
    @POST("/post/register")
    suspend fun postFormdata(
        @Field("account") account : String,
        @Field("money")   money   : Int
    ) : ResponseBody


}
