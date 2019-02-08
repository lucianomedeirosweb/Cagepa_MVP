package br.ufs.projetos.gocidade.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Camera
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import br.ufs.projetos.gocidade.R
import br.ufs.projetos.gocidade.util.CameraUtil
import kotlinx.android.synthetic.main.fragment_cam_photo.view.*
import java.io.File

/**
 * Created by samila on 20/11/17.
 */
class CamPhotoFragment : Fragment , View.OnClickListener,
        ViewTreeObserver.OnGlobalLayoutListener{

    constructor() : super()

    lateinit var mPhotoPath : File
    lateinit var mTask : LoadImageTask
    var mImageWeight : Int = 0
    var mImageHeight : Int = 0
    lateinit var mImageViewPhoto : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        val photoPath = CameraUtil.getLastMedia(this!!.activity!!, CameraUtil.MIDIA_PHOTO)

        if (photoPath != null){
            mPhotoPath = File (photoPath)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater!!.inflate(R.layout.fragment_cam_photo, container, false)
        layout.btn_photo.setOnClickListener (this)
        layout.viewTreeObserver.addOnGlobalLayoutListener(this)
        mImageViewPhoto = layout.img_photo
        return layout
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == CameraUtil.REQUEST_PHOTO){
            loadImage ()
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.img_photo){
            if(ActivityCompat.checkSelfPermission(this!!.activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                openCamera ()
            }else {
                ActivityCompat.requestPermissions(this!!.activity!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
            }
        }
    }

    fun openCamera (){
        val mPhotoPath = CameraUtil.newMidia(CameraUtil.MIDIA_PHOTO)
        val it = Intent (MediaStore.ACTION_IMAGE_CAPTURE)
        it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoPath))
        startActivityForResult(it, CameraUtil.REQUEST_PHOTO)
    }

    fun loadImage (){
        if(mPhotoPath != null && mPhotoPath.exists()){
            if(mTask == null || mTask.getStatus() != AsyncTask.Status.RUNNING){
                mTask = LoadImageTask ()
                mTask.execute()
            }
        }
    }


    override fun onGlobalLayout() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1){
            view?.viewTreeObserver?.removeGlobalOnLayoutListener (this)
        }else {
            view?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
        }

        mImageWeight = mImageViewPhoto.width
        mImageHeight = mImageViewPhoto.height

        loadImage ()
    }

   inner class LoadImageTask : AsyncTask<Void, Void, Bitmap> (){


        override fun doInBackground(vararg params: Void?): Bitmap? {
            return CameraUtil.loadImage(
                    mPhotoPath,
                    mImageWeight,
                    mImageHeight   )
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            if (result != null){
                mImageViewPhoto.setImageBitmap(result)
                activity?.let { CameraUtil.saveLastMedia(it, CameraUtil.MIDIA_PHOTO, mPhotoPath.absolutePath) }
            }
        }
    }

}