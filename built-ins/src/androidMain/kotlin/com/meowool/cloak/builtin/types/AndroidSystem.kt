/*
 * Copyright (c) 2022. The Meowool Organization Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * In addition, if you modified the project, you must include the Meowool
 * organization URL in your code file: https://github.com/meowool
 *
 * 如果您修改了此项目，则必须确保源文件中包含 Meowool 组织 URL: https://github.com/meowool
 */
 
@file:JvmMultifileClass
@file:JvmName("BuiltInTypes")
@file:Suppress("ObjectPropertyName", "SpellCheckingInspection", "NewApi", "DEPRECATION")

/*
 * This file is automatically generated by [meowool-cloak](https://github.com/meowool-catnip/cloak/built-ins/generator).
 */
package com.meowool.cloak.builtin.types

import android.app.ActionBar
import android.app.Activity
import android.app.AlertDialog
import android.app.AppComponentFactory
import android.app.Dialog
import android.app.DialogFragment
import android.app.Fragment
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.res.AssetManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.Spannable
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.AbsListView
import android.widget.Adapter
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EdgeEffect
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.RemoteViews
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.FragmentManager
import com.meowool.cloak.Type
import com.meowool.cloak.type
import com.meowool.sweekt.LazyInit

/**
 * Represents the type of [Context] class.
 */
@LazyInit val _Context: Type<Context> = Context::class.java.type

/**
 * Represents the type of [ContentProvider] class.
 */
@LazyInit val _ContentProvider: Type<ContentProvider> = ContentProvider::class.java.type

/**
 * Represents the type of [Service] class.
 */
@LazyInit val _Service: Type<Service> = Service::class.java.type

/**
 * Represents the type of [Window] class.
 */
@LazyInit val _Window: Type<Window> = Window::class.java.type

/**
 * Represents the type of [WindowManager] class.
 */
@LazyInit val _WindowManager: Type<WindowManager> = WindowManager::class.java.type

/**
 * Represents the type of [Parcelable] class.
 */
@LazyInit val _Parcelable: Type<Parcelable> = Parcelable::class.java.type

/**
 * Represents the type of [ContentResolver] class.
 */
@LazyInit val _ContentResolver: Type<ContentResolver> = ContentResolver::class.java.type

/**
 * Represents the type of [BroadcastReceiver] class.
 */
@LazyInit val _BroadcastReceiver: Type<BroadcastReceiver> = BroadcastReceiver::class.java.type

/**
 * Represents the type of [FragmentManager] class.
 */
@LazyInit val _FragmentManager: Type<FragmentManager> = FragmentManager::class.java.type

/**
 * Represents the type of [ActionBar] class.
 */
@LazyInit val _ActionBar: Type<ActionBar> = ActionBar::class.java.type

/**
 * Represents the type of [ContextWrapper] class.
 */
@LazyInit val _ContextWrapper: Type<ContextWrapper> = ContextWrapper::class.java.type

/**
 * Represents the type of [ApplicationInfo] class.
 */
@LazyInit val _ApplicationInfo: Type<ApplicationInfo> = ApplicationInfo::class.java.type

/**
 * Represents the type of [ContentValues] class.
 */
@LazyInit val _ContentValues: Type<ContentValues> = ContentValues::class.java.type

/**
 * Represents the type of [PackageInfo] class.
 */
@LazyInit val _PackageInfo: Type<PackageInfo> = PackageInfo::class.java.type

/**
 * Represents the type of [Bundle] class.
 */
@LazyInit val _Bundle: Type<Bundle> = Bundle::class.java.type

/**
 * Represents the type of [Activity] class.
 */
@LazyInit val _Activity: Type<Activity> = Activity::class.java.type

/**
 * Represents the type of [Fragment] class.
 */
@LazyInit val _Fragment: Type<Fragment> = Fragment::class.java.type

/**
 * Represents the type of [DialogFragment] class.
 */
@LazyInit val _DialogFragment: Type<DialogFragment> = DialogFragment::class.java.type

/**
 * Represents the type of [Intent] class.
 */
@LazyInit val _Intent: Type<Intent> = Intent::class.java.type

/**
 * Represents the type of [AppComponentFactory] class.
 */
@LazyInit val _AppComponentFactory: Type<AppComponentFactory> = AppComponentFactory::class.java.type

/**
 * Represents the type of [AlertDialog] class.
 */
@LazyInit val _AlertDialog: Type<AlertDialog> = AlertDialog::class.java.type

/**
 * Represents the type of [Dialog] class.
 */
@LazyInit val _Dialog: Type<Dialog> = Dialog::class.java.type

/**
 * Represents the type of [DialogInterface] class.
 */
@LazyInit val _DialogInterface: Type<DialogInterface> = DialogInterface::class.java.type

/**
 * Represents the type of [Drawable] class.
 */
@LazyInit val _Drawable: Type<Drawable> = Drawable::class.java.type

