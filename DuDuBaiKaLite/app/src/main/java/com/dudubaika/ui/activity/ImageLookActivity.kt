package com.dudubaika.ui.activity

import android.view.View
import com.dudubaika.R
import com.dudubaika.base.SimpleActivity
import com.dudubaika.util.ImageUtil
import kotlinx.android.synthetic.main.activity_image_look.*
import kotlinx.android.synthetic.main.item_look_img.view.*


/**
 * 图片查看界面
 */
class ImageLookActivity : SimpleActivity() {

    private var mList :ArrayList<String>?=null
    private var mPosotion :Int=0
    private var imgUrl:String?=null
    companion object {

        var LIST:String="list"
        var POSOTION:String="posotion"
        var IMGURL:String="imgurl"
    }
    override fun getLayout(): Int = R.layout.activity_image_look

    override fun initView() {
        defaultTitle="查看大图"
       /* val metric = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metric)
        val width = metric.widthPixels
       val height = metric.heightPixels/2

        val layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
//        mLinearLayout.setOrientation(LinearLayout.VERTICAL)
        bg_img.layoutParams = layoutParams*/

    }

    override fun initData() {
        mList  =intent.getStringArrayListExtra(LIST)
        mPosotion  =intent.getIntExtra(POSOTION,0)
        imgUrl  =intent.getStringExtra(IMGURL)
        if (null !=mList &&mList!!.size>0) {
           /* bg_img.setData(mList, ArrayList())
            bg_img.setAdapter(BGABanner.Adapter<ImageView, String> { banner, itemView, model, position ->
                ImageUtil.loadNoCache(mActivity, itemView, imgUrl+model.toString(), R.drawable.default_bank_logo)
            })*/

            val views = ArrayList<View>()
            if ( mList!!.isNotEmpty()) {
                for (item in mList!!) {
                    var viewItem = View.inflate(mActivity, R.layout.item_look_img, null)
                  ImageUtil.loadWithCache(mActivity,viewItem.iv,imgUrl+item,R.drawable.default_bank_logo)
                    views.add(viewItem!!)
                }
                bg_img.setData(views)
            }else{
                return
            }


            bg_img.currentItem = mPosotion
            bg_img.setDelegate { banner, itemView, model, position ->
                backActivity()
            }
        }
    }

}
