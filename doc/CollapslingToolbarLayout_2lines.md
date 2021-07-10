### CollapsingToolbarLayout 2줄 타이틀
> 가끔 CoordinatorLayout을 사용하는 화면에서 2줄이상의 타이틀을 구현해야 할 때가 있다.

1. CollapsingToolbarLayout에서 app:maxLines="2"로 정의
2. app:collapsedTitleTextAppearance는 접혀졌을 때 보여지는 title 스타일
3. app:expandedTitleTextAppearance는 펼쳐졌을 때 보여지는  title 스타일

example4.xml
~~~xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.coordinatorlayout.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true">

    <com.google.android.material.appbar.AppBarLayout
        android:id="@+id/app_bar"
        android:layout_width="match_parent"
        android:layout_height="240dp"
        android:fitsSystemWindows="true"
        >

        <com.google.android.material.appbar.CollapsingToolbarLayout
            android:id="@+id/toolbar_layout"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:fitsSystemWindows="true"
            android:minHeight="80dp"
            app:maxLines="2"
            app:layout_scrollFlags="scroll|exitUntilCollapsed"
            app:collapsedTitleTextAppearance="@style/Toolbar.TitleSmallText"
            app:expandedTitleTextAppearance="@style/Toolbar.TitleText"
            app:toolbarId="@+id/toolbar">

            <View
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:background="#79616557"
                android:fitsSystemWindows="true"
                android:scaleType="centerCrop"
                app:layout_collapseMode="parallax" />

            <androidx.appcompat.widget.Toolbar
                app:title="2줄로 \n 타이틀표시 타이틀표시 타이틀표시 타이틀표시 타이틀표시"
                android:id="@+id/toolbar"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                app:layout_collapseMode="pin"
                >

            </androidx.appcompat.widget.Toolbar>

        </com.google.android.material.appbar.CollapsingToolbarLayout>


    </com.google.android.material.appbar.AppBarLayout>

    <include android:id="@+id/main_scroll" layout="@layout/example_4_1" />

</androidx.coordinatorlayout.widget.CoordinatorLayout>
~~~

example4_1.xml
~~~xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.core.widget.NestedScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:layout_behavior="@string/appbar_scrolling_view_behavior"
    tools:showIn="@layout/example_4">

    <LinearLayout
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <TextView
            android:gravity="top|center"
            android:layout_width="match_parent"
            android:layout_height="900dp"
            android:textStyle="normal"
            android:textSize="36dp"
            android:textColor="#b0b3b8"
            android:text="\n 👆위로 밀어주세요"
            />

    </LinearLayout>

</androidx.core.widget.NestedScrollView>
~~~

style.xml
~~~xml
<resources>
    <style name="Toolbar.TitleText" parent="TextAppearance.Widget.AppCompat.Toolbar.Title">
        <item name="android:textSize">24dp</item>
        <item name= "android:lines">2</item>
        <item name="android:textColor">#FFF7F5</item>
    </style>

    <style name="Toolbar.TitleSmallText" parent="TextAppearance.Widget.AppCompat.Toolbar.Title">
    <item name="android:textSize">18dp</item>
    <item name= "android:ellipsize">end</item>
    </style>
</resources>
~~~

example은 MainActivity에 있음.

~~~kotlin
    private fun testAppbarlayout2Lines() {
        QuickExampleActivity.launch(this, { setContent ->
            val binding: Example4Binding
            binding = Example4Binding.inflate(layoutInflater)
            setContent(binding.root)
        })
    }
~~~
