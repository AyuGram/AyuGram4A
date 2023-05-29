/*

 This is the source code of exteraGram for Android.

 We do not and cannot prevent the use of our code,
 but be respectful and credit the original author.

 Copyright @immat0x1, 2023

*/

package com.radolyn.ayugram.preferences;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exteragram.messenger.ExteraConfig;
import com.exteragram.messenger.preferences.BasePreferencesActivity;

import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Cells.EditTextSettingsCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;

public class AyuGramPreferencesActivity extends BasePreferencesActivity {
    private int ghostEssentialsHeaderRow;
    private int sendReadPacketsRow;
    private int sendOnlinePacketsRow;
    private int sendUploadProgressRow;
    private int sendOfflinePacketAfterOnlineRow;
    private int markReadAfterSendRow;
    private int useScheduledMessagesRow;

    private int spyHeaderRow;
    private int keepDeletedMessagesRow;
    private int keepMessagesHistoryRow;

    private int qolHeaderRow;
    private int realForwardTime;
    private int showFromChannel;
    private int keepAliveService;
    private int walModeRow;

    private int customizationHeaderRow;
    private int deletedMarkText;

    @Override
    protected void updateRowsId() {
        super.updateRowsId();

        ghostEssentialsHeaderRow = newRow();
        sendReadPacketsRow = newRow();
        sendOnlinePacketsRow = newRow();
        sendUploadProgressRow = newRow();
        sendOfflinePacketAfterOnlineRow = newRow();
        markReadAfterSendRow = newRow();
        useScheduledMessagesRow = newRow();

        spyHeaderRow = newRow();
        keepDeletedMessagesRow = newRow();
        keepMessagesHistoryRow = newRow();

        qolHeaderRow = newRow();
        realForwardTime = newRow();
        showFromChannel = newRow();
        keepAliveService = newRow();
        walModeRow = newRow();

        customizationHeaderRow = newRow();
        deletedMarkText = newRow();
    }