/**
 * Represents the type of [Editable] class.
 */
@LazyInit val _Editable: Type<Editable> = Editable::class.java.type

/**
 * Represents the type of [Spannable] class.
 */
@LazyInit val _Spannable: Type<Spannable> = Spannable::class.java.type

/**
 * Represents the type of [AssetManager] class.
 */
@LazyInit val _AssetManager: Type<AssetManager> = AssetManager::class.java.type

/**
 * Represents the type of [ColorStateList] class.
 */
@LazyInit val _ColorStateList: Type<ColorStateList> = ColorStateList::class.java.type

/**
 * Represents the type of [Typeface] class.
 */
@LazyInit val _Typeface: Type<Typeface> = Typeface::class.java.type

/**
 * Represents the type of [Bitmap] class.
 */
@LazyInit val _Bitmap: Type<Bitmap> = Bitmap::class.java.type

/**
 * Represents the type of [Paint] class.
 */
@LazyInit val _Paint: Type<Paint> = Paint::class.java.type

/**
 * Represents the type of [Adapter] class.
 */
@LazyInit val _Adapter: Type<Adapter> = Adapter::class.java.type

/**
 * Represents the type of [BaseAdapter] class.
 */
@LazyInit val _BaseAdapter: Type<BaseAdapter> = BaseAdapter::class.java.type

/**
 * Represents the type of [AbsListView] class.
 */
@LazyInit val _AbsListView: Type<AbsListView> = AbsListView::class.java.type

/**
 * Represents the type of [LayoutInflater] class.
 */
@LazyInit val _LayoutInflater: Type<LayoutInflater> = LayoutInflater::class.java.type

/**
 * Represents the type of [LayoutInflater.Filter] class.
 */
@LazyInit val _LayoutInflater_Filter: Type<LayoutInflater.Filter> =
    LayoutInflater.Filter::class.java.type

/**
 * Represents the type of [LayoutInflater.Factory] class.
 */
@LazyInit val _LayoutInflater_Factory: Type<LayoutInflater.Factory> =
    LayoutInflater.Factory::class.java.type

/**
 * Represents the type of [LayoutInflater.Factory2] class.
 */
@LazyInit val _LayoutInflater_Factory2: Type<LayoutInflater.Factory2> =
    LayoutInflater.Factory2::class.java.type

/**
 * Represents the type of [Menu] class.
 */
@LazyInit val _Menu: Type<Menu> = Menu::class.java.type

/**
 * Represents the type of [MenuItem] class.
 */
@LazyInit val _MenuItem: Type<MenuItem> = MenuItem::class.java.type

/**
 * Represents the type of [MenuInflater] class.
 */
@LazyInit val _MenuInflater: Type<MenuInflater> = MenuInflater::class.java.type

/**
 * Represents the type of [View] class.
 */
@LazyInit val _View: Type<View> = View::class.java.type

/**
 * Represents the type of [Button] class.
 */
@LazyInit val _Button: Type<Button> = Button::class.java.type

/**
 * Represents the type of [TextView] class.
 */
@LazyInit val _TextView: Type<TextView> = TextView::class.java.type

/**
 * Represents the type of [EditText] class.
 */
@LazyInit val _EditText: Type<EditText> = EditText::class.java.type

/**
 * Represents the type of [CheckBox] class.
 */
@LazyInit val _CheckBox: Type<CheckBox> = CheckBox::class.java.type

/**
 * Represents the type of [ImageView] class.
 */
@LazyInit val _ImageView: Type<ImageView> = ImageView::class.java.type

/**
 * Represents the type of [ImageButton] class.
 */
@LazyInit val _ImageButton: Type<ImageButton> = ImageButton::class.java.type

/**
 * Represents the type of [RemoteViews] class.
 */
@LazyInit val _RemoteViews: Type<RemoteViews> = RemoteViews::class.java.type

/**
 * Represents the type of [ListView] class.
 */
@LazyInit val _ListView: Type<ListView> = ListView::class.java.type

/**
 * Represents the type of [PopupWindow] class.
 */
@LazyInit val _PopupWindow: Type<PopupWindow> = PopupWindow::class.java.type

/**
 * Represents the type of [EdgeEffect] class.
 */
@LazyInit val _EdgeEffect: Type<EdgeEffect> = EdgeEffect::class.java.type

/**
 * Represents the type of [ScrollView] class.
 */
@LazyInit val _ScrollView: Type<ScrollView> = ScrollView::class.java.type

/**
 * Represents the type of [Spinner] class.
 */
@LazyInit val _Spinner: Type<Spinner> = Spinner::class.java.type

/**
 * Represents the type of [Toast] class.
 */
@LazyInit val _Toast: Type<Toast> = Toast::class.java.type

/**
 * Represents the type of [Toolbar] class.
 */
@LazyInit val _Toolbar: Type<Toolbar> = Toolbar::class.java.type

