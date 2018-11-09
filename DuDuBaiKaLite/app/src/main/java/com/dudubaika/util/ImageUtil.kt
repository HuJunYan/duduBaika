package com.dudubaika.util

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.dudubaika.dagger.module.GlideApp

object ImageUtil {

    /**
     * 加载图片不缓存
     */
    fun load(context: Context, imageUrl: String, @DrawableRes placeholder: Int, view: ImageView?) {
        if (TextUtils.isEmpty(imageUrl)) {
            return
        }

        GlideApp.with(context).load(imageUrl).placeholder(placeholder).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        view?.setImageDrawable(resource)
                    }
                })

    }

    /**
     * 不使用缓存加载图片
     */
    fun loadNoCache(context: Context, imageView: ImageView, url: String, @DrawableRes errorImage: Int) {
        GlideApp.with(context)
                .load(url)
                .error(errorImage)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView)
    }

    /**
     * 不使用缓存加载图片
     */
    fun loadNoCache(activity: Activity, imageView: ImageView, url: String, @DrawableRes errorImage: Int) {
        GlideApp.with(activity)
                .load(url)
                .error(errorImage)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView)
    }

    /**
     * 不使用缓存加载图片
     */
    fun loadNoCache(fragment: Fragment, imageView: ImageView, url: String, @DrawableRes errorImage: Int) {
        GlideApp.with(fragment)
                .load(url)
                .error(errorImage)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView)
    }

    /**
     * 使用缓存加载图片
     */
    fun loadWithCache(context: Context, imageView: ImageView, url: String, @DrawableRes errorImage: Int) {
        GlideApp.with(context)
                .load(url)
                .error(errorImage)
                .into(imageView)
    }

    /**
     * 使用缓存加载图片
     */
    fun loadWithCache(activity: Activity, imageView: ImageView, url: String, @DrawableRes errorImage: Int) {
        GlideApp.with(activity)
                .load(url)
                .error(errorImage)
                .into(imageView)
    }

    /**
     * 使用缓存加载图片
     */
    fun loadWithCache(fragment: Fragment, imageView: ImageView, url: String, @DrawableRes errorImage: Int) {
        GlideApp.with(fragment)
                .load(url)
                .error(errorImage)
                .into(imageView)
    }

    /**
     * 加载Drawable目录下的图片
     */
    fun loadWithID(context: Context, imageView: ImageView, @DrawableRes image: Int) {
        GlideApp.with(context)
                .load(image)
                .into(imageView)
    }

}