package smy20011.s1venus.util

import android.app.Activity
import android.app.Fragment
import android.view.View
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
class ViewFinder<T>(val viewFinder : () -> View): ReadOnlyProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return viewFinder() as T
    }
}

infix fun <T> Activity.viewId(resId: Int) = ViewFinder<T> {
    this.findViewById(resId)
}
infix fun <T> Fragment.viewId(resId: Int) = ViewFinder<T> {
    this.view.findViewById(resId)
}