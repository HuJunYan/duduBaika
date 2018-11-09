package com.dudubaika.ui.activity

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import com.dudubaika.R
import com.dudubaika.base.SimpleActivity
import com.dudubaika.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_money_talk.*
import kotlinx.android.synthetic.main.dialog_select.view.*
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.ImageView
import com.dudubaika.util.SystemPhotoUtils
import com.dudubaika.util.ToastUtil
import cn.finalteam.galleryfinal.GalleryFinal
import cn.finalteam.galleryfinal.model.PhotoInfo
import com.baoyz.actionsheet.ActionSheet
import com.dudubaika.base.BaseActivity
import com.dudubaika.base.TalkingDataParams
import com.dudubaika.event.WriteCompleteEvent
import com.dudubaika.presenter.contract.WriteMyTalkContract
import com.dudubaika.presenter.impl.WriteMyTalkPresenter
import com.dudubaika.ui.adapter.UserImgAdapter
import com.dudubaika.ui.view.MyGridLayoutManager
import com.tendcloud.tenddata.TCAgent
import org.greenrobot.eventbus.EventBus


/**
 * 发布文章
 */
class WriteMyTalkActivity : BaseActivity<WriteMyTalkPresenter>(), WriteMyTalkContract.View {

    private var imageView: ImageView? = null
    private var mUserImageAdapter: UserImgAdapter? = null
    private var imgList: MutableList<PhotoInfo>? = null
    private var type: String? = null
    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(url: String, msg: String) {
    }

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    private var requestCarmerCodes: Int = 100
    private var requestSystemImgCodes: Int = 101
    val IMAGE_UNSPECIFIED = "image/*"

    companion object {
        var TYPE = "type"
    }

    override fun getLayout(): Int = R.layout.activity_money_talk

    override fun initView() {
        iv_return.setOnClickListener {
            backActivity()
        }
        StatusBarUtil.setPaddingSmart(mActivity, home_money_tb)
        defaultTitle="发布帖子"

        add_img.setOnClickListener {
            //            showSelectDialog()
            if (null !=imgList && imgList!!.size<9) {
                openMulti()
            }else{
                ToastUtil.showToast(mActivity,"最多只能选择9张图片")
            }
        }
        getImageView()
    }

    override fun initData() {
        type = intent.getStringExtra(TYPE)
        imgList = ArrayList()
        tv_send.setOnClickListener {
            if (TextUtils.isEmpty(artice_title.text.toString().trim())) {
                ToastUtil.showToast(mActivity, "标题不能为空")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(artice_content.text.toString().trim())) {
                ToastUtil.showToast(mActivity, "内容不能为空")
                return@setOnClickListener
            }
            mPresenter.writeArtice(type!!, artice_title.text.toString().trim(), artice_content.text.toString().trim(), imgList)
        }
    }

    override fun writeComplete() {
        //发布成功


        TCAgent.onEvent(mActivity, TalkingDataParams.PUBLISH_DISCUSS, type)

        EventBus.getDefault().post(WriteCompleteEvent())
        backActivity()
    }

    /**
     * 显示选择本地图库，还是使用摄像头的对话框
     */
    private fun showSelectDialog() {
        val mDialog = Dialog(this, R.style.MyDialog)
        val mLayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = mLayoutInflater.inflate(R.layout.dialog_select, null, false)
        mDialog.setContentView(view)
        val layoutParams = view.getLayoutParams()
        layoutParams.width = (300 * resources.displayMetrics.density).toInt()
        view.layoutParams = layoutParams
        mDialog.setCancelable(true)
        view.tv_dialog_select_gallery.setOnClickListener {
            mDialog.dismiss()
        }
        view.tv_dialog_select_camera.setOnClickListener {
            mDialog.dismiss()
        }
        mDialog.show()
    }

    /**
     * 自定义多选
     */
    private fun openMulti() {

        SystemPhotoUtils.initGalleryFinal(mActivity, 9, false)

        ActionSheet.createBuilder(this, supportFragmentManager)
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("打开相册", "拍照")
                .setCancelableOnTouchOutside(true)
                .setListener(object : ActionSheet.ActionSheetListener {
                    override fun onOtherButtonClick(actionSheet: ActionSheet?, index: Int) {
                        when (index) {
                            0 ->  {
                                GalleryFinal.openGalleryMuti(requestSystemImgCodes, 9 - imgList!!.size, mOnHanlderResultCallback)
                            }
                            1 -> GalleryFinal.openCamera(requestCarmerCodes,  mOnHanlderResultCallback)

                            else -> {
                            }
                        }
                    }

                    override fun onDismiss(actionSheet: ActionSheet?, isCancel: Boolean) {
                    }
                })
                .show()
    }


    private var mOnHanlderResultCallback = object : GalleryFinal.OnHanlderResultCallback {
        override fun onHanlderSuccess(reqeustCode: Int, resultList: MutableList<PhotoInfo>?) {
            imgList?.addAll(resultList!!)
            if (null != mUserImageAdapter){
                mUserImageAdapter?.removeAllFooterView()
            }
            showUserImg()
        }

        override fun onHanlderFailure(requestCode: Int, errorMsg: String?) {
            ToastUtil.showToast(mActivity,"失败")
        }

    }

    fun getImageView(){
        imageView = ImageView(this)
        imageView?.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        imageView?.setImageResource(R.drawable.add_img2)
        imageView?.setOnClickListener({
            if (null !=imgList && imgList!!.size<9) {
                openMulti()
            }else{
                ToastUtil.showToast(mActivity,"最多只能选择9张图片")
            }
        })
    }


  fun showUserImg(){


      if (null==mUserImageAdapter){
          mUserImageAdapter  = UserImgAdapter(imgList,mActivity)

          val manager = MyGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
          manager.setScrollEnabled(true)
          recycleview_img.layoutManager =   manager
          recycleview_img.adapter = mUserImageAdapter
      }else{
          mUserImageAdapter?.notifyDataSetChanged()
      }
      if (!mUserImageAdapter!!.isFooterViewAsFlow){
          mUserImageAdapter?.addFooterView(imageView)
      }


      mUserImageAdapter?.setOnItemChildClickListener { adapter, view, position ->

          imgList?.removeAt(position)
          mUserImageAdapter?.notifyDataSetChanged()
          if (imgList!!.size==0){
              mUserImageAdapter?.removeAllFooterView()
          }
      }
  }

}
