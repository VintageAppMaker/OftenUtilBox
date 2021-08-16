package oftenutilbox.viam.psw.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel(){
    var isRun: Boolean = false
    private lateinit var job : Job
    private var interval : Long = 1000

    fun setInterval (n : Long ) {
        interval = n
    }
    fun timerStart(fnCallback :() ->Unit){
        isRun = true
        if(::job.isInitialized) job.cancel()

        job = viewModelScope.launch {
            while(true) {
                delay(interval)
                fnCallback()
            }
        }
    }

    fun timerStop(){
        isRun = false
        if(::job.isInitialized) job.cancel()
    }
}