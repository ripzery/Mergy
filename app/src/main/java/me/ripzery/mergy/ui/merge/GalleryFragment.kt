package me.ripzery.mergy.ui.merge

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
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
import me.ripzery.mergy.viewgroups.BackgroundImageGroup

class GalleryFragment : Fragment() {
    private lateinit var mBackgroundDataList: ArrayList<Response.Photo>
    private var mListener: BackgroundImageGroup.OnImageSelectedListener? = null
    private lateinit var mAdapter: GalleryRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mBackgroundDataList = arguments!!.getParcelableArrayList<Response.Photo>(LIST_BACKGROUND_DATA)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mAdapter = GalleryRecyclerAdapter(mBackgroundDataList, mListener)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is BackgroundImageGroup.OnImageSelectedListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement me.ripzery.mergy.ui.merge.GalleryFragment.OnImageSelectedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    inner class GalleryRecyclerAdapter(private val mBitmapList: ArrayList<Response.Photo>, val onBackgroundSelectListener: BackgroundImageGroup.OnImageSelectedListener?) : RecyclerView.Adapter<GalleryRecyclerAdapter.GalleryViewHolder>() {
        override fun onBindViewHolder(holder: GalleryViewHolder?, position: Int) {
            holder?.setData(mBitmapList[position])
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GalleryViewHolder {
            val rootView = LayoutInflater.from(parent!!.context).inflate(R.layout.viewgroup_layout_background, parent, false)
            return GalleryViewHolder(rootView)
        }

        override fun getItemCount() = mBitmapList.size
        inner class GalleryViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {
            fun setData(backgroundData: Response.Photo) {
                with(rootView.backgroundImageGroup) {
                    logd(backgroundData.toString())
                    setImageBackground(backgroundData)
                    setOnBackgroundChangeListener(onBackgroundSelectListener)
                }
            }
        }
    }

    companion object {
        private val LIST_BACKGROUND_DATA = "param1"
        fun newInstance(listBackgroundData: ArrayList<Response.Photo>): GalleryFragment {
            val fragment = GalleryFragment()
            val args = Bundle()
            args.putParcelableArrayList(LIST_BACKGROUND_DATA, listBackgroundData)
            fragment.arguments = args
            return fragment
        }
    }
}
