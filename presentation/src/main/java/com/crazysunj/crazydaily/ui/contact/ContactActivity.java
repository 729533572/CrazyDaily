/**
 * Copyright 2017 Sun Jian
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.crazysunj.crazydaily.ui.contact;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.crazysunj.crazydaily.R;
import com.crazysunj.crazydaily.base.BaseActivity;
import com.crazysunj.crazydaily.extension.ItemTouchHelperExtension;
import com.crazysunj.crazydaily.presenter.ContactPresenter;
import com.crazysunj.crazydaily.presenter.contract.ContactContract;
import com.crazysunj.crazydaily.ui.adapter.ContactsAdapter;
import com.crazysunj.crazydaily.util.SnackbarUtil;
import com.crazysunj.domain.entity.contact.MultiTypeIndexEntity;
import com.gjiazhe.wavesidebar.WaveSideBar;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

import butterknife.BindView;

/**
 * author: sunjian
 * created on: 2018/4/16 下午6:26
 * description:https://github.com/crazysunj/CrazyDaily
 */
public class ContactActivity extends BaseActivity<ContactPresenter> implements ContactContract.View {

    @BindView(R.id.rv_contacts)
    RecyclerView mContacts;
    @BindView(R.id.side_bar)
    WaveSideBar mSideBar;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;

    private ItemTouchHelperExtension mItemTouchHelper;
    private List<MultiTypeIndexEntity> mContactList;
    private ContactsAdapter mAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, ContactActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentResId() {
        return R.layout.activity_contact;
    }

    @Override
    protected void initInject() {
        getActivityComponent()
                .inject(this);
    }

    private static final String[] LETTER = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);
        mContacts.setLayoutManager(new LinearLayoutManager(this));
        mItemTouchHelper = new ItemTouchHelperExtension(new ContactItemTouchHelperCallback());
        mItemTouchHelper.attachToRecyclerView(mContacts);
        mAdapter = new ContactsAdapter(mItemTouchHelper);
        mContacts.setAdapter(mAdapter);

        mSideBar.setIndexItems(LETTER);

        mSearchView.setHint(getString(R.string.search_contact));
        mSearchView.setVoiceSearch(false);
        mSearchView.setCursorDrawable(R.drawable.custom_cursor);
        mSearchView.setEllipsize(true);
        mSearchView.dismissSuggestions();
    }

    @Override
    protected void initListener() {
        mSideBar.setOnSelectIndexItemListener(this::handleSideBarSelect);
        mSearchView.setOnQueryTextListener(new SearchListener());
    }

    @Override
    protected void initData() {
        mPresenter.getConactList();
    }

    @Override
    public void showContent(List<MultiTypeIndexEntity> contacts) {
        mContactList = contacts;
        mAdapter.notifyContacts(contacts);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
            return;
        }
        if (mItemTouchHelper.isOpened()) {
            mItemTouchHelper.closeOpened();
            return;
        }
        super.onBackPressed();
    }

    private void handleSideBarSelect(String index) {
        for (int i = 0, size = mContactList.size(); i < size; i++) {
            if (mContactList.get(i).getIndex().equals(index)) {
                ((LinearLayoutManager) mContacts.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                return;
            }
        }
    }

    class SearchListener implements MaterialSearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            if (TextUtils.isEmpty(query)) {
                SnackbarUtil.show(ContactActivity.this, "请输入搜索内容");
                return false;
            }
            for (int i = 0, size = mContactList.size(); i < size; i++) {
                MultiTypeIndexEntity entity = mContactList.get(i);
                if (entity.getStringId().contains(query) || query.contains(entity.getStringId())) {
                    ((LinearLayoutManager) mContacts.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                    return false;
                }
            }
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if (TextUtils.isEmpty(newText)) {
                return false;
            }
            for (int i = 0, size = mContactList.size(); i < size; i++) {
                MultiTypeIndexEntity entity = mContactList.get(i);
                if (entity.getStringId().contains(newText) || newText.contains(entity.getStringId())) {
                    ((LinearLayoutManager) mContacts.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                    return false;
                }
            }
            return false;
        }
    }

    static class ContactItemTouchHelperCallback extends ItemTouchHelperExtension.Callback {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            final int swipeFlags = viewHolder.getItemViewType() == 0 ? ItemTouchHelper.START : 0;
            return makeMovementFlags(0, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                final ContactsAdapter.ContactViewHolder holder = (ContactsAdapter.ContactViewHolder) viewHolder;
                final float actionWidth = holder.getActionWidth();
                final float translationX = dX < -actionWidth ? -actionWidth : dX;
                final float menuTranslationX = translationX / 2 + actionWidth / 2;
                holder.getView(R.id.content).setTranslationX(translationX);
                holder.getView(R.id.menu).setTranslationX(menuTranslationX);
            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }
    }
}
