package me.ripzery.mergy.ui.merge.gallery

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.viewgroup_layout_background.view.*
import me.ripzery.mergy.R
import me.ripzery.mergy.extensions.logd
import me.ripzery.mergy.network.Response

class GalleryFragment : Fragment() {
    private val mGalleryViewModel: GalleryViewModel by lazy { ViewModelProviders.of(activity!!).get(GalleryViewModel::class.java) }
    private lateinit var mAdapter: GalleryRecyclerAdapter
    private var mMode: Int = GalleryFragment.LANDSCAPE_MODE
    private var mCurrentList: ArrayList<Response.Photo> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mAdapter = GalleryRecyclerAdapter(arrayListOf())
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mGalleryViewModel.retrieveGallery().observe(this, Observer { photoList ->
            if (photoList != null) {
                mCurrentList.addAll(photoList)
                mAdapter.addPhotos(ArrayList(mCurrentList.filter { it.imageType == mMode }))
            }
        })
        btnLandscape.setOnClickListener { changeBGMode(LANDSCAPE_MODE) }
        btnPanorama.setOnClickListener { changeBGMode(PANORAMA_MODE) }
    }

    private fun changeBGMode(mode: Int) {
        mMode = mode
        when (mode) {
            GalleryFragment.PANORAMA_MODE -> {
                btnPanorama.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
                btnLandscape.setTextColor(ContextCompat.getColor(context!!, android.R.color.white))
                btnPanorama.background = ContextCompat.getDrawable(context!!, R.drawable.btn_gallery_selected)
                btnLandscape.background = ContextCompat.getDrawable(context!!, R.drawable.btn_gallery_unselected)
            }
            GalleryFragment.LANDSCAPE_MODE -> {
                btnLandscape.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
                btnPanorama.setTextColor(ContextCompat.getColor(context!!, android.R.color.white))
                btnLandscape.background = ContextCompat.getDrawable(context!!, R.drawable.btn_gallery_selected)
                btnPanorama.background = ContextCompat.getDrawable(context!!, R.drawable.btn_gallery_unselected)
            }
        }
        updateGallery(mode)
    }

    private fun updateGallery(mode: Int) {
        mAdapter.clearPhotos()
        mAdapter.addPhotos(ArrayList(mCurrentList.filter { it.imageType == mode }))
    }


    inner class GalleryRecyclerAdapter(private val mBitmapList: ArrayList<Response.Photo>) : RecyclerView.Adapter<GalleryRecyclerAdapter.GalleryViewHolder>() {
        override fun onBindViewHolder(holder: GalleryViewHolder?, position: Int) {
            holder?.setData(mBitmapList[position])
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GalleryViewHolder {
            val rootView = LayoutInflater.from(parent!!.context).inflate(R.layout.viewgroup_layout_background, parent, false)
            return GalleryViewHolder(rootView)
        }

        override fun getItemCount() = mBitmapList.size

        fun addPhotos(photoList: ArrayList<Response.Photo>) {
            mBitmapList.addAll(photoList)
            notifyItemRangeInserted(0, photoList.size)
        }

        fun clearPhotos() {
            mBitmapList.clear()
            notifyDataSetChanged()
        }

        inner class GalleryViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {
            fun setData(backgroundData: Response.Photo) {
                with(rootView.backgroundImageGroup) {
                    logd(backgroundData.toString())
                    setImageBackground(backgroundData)
                    setGalleryViewModel(mGalleryViewModel)
                }
            }
        }
    }

    companion object {
        val LANDSCAPE_MODE = 1
        val PANORAMA_MODE = 2
        fun newInstance(): GalleryFragment {
            val fragment = GalleryFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
