package com.dudubaika.util

import com.dudubaika.base.App
import com.dudubaika.log.LogUtil
import com.dudubaika.model.bean.MyHttpResponse
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.getStackTraceString

object RxUtil {

    private val ERROR_LOG_TAG: String = "http"

    /**
     * 统一线程处理
     * @param <T>
     * @return
    </T> */
    fun <T> rxSchedulerHelper(): FlowableTransformer<T, T> {    //compose简化线程
        return FlowableTransformer<T, T> {
            it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    /**
     * 处理返回结果
     */
    fun <T> handleResult(): FlowableTransformer<MyHttpResponse<T>, T> {
        return FlowableTransformer<MyHttpResponse<T>, T> {
            it.flatMap {
                createData(it.data)
            }
        }
    }

    /**
     * 生成Flowable
     * @param <T>
     * @return
    </T> */
    fun <T> createData(t: T): Flowable<T> {
        return Flowable.create({ emitter ->
            try {
                if (t != null) {
                    emitter.onNext(t)
                } else {
                    ToastUtil.showToast(App.instance, "服务器数据错误")
                }
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
                ToastUtil.showToast(App.instance, "服务器数据错误")
                LogUtil.d(ERROR_LOG_TAG, e.getStackTraceString())
            }
        }, BackpressureStrategy.BUFFER)
    }

}