package oftenutilbox.viam.psw.util

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.test.psw.simplebox.BuildConfig
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import java.lang.Exception
import java.text.NumberFormat
import java.util.concurrent.TimeUnit

// try .. catch 간편화
fun SafeHandler( fnCode : () -> Unit, fnError : ( (String) -> Unit)? = null  ){
    try{
        fnCode()
    } catch ( e: Exception){
        if (fnError != null ) {
            fnError(e.toString())
        } else {
            // [TODO] 디폴트로 하고자 하는 기능
        }
    }
}

fun UiStopWatch(min : Int, sec : Int, fnCallBack : (Int, Int) ->Unit) : Job {
    val job = CoroutineScope(Dispatchers.IO).launch {
        val totalSeconds = TimeUnit.MINUTES.toSeconds(min.toLong()) + TimeUnit.SECONDS.toSeconds(sec.toLong())
        val tickSeconds = 1
        for (second in totalSeconds downTo tickSeconds) {
            val Passedmin = TimeUnit.SECONDS.toMinutes(second)
            val Passedsec = second - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(second))
            fnCallBack(Passedmin.toInt(), Passedsec.toInt())

            delay(1000)
        }

        fnCallBack(0, 0)
    }
    return job
}

fun convertMoneyComma(coin : Int ) : String{
    return NumberFormat.getIntegerInstance().format(coin)
}

object ImageTool{
    fun getNetworkDrawable(ctx : Context, sUrl : String, fnSetting : (BitmapDrawable?) -> Unit = {} ){
        var job = Job()
        CoroutineScope(job).launch {
            try{
                val bmp = Glide
                    .with(ctx)
                    .asBitmap()
                    .apply(RequestOptions.circleCropTransform())
                    .load(sUrl)
                    .into(100, 100)
                    .get()
                val res: Resources = ctx.resources

                fnSetting(BitmapDrawable(res, bmp))
            } catch ( e: Exception){
                e.printStackTrace()
                job.cancel()
            }
        }
    }

}

class DUtil{
    companion object{

        // 확장함수와 class외에서 정의된 함수에서는
        // me에 패키지명(= 패키지명.파일명)을 직접 입력해주어야 한다.
        val tag = "DUTIL"
        fun d(me : String = "" , sLog : String){

            if (true && !BuildConfig.DEBUG) {
                return
            }

            for (i in Thread.currentThread().stackTrace.indices) {
                val fileName = Thread.currentThread().stackTrace[i].fileName
                val functionName = Thread.currentThread().stackTrace[i].methodName
                val className = Thread.currentThread().stackTrace[i].className
                val lineNumber = Thread.currentThread().stackTrace[i].lineNumber

                // me가 없으면 로그만 찍기
                if(me == ""){
                    Log.v(tag, sLog)
                    return
                }

                // 클래스명이 일치하지않으면 다음스택
                val clsTag = me.replace("class ", "")
                if(className.indexOf(clsTag) < 0 ) continue

                // (파일이름:줄번호) 형태로 출력하면
                // Android Studio의 logcat에서 링크가 생성됨
                var s = sLog
                s = "($fileName:$lineNumber) $functionName(): $s"
                Log.v(tag, s)

                // 최초 1회만 출력 후 종료
                return
            }

        }
    }
}