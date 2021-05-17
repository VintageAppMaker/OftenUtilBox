package oftenutilbox.viam.psw.util

import kotlinx.coroutines.*
import java.lang.Exception
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