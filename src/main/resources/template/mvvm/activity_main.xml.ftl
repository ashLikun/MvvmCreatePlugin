<?xml version="1.0" encoding="utf-8"?>

<!--${mvvmDesc}-->
<androidx.constraintlayout.widget.ConstraintLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:layout_width="match_parent"
        android:layout_height="match_parent">


    <com.ashlikun.supertoobar.SuperToolBar
            android:id="@+id/toolbar"
            style="@style/SuperToolBar.Main"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toTopOf="parent"/>

</androidx.constraintlayout.widget.ConstraintLayout>