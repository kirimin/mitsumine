package me.kirimin.mitsumine.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceFragment
import android.widget.EditText
import android.widget.Toast
import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.db.AccountDAO
import me.kirimin.mitsumine.db.NGWordDAO
import me.kirimin.mitsumine.ui.activity.AboutActivity

public class SettingFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.app_preferences)
        findPreference("about").setOnPreferenceClickListener {
            startActivity(Intent(activity, AboutActivity::class.java))
            false
        }

        findPreference("ngword").setOnPreferenceClickListener {
            createEditNGWordDialog().show()
            false
        }

        val preference = findPreference("logout")
        preference.isEnabled = AccountDAO.get() != null
        preference.setOnPreferenceClickListener { preference ->
            AccountDAO.delete()
            Toast.makeText(activity, getString(R.string.settings_logout_toast), Toast.LENGTH_SHORT).show()
            preference.isEnabled = false
            false
        }
    }

    private fun createEditNGWordDialog(): AlertDialog {
        val ngWordList = NGWordDAO.findAll().plus(getString(R.string.settings_ngword_add))
        return AlertDialog.Builder(activity)
                .setTitle(R.string.settings_ngword)
                .setItems(ngWordList.toTypedArray(), { dialog, which ->
                    if (which == ngWordList.size() - 1) {
                        createAddDialog().show()
                    } else {
                        createDeleteDialog(which).show()
                    }
                })
                .create()
    }

    private fun createAddDialog(): AlertDialog {
        val editText = EditText(activity)
        return AlertDialog.Builder(activity)
                .setTitle(R.string.settings_ngword_add)
                .setPositiveButton(android.R.string.ok, { dialog, which ->
                    if (editText.text.length() != 0) NGWordDAO.save(editText.text.toString())
                })
                .setView(editText)
                .create()
    }

    private fun createDeleteDialog(index: Int): AlertDialog {
        return AlertDialog.Builder(activity)
                .setTitle(R.string.settings_ngword_delete)
                .setPositiveButton(android.R.string.ok, { dialog, which ->
                    NGWordDAO.delete(NGWordDAO.findAll().get(index))
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create()
    }
}