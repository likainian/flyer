dependencies {
        ...
        classpath "com.mob.sdk:MobSDK:2018.0319.1724"

    }
主module和使用到的有关联的module都加一下mob的引用“apply plugin: 'com.mob.sdk'”
1.在Application的OnCreate添加代码：
    MobSDK.init(this);