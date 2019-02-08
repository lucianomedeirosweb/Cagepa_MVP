package br.ufs.projetos.gocidade.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.text.format.DateFormat
import java.io.File
import java.util.*

/**
 * Created by samila on 20/11/17.
 */
class CameraUtil{

    companion object {


        val MIDIA_PHOTO = 0

        val REQUEST_PHOTO = 1

        val LAST_PHOTO = "last_photo"

        val PREFERENCE_MIDIA = "prefs_midia"
        val DIR_MIDIA = "go_cidade"

        val EXT = arrayOf(".jpg")
        val KEYS_PREFS = arrayOf(LAST_PHOTO)

        fun newMidia(type: Int): File {
            val mediaName = DateFormat.format("yyyy-MM-dd_hhmmss", Date()).toString()
            val dirMedia = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), DIR_MIDIA)

            if (!dirMedia.exists()) {
                dirMedia.mkdirs()
            }

            return File(dirMedia, mediaName + EXT[type])
        }

        fun saveLastMedia(ctx: Context, type: Int, media: String) {
            val sharedPreferences = ctx.getSharedPreferences(PREFERENCE_MIDIA, Context.MODE_PRIVATE)

            sharedPreferences.edit().putString(KEYS_PREFS[type], media)
                    .commit()

            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val contentURI = Uri.parse(media)
            mediaScanIntent.setData(contentURI)
            ctx.sendBroadcast(mediaScanIntent)
        }

        fun getLastMedia(ctx: Context, type: Int): String {
            return ctx.getSharedPreferences(PREFERENCE_MIDIA, Context.MODE_PRIVATE)
                    .getString(KEYS_PREFS[type], null)
        }

        fun loadImage(img: File, weight: Int, height: Int): Bitmap? {
            if (weight == 0 || height == 0) return null
            val bmOptions = BitmapFactory.Options()
            bmOptions.inJustDecodeBounds = true
            BitmapFactory.decodeFile(img.absolutePath, bmOptions)

            val photoWeight = bmOptions.outWidth
            val photoHeight = bmOptions.outHeight

            val scale = Math.min(photoWeight / weight, photoHeight / height)

            bmOptions.inJustDecodeBounds = false
            bmOptions.inSampleSize = scale
            bmOptions.inPurgeable = true
            bmOptions.inPreferredConfig = Bitmap.Config.RGB_565

            var bitmap = BitmapFactory.decodeFile(img.absolutePath, bmOptions)
            bitmap = rotate(bitmap, img.absolutePath)

            return bitmap
        }

        fun rotate(bitmap: Bitmap, path: String): Bitmap {
            var bm = bitmap
            try {
                val ei = ExifInterface(path)
                val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> {
                        bm = rotate(bm, 90f)
                    }
                    ExifInterface.ORIENTATION_ROTATE_180 -> {
                        bm = rotate(bm, 180f)
                    }
                    ExifInterface.ORIENTATION_ROTATE_270 -> {
                        bm = rotate(bm, 270f)
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return bm
        }

        fun rotate(bitmap: Bitmap, angle: Float): Bitmap {

            val matrix: Matrix = Matrix()
            matrix.postRotate(angle)

            val bm = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.width, bitmap.height, matrix, true)

            return bm
        }
    }
}