    @Override
    protected void onItemClick(View view, int position, float x, float y) {
        if (position == sendReadPacketsRow) {
            ExteraConfig.editor.putBoolean("sendReadPackets", ExteraConfig.sendReadPackets ^= true).apply();
            ((TextCheckCell) view).setChecked(ExteraConfig.sendReadPackets);
        } else if (position == sendOnlinePacketsRow) {
            ExteraConfig.editor.putBoolean("sendOnlinePackets", ExteraConfig.sendOnlinePackets ^= true).apply();
            ((TextCheckCell) view).setChecked(ExteraConfig.sendOnlinePackets);
        } else if (position == sendUploadProgressRow) {
            ExteraConfig.editor.putBoolean("sendUploadProgress", ExteraConfig.sendUploadProgress ^= true).apply();
            ((TextCheckCell) view).setChecked(ExteraConfig.sendUploadProgress);
        } else if (position == sendOfflinePacketAfterOnlineRow) {
            ExteraConfig.editor.putBoolean("sendOfflinePacketAfterOnline", ExteraConfig.sendOfflinePacketAfterOnline ^= true).apply();
            ((TextCheckCell) view).setChecked(ExteraConfig.sendOfflinePacketAfterOnline);
        } else if (position == markReadAfterSendRow) {
            ExteraConfig.editor.putBoolean("markReadAfterSend", ExteraConfig.markReadAfterSend ^= true).apply();
            ((TextCheckCell) view).setChecked(ExteraConfig.markReadAfterSend);
        } else if (position == useScheduledMessagesRow) {
            ExteraConfig.editor.putBoolean("useScheduledMessages", ExteraConfig.useScheduledMessages ^= true).apply();
            ((TextCheckCell) view).setChecked(ExteraConfig.useScheduledMessages);
        } else if (position == keepDeletedMessagesRow) {
            ExteraConfig.editor.putBoolean("keepDeletedMessages", ExteraConfig.keepDeletedMessages ^= true).apply();
            ((TextCheckCell) view).setChecked(ExteraConfig.keepDeletedMessages);
        } else if (position == keepMessagesHistoryRow) {
            ExteraConfig.editor.putBoolean("keepMessagesHistory", ExteraConfig.keepMessagesHistory ^= true).apply();
            ((TextCheckCell) view).setChecked(ExteraConfig.keepMessagesHistory);
        } else if (position == realForwardTime) {
            ExteraConfig.editor.putBoolean("realForwardTime", ExteraConfig.realForwardTime ^= true).apply();
            ((TextCheckCell) view).setChecked(ExteraConfig.realForwardTime);
        } else if (position == showFromChannel) {
            ExteraConfig.editor.putBoolean("showFromChannel", ExteraConfig.showFromChannel ^= true).apply();
            ((TextCheckCell) view).setChecked(ExteraConfig.showFromChannel);
        } else if (position == keepAliveService) {
            ExteraConfig.editor.putBoolean("keepAliveService", ExteraConfig.keepAliveService ^= true).apply();
            ((TextCheckCell) view).setChecked(ExteraConfig.keepAliveService);
        } else if (position == walModeRow) {
            ExteraConfig.editor.putBoolean("walMode", ExteraConfig.walMode ^= true).apply();
            ((TextCheckCell) view).setChecked(ExteraConfig.walMode);
        } else if (position == deletedMarkText) {
            var builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("DeletedMarkText", R.string.DeletedMarkText));
            var layout = new LinearLayout(getParentActivity());
            var input = new EditTextSettingsCell(getParentActivity());
            input.setText(ExteraConfig.getDeletedMark(), true);

            layout.setGravity(LinearLayout.VERTICAL);
            layout.addView(input);
            builder.setView(layout);
            builder.setPositiveButton(LocaleController.getString("Save", R.string.Save), (dialog, which) -> {
                ExteraConfig.editor.putString("deletedMarkText", input.getText()).apply();
                ((TextCell) view).setTextAndValue(LocaleController.getString("DeletedMarkText", R.string.DeletedMarkText), ExteraConfig.getDeletedMark(), true);
            });
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), (dialog, which) -> dialog.cancel());

            builder.show();
        }
    }

    @Override
    protected String getTitle() {
        return LocaleController.getString("AyuPreferences", R.string.AyuPreferences);
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
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, boolean payload) {
            switch (holder.getItemViewType()) {
                case 2:
                    TextCell textCell = (TextCell) holder.itemView;
                    if (position == deletedMarkText) {
                        textCell.setTextAndValue(LocaleController.getString("DeletedMarkText", R.string.DeletedMarkText), ExteraConfig.getDeletedMark(), true);
                    }
                    break;
                case 3:
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == ghostEssentialsHeaderRow) {
                        headerCell.setText(LocaleController.getString("GhostEssentialsHeader", R.string.GhostEssentialsHeader));
                    } else if (position == spyHeaderRow) {
                        headerCell.setText(LocaleController.getString("SpyEssentialsHeader", R.string.SpyEssentialsHeader));
                    } else if (position == qolHeaderRow) {
                        headerCell.setText(LocaleController.getString("QoLTogglesHeader", R.string.QoLTogglesHeader));
                    } else if (position == customizationHeaderRow) {
                        headerCell.setText(LocaleController.getString("CustomizationHeader", R.string.CustomizationHeader));
                    }
                    break;
                case 5:
                    TextCheckCell textCheckCell = (TextCheckCell) holder.itemView;
                    textCheckCell.setEnabled(true, null);
                    if (position == sendReadPacketsRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("SendReadPackets", R.string.SendReadPackets), ExteraConfig.sendReadPackets, true);
                    } else if (position == sendOnlinePacketsRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("SendOnlinePackets", R.string.SendOnlinePackets), ExteraConfig.sendOnlinePackets, true);
                    } else if (position == sendUploadProgressRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("SendUploadProgress", R.string.SendUploadProgress), ExteraConfig.sendUploadProgress, true);
                    } else if (position == sendOfflinePacketAfterOnlineRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("SendOfflinePacketAfterOnline", R.string.SendOfflinePacketAfterOnline) + " β", ExteraConfig.sendOfflinePacketAfterOnline, true);
                    } else if (position == markReadAfterSendRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("MarkReadAfterSend", R.string.MarkReadAfterSend) + " β", ExteraConfig.markReadAfterSend, true);
                    } else if (position == useScheduledMessagesRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("UseScheduledMessages", R.string.UseScheduledMessages) + " β", ExteraConfig.useScheduledMessages, true);
                    } else if (position == keepDeletedMessagesRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("KeepDeletedMessages", R.string.KeepDeletedMessages) + " β", ExteraConfig.keepDeletedMessages, true);
                    } else if (position == keepMessagesHistoryRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("KeepMessagesHistory", R.string.KeepMessagesHistory) + " β", ExteraConfig.keepMessagesHistory, true);
                    } else if (position == realForwardTime) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("RealForwardTime", R.string.RealForwardTime), ExteraConfig.realForwardTime, true);
                    } else if (position == showFromChannel) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("ShowFromChannel", R.string.ShowFromChannel), ExteraConfig.showFromChannel, true);
                    } else if (position == keepAliveService) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("KeepAliveService", R.string.KeepAliveService) + " β", ExteraConfig.keepAliveService, true);
                    } else if (position == walModeRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("WALMode", R.string.WALMode), ExteraConfig.walMode, true);
                    }
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == deletedMarkText) {
                return 2;
            } else if (
                    position == ghostEssentialsHeaderRow ||
                            position == spyHeaderRow ||
                            position == qolHeaderRow ||
                            position == customizationHeaderRow
            ) {
                return 3;
            }
            return 5;
        }
    }
}