/**
 * Represents the type of [KeyEvent] class.
 */
@LazyInit val _KeyEvent: Type<KeyEvent> = KeyEvent::class.java.type

/**
 * Represents the type of [MotionEvent] class.
 */
@LazyInit val _MotionEvent: Type<MotionEvent> = MotionEvent::class.java.type

/**
 * Represents the type of [View.OnClickListener] class.
 */
@LazyInit val _View_OnClickListener: Type<View.OnClickListener> = View.OnClickListener::class.java.type

/**
 * Represents the type of [View.OnLongClickListener] class.
 */
@LazyInit val _View_OnLongClickListener: Type<View.OnLongClickListener> =
    View.OnLongClickListener::class.java.type

/**
 * Represents the type of [View.OnCreateContextMenuListener] class.
 */
@LazyInit val _View_OnCreateContextMenuListener: Type<View.OnCreateContextMenuListener> =
    View.OnCreateContextMenuListener::class.java.type

/**
 * Represents the type of [View.OnContextClickListener] class.
 */
@LazyInit val _View_OnContextClickListener: Type<View.OnContextClickListener> =
    View.OnContextClickListener::class.java.type

/**
 * Represents the type of [View.OnFocusChangeListener] class.
 */
@LazyInit val _View_OnFocusChangeListener: Type<View.OnFocusChangeListener> =
    View.OnFocusChangeListener::class.java.type

/**
 * Represents the type of [View.OnDragListener] class.
 */
@LazyInit val _View_OnDragListener: Type<View.OnDragListener> = View.OnDragListener::class.java.type

/**
 * Represents the type of [View.OnGenericMotionListener] class.
 */
@LazyInit val _View_OnGenericMotionListener: Type<View.OnGenericMotionListener> =
    View.OnGenericMotionListener::class.java.type

/**
 * Represents the type of [View.OnHoverListener] class.
 */
@LazyInit val _View_OnHoverListener: Type<View.OnHoverListener> = View.OnHoverListener::class.java.type

/**
 * Represents the type of [View.OnTouchListener] class.
 */
@LazyInit val _View_OnTouchListener: Type<View.OnTouchListener> = View.OnTouchListener::class.java.type

/**
 * Represents the type of [View.OnKeyListener] class.
 */
@LazyInit val _View_OnKeyListener: Type<View.OnKeyListener> = View.OnKeyListener::class.java.type

/**
 * Represents the type of [View.OnScrollChangeListener] class.
 */
@LazyInit val _View_OnScrollChangeListener: Type<View.OnScrollChangeListener> =
    View.OnScrollChangeListener::class.java.type

/**
 * Represents the type of [View.OnLayoutChangeListener] class.
 */
@LazyInit val _View_OnLayoutChangeListener: Type<View.OnLayoutChangeListener> =
    View.OnLayoutChangeListener::class.java.type

/**
 * Represents the type of [ViewGroup] class.
 */
@LazyInit val _ViewGroup: Type<ViewGroup> = ViewGroup::class.java.type

/**
 * Represents the type of [ViewGroup.MarginLayoutParams] class.
 */
@LazyInit val _ViewGroup_MarginLayoutParams: Type<ViewGroup.MarginLayoutParams> =
    ViewGroup.MarginLayoutParams::class.java.type

/**
 * Represents the type of [ViewGroup.LayoutParams] class.
 */
@LazyInit val _ViewGroup_LayoutParams: Type<ViewGroup.LayoutParams> =
    ViewGroup.LayoutParams::class.java.type

/**
 * Represents the type of [RelativeLayout] class.
 */
@LazyInit val _RelativeLayout: Type<RelativeLayout> = RelativeLayout::class.java.type

/**
 * Represents the type of [RelativeLayout.LayoutParams] class.
 */
@LazyInit val _RelativeLayout_LayoutParams: Type<RelativeLayout.LayoutParams> =
    RelativeLayout.LayoutParams::class.java.type

/**
 * Represents the type of [LinearLayout] class.
 */
@LazyInit val _LinearLayout: Type<LinearLayout> = LinearLayout::class.java.type

/**
 * Represents the type of [LinearLayout.LayoutParams] class.
 */
@LazyInit val _LinearLayout_LayoutParams: Type<LinearLayout.LayoutParams> =
    LinearLayout.LayoutParams::class.java.type

/**
 * Represents the type of [FrameLayout] class.
 */
@LazyInit val _FrameLayout: Type<FrameLayout> = FrameLayout::class.java.type

/**
 * Represents the type of [FrameLayout.LayoutParams] class.
 */
@LazyInit val _FrameLayout_LayoutParams: Type<FrameLayout.LayoutParams> =
    FrameLayout.LayoutParams::class.java.type

/**
 * Represents the type of [AttributeSet] class.
 */
@LazyInit val _AttributeSet: Type<AttributeSet> = AttributeSet::class.java.type
