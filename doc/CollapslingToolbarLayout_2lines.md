### CollapsingToolbarLayout 2줄 타이틀
> 가끔 CoordinatorLayout을 사용하는 화면에서 2줄이상의 타이틀을 구현해야 할 때가 있다.

1. CollapsingToolbarLayout에서 app:maxLines="2"로 정의
2. app:collapsedTitleTextAppearance는 접혀졌을 때 보여지는 title 스타일
3. app:expandedTitleTextAppearance는 펼쳐졌을 때 보여지는  title 스타일

~~~xml
<com.google.android.material.appbar.CollapsingToolbarLayout
            android:id="@+id/toolbar_layout"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:fitsSystemWindows="true"
            android:minHeight="60dp"
            app:maxLines="2"
            android:background="@color/transparent"
            app:layout_scrollFlags="scroll|exitUntilCollapsed"
            app:collapsedTitleTextAppearance="@style/Toolbar.TitleSmallText"
            app:expandedTitleTextAppearance="@style/Toolbar.TitleText"
            app:toolbarId="@+id/toolbar">

            <View
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:background="@color/windowBackground"
                android:fitsSystemWindows="true"
                android:scaleType="centerCrop"
                app:layout_collapseMode="parallax" />

            <androidx.appcompat.widget.Toolbar
                android:id="@+id/toolbar"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                app:layout_collapseMode="pin"

                >

            </androidx.appcompat.widget.Toolbar>

        </com.google.android.material.appbar.CollapsingToolbarLayout>
~~~

style.xml
~~~xml
    <style name="Toolbar.TitleText" parent="TextAppearance.Widget.AppCompat.Toolbar.Title">
        <item name="android:textSize">24dp</item>
        <item name= "android:lines">2</item>
    </style>

    <style name="Toolbar.TitleSmallText" parent="TextAppearance.Widget.AppCompat.Toolbar.Title">
        <item name="android:textSize">18dp</item>
        <item name= "android:ellipsize">end</item>
    </style>

~~~
사용법은 recycler를 생성 후, setMagneticMove를 호출해주기만 하면된다. 