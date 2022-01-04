package ${packageName}.viewmodel
import ${BaseListViewModel}
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
class ${vmName} : BaseListViewModel(){

    val mainData = get<List<String>>()

    override fun onCreate() {
        super.onCreate()
        getData(true)
    }

    /**
    * 获取数据
    */
    fun getData(isStart: Boolean) = launch {
        if (isStart) cleanPage()
        val handle = HttpUiHandle[this]
                .setLoadingStatus(this)
        ApiMain.api.test(handle)?.also { result ->
            if (result.isSucceed) {
                result.setPageHelp(pageHelp!!)
                if (isStart) {
                    clearData()
                }
                if (mainData.value == null) {
                    mainData.value = result.data as ArrayList<String>?
                } else {
                    mainData.value?.addAll(result.data!!)
                    mainData.value = mainData.value
                }
            }
        }
    }
}