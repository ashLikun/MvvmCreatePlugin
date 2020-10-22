package ${packageName}.viewmodel

import com.ashlikun.core.mvvm.BaseViewModel
import ${showToast}
import ${httpCallbackHandle}
/**
 * @author　　: 李坤
 * 创建时间: ${mData}
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：${mvvmDesc}业务
 */
class ${vmName} : BaseViewModel(){

    override fun onCreate() {
        super.onCreate()
    }

    /**
    * 获取数据
    */
    fun getData() {
        val handle = HttpCallbackHandle[this]
        ApiLogin.api.aaa( handle) { result ->
            if(result.isSucceed){

            }else{
                result.showToast()
            }
        }
    }
}