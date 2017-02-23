# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android-studio\android-sdk\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**
-dontwarn org.xmlpull.v1.XmlPullParser
-dontwarn org.xmlpull.v1.XmlSerializer
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class net.sf.json.** {*;}
-keep class org.apache.http.entity.** { *; }
-keep class nu.xom.** { *; }
-keep class com.thoughtworks.xstream.** { *; }