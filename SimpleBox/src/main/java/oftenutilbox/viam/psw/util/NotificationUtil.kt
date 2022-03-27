package oftenutilbox.viam.psw.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import java.net.URL

class NotificationUtil {

    companion object{

        fun getImageFromUrl(imgUrl : String ) : Bitmap?{
            try {
                val url = URL(imgUrl)
                var bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                return bigPicture
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

            return null
        }

        fun notiWithImage(ctx : Context, iconRes : Int, intent : Intent, title: String, message: String, bm : Bitmap) {

            // 푸시메시지 누적
            var serialNo = "" + System.currentTimeMillis()
            serialNo = serialNo.substring(5, 12)
            val uniqID = serialNo.toInt()

            // 언제나 새롭게 갱신
            val contentIntent = PendingIntent.getActivity(ctx, uniqID, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId   = "OFTENUTIL"
            val channelName = "OFTENUTIL"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(notificationChannel)
            }

            val builder = NotificationCompat.Builder(ctx, channelId)
                .setSmallIcon(iconRes)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(contentIntent)

            // 이미지 처리
            builder.setLargeIcon(bm)
            builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bm))

            notificationManager.notify(uniqID, builder.build())
        }

    }
}