package oftenutilbox.viam.psw.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


val Context.pref: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(this)

// 이런 형식으로 추가한다.
private const val PREF_TEST = "PREF_TEST"
var SharedPreferences.preftest: Boolean
    get() = getBoolean(PREF_TEST, true)
    set(value) = edit { putBoolean(PREF_TEST, value) }


inline fun SharedPreferences.edit(
        commit: Boolean = false,
        action: SharedPreferences.Editor.() -> Unit
) {
    val editor = edit()
    action(editor)
    if (commit) {
        editor.commit()
    } else {
        editor.apply()
    }
}