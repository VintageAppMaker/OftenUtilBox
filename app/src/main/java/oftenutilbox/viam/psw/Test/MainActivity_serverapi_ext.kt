package oftenutilbox.viam.psw.Test

import com.test.psw.oftenutilbox.databinding.ExampleServerapiBinding
import oftenutilbox.viam.psw.Test.serverApi.HTTPRespErr
import oftenutilbox.viam.psw.Test.serverApi.IORoutineWithUI
import oftenutilbox.viam.psw.Test.serverApi.api
import oftenutilbox.viam.psw.Test.serverApi.data.User

fun MainActivity.testServerAPITest(){

    // get 방식 => Json object로 가져오기
    fun getMyInfo(fnSuccess : (userInfo: User) -> Unit, fnError : (HTTPRespErr) -> Unit){
        IORoutineWithUI({
            val user = api.test.getUserInfo()
            fnSuccess(user)

        }, {
            fnError(it)
        })
    }

    // post 방식 => Json object를 보내기
    fun postMyInfo(fnSuccess : (res: String) -> Unit, fnError : (HTTPRespErr) -> Unit){
        IORoutineWithUI({
            val res = api.test.addObjectWithDataClass(User(100, "guest"))
            fnSuccess(res.string())

        }, {
            fnError(it)
        })
    }

    // post 방식 => form data 보내기
    fun postFormInfo(fnSuccess : (res: String) -> Unit, fnError : (HTTPRespErr) -> Unit){
        IORoutineWithUI({
            val res = api.test.postFormdata("BC-212-12-111-991", 10000000)
            fnSuccess(res.string())

        }, {
            fnError(it)
        })
    }

    // get 방식 => path와 queryString 보내기
    fun addValue(fnSuccess : (res: String) -> Unit, fnError : (HTTPRespErr) -> Unit){
        IORoutineWithUI({
            val res = api.test.addByParam("add", 100)
            fnSuccess(res.string())

        }, {
            fnError(it)
        })
    }

    QuickExampleActivity.launch(this, { setContent ->
        val binding: ExampleServerapiBinding
        binding = ExampleServerapiBinding.inflate(layoutInflater)
        setContent(binding.root)

        binding.apply {
            btnApiget.setOnClickListener {
                getMyInfo({
                    txtResult.text = it.toString()
                }, {
                    txtResult.text = it.toString()
                })
            }

            btnApipost.setOnClickListener {
                postMyInfo({
                    txtResult.text = it.toString()
                }, {
                    txtResult.text = it.toString()
                })
            }

            btnApiPathAndQuery.setOnClickListener {
                addValue({
                    txtResult.text = it.toString()
                }, {
                    txtResult.text = it.toString()
                })
            }

            btnApipostForm.setOnClickListener {
                postFormInfo({
                    txtResult.text = it.toString()
                }, {
                    txtResult.text = it.toString()
                })
            }
        }

    })
}