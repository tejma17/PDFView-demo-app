# PDFView-demo-app

Demo application to show how to fetch PDF file hosted on Google Drive and display it in built-in PDF viewer for android.


### Updated Files
1. build.gradle
```
implementation 'com.github.barteksc:android-pdf-viewer:3.2.0-beta.1'
```

2. proguard-rules.pro
```
-keep class com.shockwave.**
```

3. Add PDFView in layout and necessary code in activity/fragment file.

For more details, view [Android PDF Viewer](https://github.com/barteksc/AndroidPdfViewer) library
