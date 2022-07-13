package ${packageName}.viewmodel
import androidx.lifecycle.MutableLiveData
import com.ashlikun.core.mvvm.BaseViewModel
import  com.ashlikun.core.mvvm.launch
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

    val mainData = get<String>()

    override fun onCreate() {
        super.onCreate()
        getData()
    }

    /**
    * 获取数据
    */
    fun getData() = launch {
        val handle = HttpUiHandle[this]
        ApiMain.api.test(handle)?.also { result ->
            if (result.isSucceed) {
                clearData()
                mainData.value = result.data
            }
        }
    }
}