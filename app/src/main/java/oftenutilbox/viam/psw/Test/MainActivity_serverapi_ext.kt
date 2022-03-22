package oftenutilbox.viam.psw.Test

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.test.psw.oftenutilbox.databinding.ExampleServerapiBinding
import oftenutilbox.viam.psw.Test.serverApi.HTTPRespErr
import oftenutilbox.viam.psw.Test.serverApi.IORoutineWithUI
import oftenutilbox.viam.psw.Test.serverApi.api
import oftenutilbox.viam.psw.Test.serverApi.data.User
import oftenutilbox.viam.psw.util.DUtil
import oftenutilbox.viam.psw.util.SafeHandler
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

fun MainActivity.testServerAPITest(){

    fun getMediaType(ext : String ) : MediaType? {
        return when (ext.toLowerCase()){
            "jpg"  -> {
                MediaType.parse("image/jpg")}
            "jpeg" -> {
                MediaType.parse("image/jpg")}
            "png"  -> {
                MediaType.parse("image/png")}
            "pdf" -> {
                MediaType.parse("doc/pdf")}
            "mov" -> {
                MediaType.parse("video/mov")}
            "avi" -> {
                MediaType.parse("video/avi")}
            "mp4" -> {
                MediaType.parse("video/mp4")}
            else -> {null}
        }
    }

    // 파일 선택하기
    var selectedfileExt  = ""
    var selectedfilename = ""
    fun getUploadFilename() = "${selectedfilename}"
    fun makeUploadInfo(): MultipartBody.Part {
        var path: File = getFilesDir()
        val file = File(path, getUploadFilename())

        val mediaType = getMediaType(selectedfileExt)

        val requestFile = RequestBody.create(mediaType, file)
        val body = MultipartBody.Part.createFormData("attachFile", file.name, requestFile)
        return body
    }

    // get 방식 => Json object로 가져오기
    fun getMyInfo(fnSuccess : (userInfo: User) -> Unit, fnError : (HTTPRespErr) -> Unit){
        IORoutineWithUI({
            val user = api.test.getUserInfo()
            fnSuccess(user)

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

    // post 방식 => fileupload, 코루틴을 사용하지 않는 레트로핏 기본제공 callback 형식통신
    fun fileAndFormdataUpload(fnSuccess : (res: String) -> Unit, fnError : (HTTPRespErr) -> Unit){
        api.test.fileUpload(api.getBody("filename을 Field값으로 보냄"), makeUploadInfo()).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val body = response.body()?.string()
                val errBody = response.errorBody()?.string()
                if (body != null){
                    fnSuccess(body.toString())
                } else{
                    fnError(HTTPRespErr(-1, "", errBody ?: ""))
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                //fnError()
            }
        })
    }

    QuickExampleActivity.launch(this, {  act, setContent ->
        val binding: ExampleServerapiBinding
        binding = ExampleServerapiBinding.inflate(layoutInflater)
        setContent(binding.root)

        binding.apply {

            edtIP.addTextChangedListener( object:TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    api.BASE = edtIP.text.toString()
                    DUtil.d(this.javaClass.toString(), api.BASE)
                }
            })

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

            btnApiFileupload.setOnClickListener {


                act.takeFile { i, intent ->
                    if (intent == null) return@takeFile

                    // uri 가져오기
                    SafeHandler({
                        val uri = intent!!.data
                        selectedfilename = act.getFileNameFromUri(uri!!) ?: ""

                        // 확장자 구하기
                        val pos: Int = selectedfilename!!.lastIndexOf(".")
                        val ext: String = selectedfilename!!.substring(pos + 1)

                        selectedfileExt = ext

                        //  파일쓰기
                        val inputStream = contentResolver.openInputStream(uri)
                        var fos: FileOutputStream = openFileOutput(getUploadFilename(), Context.MODE_PRIVATE)
                        fos!!.write(inputStream!!.readBytes())
                        fos!!.close()

                       fileAndFormdataUpload({
                               txtResult.text = it.toString()
                           }, {
                               txtResult.text = it.toString()
                           })


                    }, {})

                }
            }
        }

    })
}

