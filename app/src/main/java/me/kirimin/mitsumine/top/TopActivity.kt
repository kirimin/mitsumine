package me.kirimin.mitsumine.top

import me.kirimin.mitsumine.R

import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import android.widget.ArrayAdapter
import android.widget.TextView

import com.makeramen.RoundedTransformationBuilder
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_top.*
import me.kirimin.mitsumine.top.TopData
import me.kirimin.mitsumine.top.TopUseCase
import me.kirimin.mitsumine.common.domain.enums.Category
import me.kirimin.mitsumine.common.domain.enums.Type
import me.kirimin.mitsumine.top.TopPresenter
import me.kirimin.mitsumine.top.TopView
import me.kirimin.mitsumine.feed.readlater.ReadLaterActivity
import me.kirimin.mitsumine.feed.FeedFragment
import java.io.Serializable

class TopActivity : AppCompatActivity(), TopView {

    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    private var presenter: TopPresenter = TopPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top)
        if (savedInstanceState != null) {
            val selectedCategory = savedInstanceState.getSerializable(Category::class.java.canonicalName) as Category
            val selectedType = savedInstanceState.getSerializable(Type::class.java.canonicalName) as Type
            presenter.onCreate(this, TopUseCase(TopData()), selectedCategory, selectedType)
        } else {
            presenter.onCreate(this, TopUseCase(TopData()))
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(Category::class.java.canonicalName, presenter.getCurrentCategory() as Serializable)
        outState.putSerializable(Type::class.java.canonicalName, presenter.getCurrentType() as Serializable)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val unRead = menu.add(Menu.NONE, 1, Menu.NONE, R.string.type_read_later)
        unRead.setIcon(R.drawable.ic_action_labels)
        MenuItemCompat.setShowAsAction(unRead, MenuItemCompat.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }

        if (item.title == getString(R.string.type_read_later)) {
            startActivity(Intent(this, ReadLaterActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mDrawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mDrawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onBackPressed() {
        presenter.onBackKeyClick()
    }

    override fun backPress() {
        super.onBackPressed()
    }

    override fun initViews() {
        setSupportActionBar(toolBar)
        val actionBar = supportActionBar ?: return
        actionBar.navigationMode = ActionBar.NAVIGATION_MODE_LIST
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)

        val data = arrayOf(getString(R.string.feed_hot), getString(R.string.feed_new))
        val adapter = ArrayAdapter(actionBar.themedContext, android.R.layout.simple_list_item_1, data)
        actionBar.setListNavigationCallbacks(adapter, { position, id -> presenter.onNavigationClick(position) })

        mDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name)
        drawerLayout.setDrawerListener(mDrawerToggle)
        toolBar.setOnClickListener { presenter.onToolbarClick() }

        val onclickListener = OnClickListener { v -> presenter.onViewClick(v.id) }
        navigationReadTextView.setOnClickListener(onclickListener)
        navigationSettingsTextView.setOnClickListener(onclickListener)
        navigationKeywordSearchTextView.setOnClickListener(onclickListener)
        navigationUserSearchTextView.setOnClickListener(onclickListener)
        navigationUserInfoLayout.setOnClickListener(onclickListener)
        navigationLoginButton.setOnClickListener(onclickListener)
    }

    override fun addNavigationCategoryButton(category: Category) {
        val button = makeNavigationButton(getString(category.labelResource), OnClickListener { v -> presenter.onCategoryClick(category) }, null)
        navigationCategories.addView(button)
    }

    private fun makeNavigationButton(label: String, onClick: OnClickListener, onLongClick: OnLongClickListener?): View {
        val navigationView = LayoutInflater.from(applicationContext).inflate(R.layout.activity_top_navigation, null)
        val textView = navigationView.findViewById(R.id.MainNavigationTextView) as TextView
        textView.text = label
        textView.setOnClickListener(onClick)
        textView.setOnLongClickListener(onLongClick)
        return navigationView
    }

    override fun refreshShowCategoryAndType(category: Category, type: Type, typeInt: Int) {
        supportActionBar?.setTitle(category.labelResource)
        supportActionBar?.setSelectedNavigationItem(typeInt)
        supportFragmentManager.beginTransaction()
                .replace(R.id.containerFrameLayout, FeedFragment.newFragment(category, type))
                .commit()
        for (i in 0..navigationCategories.childCount - 1) {
            val categoryButton = navigationCategories.getChildAt(i).findViewById(R.id.MainNavigationTextView) as TextView
            val isSelectedCategory = categoryButton.text == getString(category.labelResource)
            categoryButton.setTextColor(ContextCompat.getColor(this, if (isSelectedCategory) R.color.orange else R.color.text))
        }
    }

    override fun closeNavigation() {
        drawerLayout.closeDrawers()
    }

    override fun openNavigation() {
        drawerLayout.openDrawer(Gravity.LEFT)
    }

    override fun isOpenNavigation(): Boolean {
        return drawerLayout.isDrawerOpen(Gravity.LEFT)
    }

    override fun startActivity(activityClass: Class<*>) {
        startActivity(Intent(this, activityClass));
    }

    override fun startActivity(activityClass: Class<*>, bundle: Bundle) {
        val intent = Intent(this, activityClass)
        startActivity(intent.putExtras(bundle))
    }

    override fun enableUserInfo(userName: String, iconUrl: String) {
        navigationUserInfoLayout.visibility = View.VISIBLE
        navigationLoginButton.visibility = View.GONE
        navigationUserName.text = userName
        val transformation = RoundedTransformationBuilder().borderWidthDp(0f).cornerRadiusDp(48f).oval(false).build()
        Picasso.with(this).load(iconUrl).transform(transformation).fit().into(navigationUserIconImageView)
    }

    override fun disableUserInfo() {
        navigationUserInfoLayout.visibility = View.GONE
        navigationLoginButton.visibility = View.VISIBLE
    }

    override fun removeNavigationAdditions() {
        navigationAdditions.removeAllViews()

    }

    override fun addAdditionKeyword(keyword: String) {
        navigationAdditions.addView(makeNavigationButton(keyword,
                OnClickListener { presenter.onAdditionKeywordClick(keyword) },
                OnLongClickListener { v -> presenter.onAdditionKeywordLongClick(keyword, v) }))
    }

    override fun addAdditionUser(userId: String) {
        navigationAdditions.addView(makeNavigationButton(userId,
                OnClickListener { presenter.onAdditionUserClick(userId) },
                OnLongClickListener { v -> presenter.onAdditionUserLongClick(userId, v) }))
    }

    override fun showDeleteUserDialog(userId: String, view: View) {
        AlertDialog.Builder(this)
                .setTitle(R.string.settings_ngword_delete)
                .setPositiveButton(android.R.string.ok, { dialog, id -> presenter.onDeleteUserIdDialogClick(userId, view) })
                .setNegativeButton(android.R.string.cancel, null).create().show()
    }

    override fun showDeleteKeywordDialog(keyword: String, view: View) {
        AlertDialog.Builder(this)
                .setTitle(R.string.settings_ngword_delete)
                .setPositiveButton(android.R.string.ok, { dialog, id -> presenter.onDeleteKeywordDialogClick(keyword, view) })
                .setNegativeButton(android.R.string.cancel, null)
                .create().show()
    }
}
