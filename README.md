# 背景
跨页面通信是一个比较常见的场景，通常我们会选择使用`EventBus`，但`EventBus`无法感知声明周期，收到消息就会回调，所以有了`LiveData`之后很快就有了`LiveEventBus`。不过它也有缺点，比如不能切换线程。现在`SharedFlow`稳定了，那是不是也能搞一波？  

于是有了`FlowEventBus`

# 常用消息总线对比

消息总线 | 延迟发送 | 有序接收消息 | Sticky | 生命周期感知 | 跨进程/APP | 线程分发
---|---|---|---|---|---|---
EventBus | ❌ | ✅ | ✅ | ❌ | ❌ | ✅
RxBus | ❌ | ✅ | ✅ | ❌ | ❌ | ✅
LiveEventBus | ✅ | ✅ | ✅ | ✅ | ✅ | ❌
FlowEventBus| ✅ | ✅ | ✅ | ✅ |❌| ✅ | 


# 设计构思
通过学习 [从 LiveData 迁移到 Kotlin 数据流](https://mp.weixin.qq.com/s/o61NDIptP94X4HspKwiR2w)  得到思路：
- SharedFlow作为事件载体 ：  
优点：  
-  依托协程轻松切换线程
-  可以通过replay实现粘性效果
-  可以被多个观察者订阅
-  无观察者自动清除事件不会造成积压


结合 Lifecycle 感知生命周期，做到响应时机可控 。

不仅可以全局范围的事件，也可以单页面内的通信而不透传到别的页面，如：Activity内部，Fragment内部通信。

# 依赖库版本
关键在于 `kotlinx-coroutines > 1.4.x`  和 `lifecycle-runtime-ktx > 2.3.x`
# API

> 以下示例中的XEvent均是随意定义的类，只是测试时为了区分事件而定义的名字
## 事件发送

```kotlin
//全局范围
postEvent(AppScopeEvent("form TestFragment"))

//Fragment 内部范围 
postEvent(fragment,FragmentEvent("form TestFragment"))

//Activity 内部范围
postEvent(requireActivity(),ActivityEvent("form TestFragment"))
```

## 事件监听

```kotlin
//接收 Activity Scope事件
observeEvent<ActivityEvent>(scope = requireActivity()) {
    ...
}

//接收 Fragment Scope事件
observeEvent<FragmentEvent>(scope = fragment) {
    ...
}

//接收 App Scope事件
observeEvent<AppScopeEvent> {
    ...
}

```
## Like ObserveForever：
```kotlin
//此时需要指定协程范围
observeEvent<GlobalEvent>(scope = coroutineScope) {
       ...
}
```
## 延迟发送
```kotlin
postEvent(CustomEvent(value = "Hello Word"),1000)
```
## 线程切换
```kotlin
observeEvent<ActivityEvent>(Dispatchers.IO) {
    ...
}
```
## 指定可感知的最小生命状态
```kotlin
observeEvent<ActivityEvent>(minActiveState = Lifecycle.State.DESTROYED) {
   ...
}
```
## 以粘性方式监听
```kotlin
observeEvent<GlobalEvent>(isSticky = true) {
   ...
}
```
## 删除粘性事件
移除粘性事件实例
```kotlin
    removeStickyEvent(StickyEvent::class.java)
    removeStickyEvent(fragment,StickyEvent::class.java)
    removeStickyEvent(activity,StickyEvent::class.java)
```
## 清除粘性事件
粘性事件实例还在，但没有了`ReplayCache`,新观察者不会收到回调
```kotlin
    clearStickyEvent(GlobalEvent::class.java)
```
## 获取事件监听者数量
```kotlin
    getEventObserverCount(GlobalEvent::class.java)
```
# 原理
 以上功能依托于Kotlin协程的`SharedFlow`和`Lifecycle` 因此实现起来非常简单。 
- 粘性事件
```kotlin
MutableSharedFlow<Any>(
    replay = if (isSticky) 1 else 0,
    extraBufferCapacity = Int.MAX_VALUE //避免挂起导致数据发送失败
)
```
- 生命周期感知
```kotlin
fun <T> LifecycleOwner.launchWhenStateAtLeast(
    minState: Lifecycle.State,
    block: suspend CoroutineScope.() -> T
) :Job {
    lifecycleScope.launch {
        lifecycle.whenStateAtLeast(minState, block)
    }
}
```
- 自行取消监听  
  观察事件方法返回一个`Job` 使用方自行可以自行控制是否还继续监听
```kotlin
    val job = obserEvent<Event>{
        ...
    }
    //取消监听
    job.cancel()
```

- 切换线程
`whenStateAtLeast` 由于执行的`block`默认是在主线程，因此需要手动切换线程：
```kotlin
lifecycleOwner.launchWhenStateAtLeast(minState) {
    flow.collect { value ->
        lifecycleOwner.lifecycleScope.launch(dispatcher) {
                onReceived.invoke(value as T)
        }
    }
}
```
- 延迟事件
```kotlin
viewModelScope.launch {
    delay(time)
    flow.emit(value)
}
```
- 有序分发  
`Flow`本身就是有序的

- 全局单例  
使用全局`ViewModel`，主要是因为有`ViewModelScope`，可以避免使用`GlobalScope`，如果想要单页面内部组件通信，那就使用ActivityScope的ViewModel就行了：

```kotlin
object ApplicationScopeViewModelProvider : ViewModelStoreOwner {

    private val eventViewModelStore: ViewModelStore = ViewModelStore()

    override fun getViewModelStore(): ViewModelStore {
        return eventViewModelStore
    }

    private val mApplicationProvider: ViewModelProvider by lazy {
        ViewModelProvider(
            ApplicationScopeViewModelProvider,
            ViewModelProvider.AndroidViewModelFactory.getInstance(EventBusInitializer.application)
        )
    }

    fun <T : ViewModel> getApplicationScopeViewModel(modelClass: Class<T>): T {
        return mApplicationProvider[modelClass]
    }
}
```
  
  ViewModel内部有2个`map`，分别是粘性和非粘性：

```kotlin

internal class EventBusViewModel : ViewModel() {

    private val eventFlows: HashMap<String, MutableSharedFlow<Any>> = HashMap()
   
    private val stickyEventFlows: HashMap<String, MutableSharedFlow<Any>> = HashMap()
    ...

}
```
# 总结

站在巨人的肩膀上的同时也可以简单了解下原理。不过挺复杂的，需要下点功夫😄  

[kotlinx.coroutines.flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/index.html)


# 使用
Add it in your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency
```
dependencies {
	        implementation 'com.github.biubiuqiu0:flow-event-bus:0.0.2'
	}
```

  
  Step 3. Init
  
```kotlin
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        EventBusInitializer.init(this)
    }
}
```


### License
```
MIT License

Copyright (c) 2021 Compose-Museum

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```
