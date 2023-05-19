# PinView

[![](https://jitpack.io/v/nokhyun/PinView.svg)](https://jitpack.io/#nokhyun/PinView)

settings.gradle
```
dependencyResolutionManagement {
    ...
    repositories {
        google()
        maven { url 'https://jitpack.io' }
        mavenCentral()
    }
}
```
build.gradle(app)
```
dependencies {
  ...
	implementation 'com.github.nokhyun:PinView:Tag'
}
```

usage
```
// viewBinding, dataBinding
  binding.pinView.initPinCodeSetting("fileName")
// findViewById
  findViewById<PinView>(R.id.pinView).initPinCodeSetting("fileName")
  
// savePinCode
  savePinCode(key, value) // valueType - (string, list, array)
 
 onSuccess = { // TODO Success }
 onFauilre = { // TODO PinCode Error }
```

attribute
```
<com.nokhyun.pinview.PinView
    android:id="@+id/pinView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    ...
    app:pinLength="4"
    app:initializationCount="5"
    app:pinImage="@drawable/baseline_1x_mobiledata_24"/> 
    
    pinLength - PinCodeLength Setting
    initializationCount - pinCode initialization count
    pinImage - pinCode Input Image
```
