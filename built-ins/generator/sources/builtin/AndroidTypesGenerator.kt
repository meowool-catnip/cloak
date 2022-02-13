@file:Suppress("DEPRECATION")

package builtin

import android.app.Activity
import android.app.*
import android.app.AppComponentFactory
import android.app.Fragment
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
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.app.CoreComponentFactory
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import org.junit.jupiter.api.Test
import androidx.appcompat.app.ActionBar as ActionBarX
import androidx.appcompat.app.AlertDialog as AlertDialogX
import androidx.core.app.ComponentActivity as CoreAppComponentActivity
import androidx.fragment.app.DialogFragment as DialogFragmentX
import androidx.fragment.app.Fragment as FragmentX

/**
 * @author 凛 (RinOrz)
 */
class AndroidTypesGenerator : TypesGenerator(Platform.Android) {
  @Test fun generateSystemTypes(): Unit = generateTypes(
    "AndroidSystem",

    Context::class,
    ContentProvider::class,
    Service::class,
    Window::class,
    WindowManager::class,
    Parcelable::class,
    ContentResolver::class,
    BroadcastReceiver::class,
    FragmentManager::class,
    ActionBar::class,
    ContextWrapper::class,
    ApplicationInfo::class,
    ContentValues::class,
    PackageInfo::class,
    Bundle::class,
    Activity::class,
    Fragment::class,
    DialogFragment::class,
    Intent::class,
    AppComponentFactory::class,

    AlertDialog::class,
    Dialog::class,
    DialogInterface::class,

    Drawable::class,
    Editable::class,
    Spannable::class,
    AssetManager::class,
    ColorStateList::class,
    Typeface::class,
    Bitmap::class,
    Paint::class,

    Adapter::class,
    BaseAdapter::class,
    AbsListView::class,
    LayoutInflater::class,
    LayoutInflater.Filter::class,
    LayoutInflater.Factory::class,
    LayoutInflater.Factory2::class,
    Menu::class,
    MenuItem::class,
    MenuInflater::class,
    View::class,
    Button::class,
    TextView::class,
    EditText::class,
    CheckBox::class,
    ImageView::class,
    ImageButton::class,
    RemoteViews::class,
    ListView::class,
    PopupWindow::class,
    EdgeEffect::class,
    ScrollView::class,
    Spinner::class,
    Toast::class,
    Toolbar::class,
    KeyEvent::class,
    MotionEvent::class,
    View.OnClickListener::class,
    View.OnLongClickListener::class,
    View.OnCreateContextMenuListener::class,
    View.OnContextClickListener::class,
    View.OnFocusChangeListener::class,
    View.OnDragListener::class,
    View.OnGenericMotionListener::class,
    View.OnHoverListener::class,
    View.OnTouchListener::class,
    View.OnKeyListener::class,
    View.OnScrollChangeListener::class,
    View.OnLayoutChangeListener::class,

    ViewGroup::class,
    ViewGroup.MarginLayoutParams::class,
    ViewGroup.LayoutParams::class,
    RelativeLayout::class,
    RelativeLayout.LayoutParams::class,
    LinearLayout::class,
    LinearLayout.LayoutParams::class,
    FrameLayout::class,
    FrameLayout.LayoutParams::class,

    AttributeSet::class,
  )
  @Test fun generateSystemHiddenTypes(): Unit = generateTypes(
    "AndroidSystemHidden",

  )

  @Test fun generateAndroidXTypes(): Unit = generateTypes(
    "AndroidX",
    AppCompatActivity::class,
    ComponentActivity::class,
    CoreAppComponentActivity::class to "CoreAppComponentActivity",
    CoreComponentFactory::class,
    ActivityResult::class,
    AppCompatDialog::class,
    AppCompatDelegate::class,
    AppCompatDialogFragment::class,
    FragmentX::class to "FragmentX",
    FragmentActivity::class to "FragmentActivityX",
    DialogFragmentX::class to "DialogFragmentX",
    AlertDialogX::class to "AlertDialogX",
    ActionBarX::class to "ActionBarX",
  )
}