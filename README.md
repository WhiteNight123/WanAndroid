# 仿WanAndroid
## 前言
2022红岩移动寒假考核

## 简介
采用单**Activity多Fragment**  
为什么要采用这种架构?我觉得单activity更轻量,而且也不会MVP,MVVM架构,就简单写写来完成考核(因为太菜了).  
后来发现fragment的**坑**太多了  

fragment的重叠,嵌套,穿透,白屏,fragment之间的通信非常难实现,fragment的生命周期,fragment的回退栈bug,fragment的toolbar和activity的toolbar的坑,fragment和输入法的坑...

## 功能
<img src="演示动画.gif" alt="总体展示" title="总体展示"   />

<img src="https://gitee.com/heihun666/WanAndroid/raw/master/picture/home.jpg" alt="首页" title="首页" width="180" height="400"/>

主界面用 **ToolBar+BottomNavigationView + ViewPager2 + Fragment**  
用**SwipeRefreshLayou**t实现下拉刷新  
banner是**ViewPager2**,最外层也是ViewPager2,这里出现了滑动冲突,外层的ViewPager2消耗了滑动,导致内部的banner不能滑动,通过**自定义View**套在两层ViewPager2中间拦截滑动,当内部滑动完后外部才能滑动

<img src="https://gitee.com/heihun666/WanAndroid/raw/master/picture/search.jpg" alt="搜索" title="搜索" width="180" height="400"/>

搜索界面 **Toolbar+SearchView+recycleView**(热搜)

<img src="https://gitee.com/heihun666/WanAndroid/raw/master/picture/login.jpg" alt="登录" title="登录" width="180" height="400"/>

直接用之前作业写得界面  
用**SharedPreferences**储存的cookie,实现自动登录

<img src="https://gitee.com/heihun666/WanAndroid/raw/master/picture/my.jpg" alt="我的" title="我的" width="180" height="400" />

<img src="https://gitee.com/heihun666/WanAndroid/raw/master/picture/myrank.jpg" alt="我的积分" title="我的积分" width="180" height="400"/>

用**LinearProgressIndicator**显示进度条

<img src="https://gitee.com/heihun666/WanAndroid/raw/master/picture/tool.jpg" alt="工具" title="工具" width="180" height="400"/>

用**NestedScrollView**包裹实现滑动  

<img src="https://gitee.com/heihun666/WanAndroid/raw/master/picture/webview.jpg" alt="网页" title="网页" width="180" height="400"/>

用**WebView**展示网页,**FloatingActionButton**实现返回  

所以的界面都是在**fragment**上,适配了**黑夜模式**  

实现了WanAndroid的部分功能(**首页,登录,搜索,积分**)  

待实现功能:**收藏,上拉加载,体系导航**  

本次的网络请求也是之前作业封装的**HttpURLConnection** , **JSON**是**手动**解析的

本次使用的库只有**Glide**,**Material**和**Androidx** .

## 心得
遇到无数的**bug**,心态崩了,然后去找百度,每当解决了一个bug内心就无比激动,就这样坚持下来了,勉强写了一个APP  
不足之处:
1. 代码 命名 注释 非常糟糕  
2. UI丑陋  
3. 没有用到高级的东西(**Kotlin,自定义View,MVP,MVVM**),接下来我将学习这些知识  
  最后:**感谢红岩网校,让我度过了一个有意义的寒假👏👏👏**

