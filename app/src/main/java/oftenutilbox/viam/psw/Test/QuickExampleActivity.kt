package oftenutilbox.viam.psw.Test

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import com.test.psw.oftenutilbox.R
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class QuickExampleActivity : AppCompatActivity() {
    companion object{
        var fnSetup : (act : QuickExampleActivity,  (View)-> Unit )-> Unit = {act, setup -> }
        var bTrans  : Boolean = false
        fun launch(ctx : Context, fnSetup : ( act : QuickExampleActivity, (View)-> Unit )-> Unit, bTransparent : Boolean =false ){
            Intent(ctx, QuickExampleActivity::class.java).apply {
                QuickExampleActivity.fnSetup = fnSetup
                bTrans  = bTransparent
                ctx.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (bTrans){
            setTheme(R.style.Normal_transparent)
        }
        super.onCreate(savedInstanceState)
        fnSetup(this, ::setContentView)

    }

    // 파일선택
    final var FILE_CHOOSE : Int = 2121
    var fnCallback : (Int, Intent?)->Unit ={ n, intent ->}
    open fun takeFile(fnResult:(Int, Intent?)->Unit){
        fnCallback = fnResult
        Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            setType("*/*")
            val extraMimeTypes = arrayOf("application/pdf", "application/doc")
            putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes)
            startActivityForResult(Intent.createChooser(this, ""), FILE_CHOOSE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            FILE_CHOOSE -> {fnCallback(requestCode, data)}
        }
    }

    fun getFileNameFromUri(uri: Uri): String? {
        var fileName = ""
        val cursor: Cursor = getContentResolver()!!.query(uri, null, null, null, null)!!

        if (cursor != null && cursor.moveToFirst()) {
            fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
        cursor.close()
        return fileName
    }
}
