package oftenutilbox.viam.psw.util

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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