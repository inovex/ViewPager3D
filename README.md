
# ViewPager 3D

![Screenshot 1](https://github.com/inovex/ViewPager3D/raw/master/screenshot1.png)
![Screenshot 1](https://github.com/inovex/ViewPager3D/raw/master/screenshot2.png)
![Screenshot 1](https://github.com/inovex/ViewPager3D/raw/master/screenshot3.png)

This project aims to provide a reusable ViewPager widget for Android. It is based on the ViewPager class from Androids V4 compatibility package. Because of that android-support-v4.jar needs to be included in the build path.

## Dependencies

 * android-support-v4.jar
 * Animations are handled by https://github.com/JakeWharton/NineOldAndroids. Add NineOldAndrois as a library project

## Features

 * 3D overscroll effect
 * 3D swipe effect 
 * subtle fade out during swipe and over scroll

## Usage

### Layout

``` xml
<!--
  The ViewPager3d replaces a standard android.support.v4.view.ViewPager widget.
-->
<de.inovex.android.widgets.ViewPager3D
    android:id="@+id/view_pager"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

You can control the 3D effect with XML attributes:

``` xml
<de.inovex.android.widgets.ViewPager3D
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/view_pager"
    app:overscroll_rotation ="1.75"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```
All attributes are optional.

 * **overscroll_rotation** (float): Determines the amount of rotation during over scroll. Maximum rotation angle is 90 degrees divided by this value. Default is 2 
 * **overscroll_translation** (integer): determines the maximum amount of translation along the z-axis during the overscroll. Default is 150.
 * **swipe_rotation** (float). Controls maximum rotation during swipe. Maximum rotation angle is 90 degrees divided by this value. Default is 3.
 * **swipe_translation** (integer): Maximum z-translation during swipe. Default = 100.
 * **overscroll_animation_duration** (integer): Duration of animation when user releases the over scroll. Default is 400 ms.
 * **animate_alpha** (boolean): if true the alpha value of the children views is decreased as they scroll out of the screen. Default is false because of performance issues.


### Activity

ViewPager3D is used like the standard ViewPager widget.
<http://developer.android.com/reference/android/support/v4/view/ViewPager.html>

## License

Licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
