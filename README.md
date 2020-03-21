
本分支只是为了解决windows机器使用JIMU框架上遇到的无法删除问题；组件化原理请参考原著：地址：
https://github.com/mqzhangw/JIMU


1.windows出现Could not delete path ... app\build\intermediates\transforms\desugar\debug\0.jar错误，根据原因分析得出是Javassist修改类方法时导致Java程序无法释放问题导致的；

2.集合AutoRegister方案，使用效率更高的ASM框架来进行字节码分析和修改，非常感谢作者，原著地址：https://github.com/luckybilly/AutoRegister；

3.具体实现步骤：
1.替换原有项目中com.github.jimu:build-gradle:1.*.*为com.smallbuer:build-gradle:1.0.5，已上传到jcenter仓库；

2.在app主module中新建类AppCompCore.class，主要用于AMS修改时找到需要插入注册组件的代码；代码内容如下：


```
package com.luojilab.componentdemo.application;

import android.util.Log;

import com.luojilab.component.componentlib.applicationlike.IApplicationLike;

public class AppCompCore {

    public static void rigisterComp(IApplicationLike iApplicationLike){
        Log.d("asm---",""+iApplicationLike.getClass().getSimpleName());
        iApplicationLike.onCreate();
    }

    public static void initComp(){

    }

}
```

然后在主module中的build.gradle中添加自动注册的代码；

```
autoregister {

    registerInfo = [

            [
                    'scanInterface'             : 'com.luojilab.component.componentlib.applicationlike.IApplicationLike'
                    , 'codeInsertToClassName'   : 'com.luojilab.componentdemo.application.AppCompCore'
                    , 'codeInsertToMethodName'  : 'initComp' 
                    , 'registerMethodName'      : 'rigisterComp' 
            ]
    ]
}
```
3.然后删除所有module中的combuild字段；


```
//combuild {
//    applicationName = 'com.luojilab.componentdemo.application.AppApplication'
//    isRegisterCompoAuto = true
//}
```

4.然后编译，在编译过程中会扫描出所有集成IApplicationLike的类，然后在AppCompCore类中initComp方法中动态插入该类的实例并实现rigisterComp方法，所以在编译期就自动执行了组件中的onCreate()方法；

5.以上操作只是修改了每一个组件自动注入执行onCreate方法的方式，并不修改其他地方；所以不需要有其他的改动；只是为windows机器无法删除的bug的一种解决方案；

6.继续支持组件的单独打包编译；

