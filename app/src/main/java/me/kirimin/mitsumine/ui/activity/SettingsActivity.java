package me.kirimin.mitsumine.ui.activity;

import java.util.List;

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.db.AccountDAO;
import me.kirimin.mitsumine.db.NGWordDAO;
import me.kirimin.mitsumine.model.Account;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(R.id.tool_bar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.settings_title);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        getFragmentManager().beginTransaction()
                .replace(R.id.content, new MyPrefsFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class MyPrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.app_preferences);
            findPreference("about").setOnPreferenceClickListener(new OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), AboutActivity.class));
                    return false;
                }
            });

            findPreference("ngword").setOnPreferenceClickListener(new OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    createEditNGWordDialog().show();
                    return false;
                }
            });

            Account account = AccountDAO.get();
            Preference preference = findPreference("logout");
            preference.setEnabled(account != null);
            preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AccountDAO.delete();
                    Toast.makeText(getActivity(), getString(R.string.settings_logout_toast), Toast.LENGTH_SHORT).show();
                    preference.setEnabled(false);
                    return false;
                }
            });
        }

        private AlertDialog createEditNGWordDialog() {
            final List<String> ngWordList = NGWordDAO.findAll();
            ngWordList.add(getString(R.string.settings_ngword_add));
            OnClickListener onClick = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == ngWordList.size() - 1) {
                        createAddDialog().show();
                    } else {
                        createDeleteDialog(which).show();
                    }
                }
            };
            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.settings_ngword)
                    .setItems(ngWordList.toArray(new String[ngWordList.size()]), onClick)
                    .create();
        }

        private AlertDialog createAddDialog() {
            final EditText editText = new EditText(getActivity());
            OnClickListener onPositiveListener = new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    NGWordDAO.save(editText.getText().toString());
                }
            };

            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.settings_ngword_add)
                    .setPositiveButton(android.R.string.ok, onPositiveListener)
                    .setView(editText)
                    .create();
        }

        private AlertDialog createDeleteDialog(final int index) {
            OnClickListener onPositiveListener = new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    NGWordDAO.delete(NGWordDAO.findAll().get(index));
                }
            };

            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.settings_ngword_delete)
                    .setPositiveButton(android.R.string.ok, onPositiveListener)
                    .setNegativeButton(android.R.string.cancel, null)
                    .create();
        }
    }

}
