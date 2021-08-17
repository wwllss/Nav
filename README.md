# Nav文档
## 接入方法
**LastestVersion = "1.0.0"**

```JAVA
//root build.gradle 配置
dependencies {
	classpath 'zy.nav:register:$LastestVersion'
}

//所有 module 配置
apply plugin: 'zy.nav.register'
```
## 路由功能
### 基本使用
```JAVA
//注册Activity
//——注解方式
@Route(URL)
public static class OneActivity extends Activity {···}
//——原生方式
<activity android:name=".OneActivity">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data
            android:host="host"
            android:path="/path"
            android:scheme="scheme" />
    </intent-filter>
</activity>

//跳转Activity
Nav.from(Context|Activity|Fragment).to(URL)

//跳转Activity——forResult
Nav.from(Context|Activity|Fragment).to(URL,REQUEST_CODE)


//注册Fragment
@Route(URL)
public static class OneFragment extends Fragment {···}

//获取Fragment
Nav.from(Context|Activity|Fragment).getFragment(URL)
```
**注：**

* Route注解注册或即将跳转的URL在组件内部只通过Path匹配，与URL的scheme、host、params等无关，即注册或跳转时传入全URL如`scheme://host/path?params=xxx#fragment`，**生效的也只有/path(以`/`开头)**

* Activity原生注册请采用全URL配置，如上述代码`<intent-filter/>`标签内所示。**注意！！！只在注解注册匹配不到时，才会进行原生注册匹配。**

* Google已废弃android.app.Fragment，所以只支持android.support.v4.app.Fragment

* `Nav.from(Context|Activity|Fragment).to(URL,REQUEST_CODE)`跳转Activity并请求返回的时，`from(Activity)`将回调`Activity.onActivityResult()`方法；`from(Fragment)`将回调`Fragment.onActivityResult()`方法；

### 拦截器
#### 单次请求添加
```JAVA
Nav.from(MainActivity.this)
        .withInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) {
                Request request = chain.request();
                //做请求前的操作
                Response response = chain.process(request);
                //做请求后的操作
                return response;
            }
        })
```
#### 指定路由拦截
```JAVA
@Intercept(priority = 优先级, token = 路由)
public class SubInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) {
        Request request = chain.request();
        //做请求前的操作
        Response response = chain.process(request);
        //做请求后的操作
        return response;
    }
}
```
#### 全局添加
```JAVA
@Intercept(priority = 优先级)
public class SubInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) {
        Request request = chain.request();
        //做请求前的操作
        Response response = chain.process(request);
        //做请求后的操作
        return response;
    }
}
```
**注：**

* 中断本次操作——`request.intercept(错误提示信息)`

* 重定向URL——`request.redirect(重定向URL)`

### 参数及自动注入
#### 可携带参数
```JAVA
Nav.from(Context|Activity|Fragment)
	//intent flag
	.addFlag(flags)				
	//新版动画			
	.withOptions(ActivityOptionsCompat)	
	//Bundle		
	.withAll(Bundle)	
	//boolean			
	.withBoolean(boolean)	
	//byte			
	.withByte(byte)	
	//char			
	.withChar(char)	
	//short		
	.withShort(short)	
	//int		
	.withInt(int)	
	//long			
	.withLong(long)	
	//float			
	.withFloat(float)	
	//double			
	.withDouble(double)	
	//string		
	.withString(string)	
	//object		
	.withObject(object)
```
#### 参数自动注入
```JAVA
    @Arg
    boolean aBoolean;

    @Arg
    byte aByte;

    @Arg
    short aShort;

    @Arg
    int anInt;

    @Arg
    long aLong;

    @Arg
    float aFloat;

    @Arg
    double aDouble;

    @Arg
    char aChar;

    @Arg
    Boolean aBoxBoolean;

    @Arg
    Byte aBoxByte;

    @Arg
    Short aBoxShort;

    @Arg
    Integer anBoxInt;

    @Arg
    Long aBoxLong;

    @Arg
    Float aBoxFloat;

    @Arg
    Double aBoxDouble;

    @Arg
    Character aBoxChar;

    @Arg
    String secondFragName;

    @Arg("obj")
    Object secondFragObj;

    @Arg
    List<String> secondFragList;

    @Arg
    Map<String, Object> secondFragMap;

    @Arg("gaoShiQing")
    Map<String, List<String>> listMap;
```
**注：**

* `Arg`注解可以配置别名，但要确保与参数的`key`一致。

* `Object`类型的参数携带和自动注入需要依赖`JsonMarshaller`服务的实现，详情请见下面

## 模块通信功能
```JAVA
---------------接口
@Service(Parent.class)
public class ParentImpl1 implements Parent {···}

@Service(Parent.class, token = 实现类唯一标识)
public class ParentImpl2 implements Parent {···}

@Service(Parent.class, token = 实现类唯一标识)
public class ParentImpl3 implements Parent {

	public ParentImpl3(构造入参) {···}

}

//使用
Parent service = Nav.getService(Parent.class);
Parent service = Nav.getService(Parent.class,实现类唯一标识);
Parent service = Nav.getService(Parent.class,实现类唯一标识,构造入参);
if (service != null) {
	service.doSomeThing();
}

---------------基类

@Service(Parent.class)
public class Child1 extends Parent {···}

@Service(Parent.class, token = 实现类唯一标识)
public class Child2 extends Parent {···}

@Service(Parent.class, token = 实现类唯一标识)
public class Child3 extends Parent  {

	public Child3(构造入参) {···}

}

//使用
Parent service = Nav.getService(Parent.class);
Parent service = Nav.getService(Parent.class,实现类唯一标识);
Parent service = Nav.getService(Parent.class,实现类唯一标识,构造入参);
if (service != null) {
	service.doSomeThing();
}

```
## 手动注册功能
```JAVA
public static void register(ActivityRegister register)

public static void register(FragmentRegister register)

public static void register(ServiceRegister register)

public static void register(InterceptorRegister register)
```

## 总结
* 所有需要注解的地方会由插件完成自动注册