/*

 This is the source code of exteraGram for Android.

 We do not and cannot prevent the use of our code,
 but be respectful and credit the original author.

 Copyright @immat0x1, 2023

*/

package com.exteragram.messenger.preferences;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exteragram.messenger.preferences.components.HeaderSettingsCell;
import com.exteragram.messenger.preferences.updater.UpdaterBottomSheet;
import com.radolyn.ayugram.preferences.AyuGramPreferencesActivity;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class MainPreferencesActivity extends BasePreferencesActivity {

    private View actionBarBackground;
    private AnimatorSet actionBarAnimator;

    private HeaderSettingsCell headerSettingsCell;

    private int categoryHeaderRow;
    private int generalRow;
    private int appearanceRow;
    private int chatsRow;
    private int otherRow;
    private int ayuRow;

    private int categoryDividerRow;
    private int aboutExteraDividerRow;

    private int infoHeaderRow;
    private int aboutExteraRow;
    private int sourceCodeRow;
    private int channelRow;
    private int groupRow;
    private int crowdinRow;
    private int infoDividerRow;

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setBackground(null);
        actionBar.setTitleColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        actionBar.setItemsColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText), false);
        actionBar.setItemsBackgroundColor(Theme.getColor(Theme.key_listSelector), false);
        actionBar.setCastShadows(false);
        actionBar.setAddToContainer(false);
        actionBar.setOccupyStatusBar(!AndroidUtilities.isTablet());
        actionBar.setTitle(getTitle());
        actionBar.getTitleTextView().setAlpha(0.0f);
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        fragmentView = new FrameLayout(context) {
            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) actionBarBackground.getLayoutParams();
                layoutParams.height = ActionBar.getCurrentActionBarHeight() + (actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) + AndroidUtilities.dp(3);

                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }

            @Override
            protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
                super.onLayout(changed, left, top, right, bottom);
                checkScroll(false);
            }
        };
        fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        fragmentView.setTag(Theme.key_windowBackgroundGray);
        FrameLayout frameLayout = (FrameLayout) fragmentView;

        listView = new RecyclerListView(context);
        listView.setLayoutManager(layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listView.setAdapter(listAdapter = createAdapter(context));
        frameLayout.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        listView.setOnItemClickListener(this::onItemClick);
        listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                checkScroll(true);
            }
        });

        actionBarBackground = new View(context) {

            private final Paint paint = new Paint();

            @Override
            protected void onDraw(Canvas canvas) {
                paint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                int h = getMeasuredHeight() - AndroidUtilities.dp(3);
                canvas.drawRect(0, 0, getMeasuredWidth(), h, paint);
                parentLayout.drawHeaderShadow(canvas, h);
            }
        };
        actionBarBackground.setAlpha(0.0f);
        frameLayout.addView(actionBarBackground, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));
        frameLayout.addView(actionBar, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        updateRowsId();
        return fragmentView;
    }

    private final int[] location = new int[2];

    private void checkScroll(boolean animated) {
        int first = layoutManager.findFirstVisibleItemPosition();
        boolean show;
        if (first != 0) {
            show = true;
        } else {
            RecyclerView.ViewHolder holder = listView.findViewHolderForAdapterPosition(first);
            if (holder == null) {
                show = true;
            } else {
                headerSettingsCell = (HeaderSettingsCell) holder.itemView;
                headerSettingsCell.titleTextView.getLocationOnScreen(location);
                show = location[1] + headerSettingsCell.titleTextView.getMeasuredHeight() < actionBar.getBottom();
            }
        }
        boolean visible = actionBarBackground.getTag() == null;
        if (show != visible) {
            actionBarBackground.setTag(show ? null : 1);
            if (actionBarAnimator != null) {
                actionBarAnimator.cancel();
                actionBarAnimator = null;
            }
            if (animated) {
                actionBarAnimator = new AnimatorSet();
                actionBarAnimator.playTogether(
                        ObjectAnimator.ofFloat(actionBarBackground, View.ALPHA, show ? 1.0f : 0.0f),
                        ObjectAnimator.ofFloat(actionBar.getTitleTextView(), View.ALPHA, show ? 1.0f : 0.0f)
                );
                actionBarAnimator.setDuration(250);
                actionBarAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (animation.equals(actionBarAnimator)) {
                            actionBarAnimator = null;
                        }
                    }
                });
                actionBarAnimator.start();
            } else {
                actionBarBackground.setAlpha(show ? 1.0f : 0.0f);
                actionBar.getTitleTextView().setAlpha(show ? 1.0f : 0.0f);
            }
        }
    }

    @Override
    protected void updateRowsId() {
        super.updateRowsId();

        aboutExteraRow = newRow();
        aboutExteraDividerRow = newRow();

        categoryHeaderRow = newRow();
        generalRow = newRow();
        appearanceRow = newRow();
        chatsRow = newRow();
        otherRow = newRow();
        ayuRow = newRow();
        categoryDividerRow = newRow();

        infoHeaderRow = newRow();
        channelRow = newRow();
        groupRow = newRow();
        crowdinRow = newRow();
        sourceCodeRow = newRow();
        infoDividerRow = newRow();
    }

    @Override
    protected void onItemClick(View view, int position, float x, float y) {
        if (position == aboutExteraRow) {
            if (!BuildVars.PM_BUILD)
                (new UpdaterBottomSheet(getParentActivity(), this, false)).show();
        } else if (position == sourceCodeRow) {
            Browser.openUrl(getParentActivity(), "https://github.com/" + BuildVars.APP_GITHUB);
        } else if (position == channelRow) {
            MessagesController.getInstance(currentAccount).openByUserName(("ayugram1338"), this, 1);
        } else if (position == groupRow) {
            MessagesController.getInstance(currentAccount).openByUserName(("ayugramchat"), this, 1);
        } else if (position == crowdinRow) {
            Browser.openUrl(getParentActivity(), "https://crowdin.com/project/exteralocales");
        } else if (position == appearanceRow) {
            presentFragment(new AppearancePreferencesActivity());
        } else if (position == chatsRow) {
            presentFragment(new ChatsPreferencesActivity());
        } else if (position == otherRow) {
            presentFragment(new OtherPreferencesActivity());
        } else if (position == ayuRow) {
            presentFragment(new AyuGramPreferencesActivity());
        } else if (position == generalRow) {
            presentFragment(new GeneralPreferencesActivity());
        }
    }

    @Override
    protected String getTitle() {
        return LocaleController.getString("Preferences", R.string.Preferences);
    }

    @Override
    protected boolean hasWhiteActionBar() {
        return true;
    }

    @Override
    protected BaseListAdapter createAdapter(Context context) {
        return new ListAdapter(context);
    }

    private class ListAdapter extends BaseListAdapter {

        public ListAdapter(Context context) {
            super(context);
        }

        @Override
        public int getItemCount() {
            return rowCount;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case 1:
                    holder.itemView.setBackground(Theme.getThemedDrawable(mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    break;
                case 2:
                    TextCell textCell = (TextCell) holder.itemView;
                    if (position == generalRow) {
                        textCell.setTextAndIcon(LocaleController.getString("General", R.string.General), R.drawable.msg_media, true);
                    } else if (position == appearanceRow) {
                        textCell.setTextAndIcon(LocaleController.getString("Appearance", R.string.Appearance), R.drawable.msg_theme, true);
                    } else if (position == chatsRow) {
                        textCell.setTextAndIcon(LocaleController.getString("SearchAllChatsShort", R.string.SearchAllChatsShort), R.drawable.msg_discussion, true);
                    } else if (position == otherRow) {
                        textCell.setTextAndIcon(LocaleController.getString("LocalOther", R.string.LocalOther), R.drawable.msg_fave, false);
                    } else if (position == ayuRow) {
                        textCell.setTextAndIcon(LocaleController.getString("AyuPreferences", R.string.AyuPreferences), R.drawable.msg_premium_badge, false);
                    } else if (position == channelRow) {
                        textCell.setTextAndValueAndIcon(LocaleController.getString("Channel", R.string.Channel), "@ayugram1338", R.drawable.msg_channel, true);
                    } else if (position == groupRow) {
                        textCell.setTextAndValueAndIcon(LocaleController.getString("SearchAllChatsShort", R.string.SearchAllChatsShort), "@ayugramchat", R.drawable.msg_groups, true);
                    } else if (position == crowdinRow) {
                        textCell.setTextAndValueAndIcon(LocaleController.getString("Crowdin", R.string.Crowdin), "Crowdin", R.drawable.msg_translate, true);
                    } else if (position == sourceCodeRow) {
                        textCell.setTextAndValueAndIcon(LocaleController.getString("SourceCode", R.string.SourceCode), "GitHub", R.drawable.msg_delete, false);
                    }
                    break;
                case 3:
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == categoryHeaderRow) {
                        headerCell.setText(LocaleController.getString("Categories", R.string.Categories));
                    } else if (position == infoHeaderRow) {
                        headerCell.setText(LocaleController.getString("Links", R.string.Links));
                    }
                    break;
                case 4:
                    headerSettingsCell = (HeaderSettingsCell) holder.itemView;
                    headerSettingsCell.setPadding(0, ActionBar.getCurrentActionBarHeight() + (actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) - AndroidUtilities.dp(40), 0, 0);
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == categoryDividerRow || position == aboutExteraDividerRow || position == infoDividerRow) {
                return 1;
            } else if (position == infoHeaderRow || position == categoryHeaderRow) {
                return 3;
            } else if (position == aboutExteraRow) {
                return 4;
            }
            return 2;
        }
    }
}