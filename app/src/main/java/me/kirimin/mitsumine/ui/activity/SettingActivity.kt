package me.kirimin.mitsumine.ui.activity

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.db.AccountDAO
import me.kirimin.mitsumine.db.NGWordDAO
import me.kirimin.mitsumine.model.Account

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.support.v7.app.ActionBarActivity
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast

import kotlinx.android.synthetic.activity_settings.*

public class SettingsActivity : ActionBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolBar)
        val actionBar = getSupportActionBar()
        actionBar.setTitle(R.string.settings_title)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)

        getFragmentManager().beginTransaction().replace(R.id.content, MyPrefsFragment()).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.getItemId()) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    public class MyPrefsFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.app_preferences)
            findPreference("about").setOnPreferenceClickListener {
                startActivity(Intent(getActivity(), javaClass<AboutActivity>()))
                false
            }

            findPreference("ngword").setOnPreferenceClickListener {
                createEditNGWordDialog().show()
                false
            }

            val preference = findPreference("logout")
            preference.setEnabled(AccountDAO.get() != null)
            preference.setOnPreferenceClickListener { preference ->
                AccountDAO.delete()
                Toast.makeText(getActivity(), getString(R.string.settings_logout_toast), Toast.LENGTH_SHORT).show()
                preference.setEnabled(false)
                false
            }
        }

        private fun createEditNGWordDialog(): AlertDialog {
            val ngWordList = NGWordDAO.findAll()
            return AlertDialog.Builder(getActivity())
                    .setTitle(R.string.settings_ngword)
                    .setItems(ngWordList.copyToArray(), { dialog, which ->
                        if (which == ngWordList.size() - 1) {
                            createAddDialog().show()
                        } else {
                            createDeleteDialog(which).show()
                        }
                    })
                    .create()
        }

        private fun createAddDialog(): AlertDialog {
            val editText = EditText(getActivity())
            return AlertDialog.Builder(getActivity())
                    .setTitle(R.string.settings_ngword_add)
                    .setPositiveButton(android.R.string.ok, { dialog, which ->
                        if (editText.getText().length() != 0) NGWordDAO.save(editText.getText().toString())
                    })
                    .setView(editText)
                    .create()
        }

        private fun createDeleteDialog(index: Int): AlertDialog {
            return AlertDialog.Builder(getActivity())
                    .setTitle(R.string.settings_ngword_delete)
                    .setPositiveButton(android.R.string.ok, { dialog, which ->
                        NGWordDAO.delete(NGWordDAO.findAll().get(index))
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .create()
        }
    }
}