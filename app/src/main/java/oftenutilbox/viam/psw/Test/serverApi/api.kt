package oftenutilbox.viam.psw.Test.serverApi

import com.bumptech.glide.util.Util
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import oftenutilbox.viam.psw.util.DUtil
import oftenutilbox.viam.psw.util.SafeHandler
import okhttp3.*
import org.json.JSONObject
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit


object api {
    var errorCode: String = ""
    var errorMsg: String = ""
    var httpCode: Int = 0

    var accessToken: String = ""
    var devID: String = ""


    fun setDeviceIdInfo(token: String, devId: String) {
        api.devID = devId
        api.accessToken = token
    }

    // androidmanifest.xml에서 application 영역에
    // android:usesCleartextTraffic="true"을 추가해야 보안없는 http 프로토콜을 사용할 수 있다.
    // 예제는 서버(https://github.com/VintageAppMaker/quick_ktor)가 실행된 주소를 하드코딩한다.
    var BASE = "http://192.168.0.14:8080"

    val builder = OkHttpClient.Builder()
        // timeout setting
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)

        // dump packet
        .addInterceptor(HttpLoggingInterceptor().apply {
            // Debug시에 모든 패킷을 덤프
            // setLevel(HttpLoggingInterceptor.Level.BODY)
        })

        // header(인증키 -access token- 운영이 목적임)
        .addInterceptor(Interceptor { chain ->
            var hdr = chain.request().newBuilder().build()

            // 토큰이 있을 경우와 없을 경우처리
            if (accessToken != "" && accessToken != "null") {
                hdr = chain.request().newBuilder()
                    .addHeader("Authorization", " Bearer ${accessToken}")
                    .addHeader("devID", devID)
                    .build()
            } else {
                hdr = chain.request().newBuilder()
                    .addHeader("devID", devID)
                    .addHeader("Content-Type", " application/json")
                    .build()
            }


            return@Interceptor chain.proceed(hdr)
        })

        .addInterceptor(Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)

            // 디버깅용 함수:
            makeCurlforDebug(request)

            // 에러코드 핸들링
            // 향후, 넘어오는 값을 JSON으로 파싱
            httpCode = response.code()

            if (response.code() != 200) {
                SafeHandler({
                    val jsonString = response.body()?.string() ?: ""
                    val jsonObject = JSONObject(jsonString)

                    // 200이 아닐경우, 넘어오는 json 처리
                }, {})

                // 2번 호출
                return@Interceptor chain.proceed(request)
            } else {
                // 200일때 전역적으로 처리해야할 필드만 가져오기

            }

            response
        })


    // 서버개발과 협업을 위한 Curl 문자열 만들기
    private fun makeCurlforDebug(req: Request) {
        // 값이 없을 때는 문자열로 null로 출력됨. ""에서 처리하기에 코틀린이 변환함.
        val token = if (req.headers().get("Authorization") != null)
            "-H \"Authorization: ${req.headers().get("Authorization")}\" "
        else ""

        var cmd = "curl ${req.url()} $token -H \"deviceType: ${
            req.headers().get("deviceType")
        }\" -H \"deviceId: ${req.headers().get("deviceId")}\"  -H \"deviceVersionNumber: ${
            req.headers().get("deviceVersionNumber")
        }\""

        DUtil.d(me = DTag(), cmd)
    }


    val test: TestApi
        get() {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build()
            return retrofit.create(TestApi::class.java!!)
        }

    fun getBody(value : String ): RequestBody {
        //val r = MultipartBody.Part.createFormData(key, value.toString())
        var r= RequestBody.create(
            MediaType.parse("multipart/form-data"), value)
        //Util.d("{$r} ${key}, ${value} ${value.toString()} ")
        return r
    }
}



fun DTag() = "oftenutilbox.viam.psw.Test.serverApi.api"
enum class ErrorType {
    UnknownHost, SocketTimeout, MalformedJson, JSONError;
}


// >> 코루틴으로 처리하기 <<
// UI에서 바로사용하면 안됨
// ViewModel에서 사용. observer/post로 통신
data class HTTPRespErr(val httpcode : Int, val code : String, val msg : String)
fun IORoutine(fnProcess: suspend CoroutineScope.() -> Unit, fnError : suspend CoroutineScope.(err : HTTPRespErr)->Unit){
    CoroutineScope(Dispatchers.IO).launch {
        try{
            fnProcess()
        }
        catch (e: Exception){
            DUtil.d(me = DTag(), e.toString())
            when (e){
                is UnknownHostException   -> {api.errorMsg = "UnknownHostException"}
                is SocketTimeoutException -> {api.errorMsg = "SocketTimeoutException"}
            }

            DUtil.d(me = DTag(),"IORoutine -> fnError ${api.httpCode}, ${api.errorCode}, ${api.errorMsg}")
            fnError(HTTPRespErr(api.httpCode, api.errorCode, api.errorMsg))
        }
    }

}


// >> 코루틴으로 처리하기 <<
// ViewModel에서 사용.
// UI에서 바로사용
// 클로져를 통해 결과를 전달하기 위함
fun IORoutineWithUI(fnProcess: suspend CoroutineScope.() -> Unit, fnError : suspend CoroutineScope.(err : HTTPRespErr)->Unit){
    CoroutineScope(Dispatchers.Main).launch {
        try{
            fnProcess()
        }
        catch (e: Exception){
            when (e){
                is UnknownHostException   -> {
                    fnError(HTTPRespErr(ErrorType.UnknownHost.ordinal, api.errorCode, api.errorMsg))

                    return@launch
                }

                is SocketTimeoutException -> {
                    fnError(HTTPRespErr(ErrorType.SocketTimeout.ordinal, api.errorCode, api.errorMsg))

                    return@launch
                }

                is com.google.gson.stream.MalformedJsonException ->{
                    fnError(HTTPRespErr(ErrorType.MalformedJson.ordinal, api.errorCode, api.errorMsg))
                    return@launch
                }

                is org.json.JSONException ->{
                    fnError(HTTPRespErr(ErrorType.JSONError.ordinal, api.errorCode, api.errorMsg))
                    return@launch
                }
            }
            fnError(HTTPRespErr(api.httpCode, api.errorCode, api.errorMsg))
            DUtil.d(me = DTag(),"IORoutine -> fnError ${api.httpCode}, ${api.errorCode}, ${api.errorMsg}")

        }
    }
}
