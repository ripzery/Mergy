package me.ripzery.mergy

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var mCurrentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnTakeAPhoto.setOnClickListener {
            dispatchTakePictureIntent()
        }

        /* get bitmap from resource */
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.test_9).copy(Bitmap.Config.ARGB_8888, true)
        bitmap.setHasAlpha(true)
        val pixel = bitmap.getPixel(0, 0)
        val red = Color.red(pixel)
        val green = Color.green(pixel)
        val blue = Color.blue(pixel)
//        bitmap.pixels
        Log.d("Total pixels", "${bitmap.width}, ${bitmap.height}")
        for (i in 0 until bitmap.width) {
            for (j in 0 until bitmap.height) {
                val pixel1 = bitmap.getPixel(i, j)
                if (Color.green(pixel1) - Color.red(pixel1) > 20 && Color.green(pixel1) - Color.blue(pixel1) > 20) {
                    val a = Color.alpha(Color.TRANSPARENT)
                    bitmap.setPixel(i, j, a)
                } else {
                    Log.d("Non-Green", "${Color.red(pixel1)} ${Color.green(pixel1)} ${Color.blue(pixel1)}")
                }
            }
        }
        ivGreenPhoto.setImageBitmap(bitmap)
        Log.d("Color First Pixel", "$red $green $blue")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            galleryAddPic()
            setPic()
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            var mFilePhoto: File? = null
            try {
                mFilePhoto = createImageFile()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (mFilePhoto != null) {
                val photoURI = FileProvider.getUriForFile(this, "me.ripzery.mergy.fileprovider", mFilePhoto)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun galleryAddPic() {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(mCurrentPhotoPath)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        this.sendBroadcast(mediaScanIntent)
    }

    private fun adjustOrientation(bitmap: Bitmap): Bitmap {
        val ei = ExifInterface(mCurrentPhotoPath)
        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED)

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
            ExifInterface.ORIENTATION_NORMAL -> bitmap
            else -> bitmap
        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height,
                matrix, true)
    }

    private fun setPic() {
        // Get the dimensions of the View
        val targetW = ivGreenPhoto.width
        val targetH = ivGreenPhoto.height

        // Get the dimensions of the bitmap
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight

        // Determine how much to scale down the image
        val scaleFactor = Math.min(photoW / targetW, photoH / targetH)

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor
        bmOptions.inPurgeable = true

        val bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)

        val adjustAngleBitmap = adjustOrientation(bitmap)

        ivGreenPhoto.setImageBitmap(adjustAngleBitmap)
    }


    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath()
        return image
    }
}
