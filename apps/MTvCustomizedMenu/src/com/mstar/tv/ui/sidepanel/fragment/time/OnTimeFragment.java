//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2015 MStar Semiconductor, Inc. All rights reserved.
// All software, firmware and related documentation herein ("MStar Software") are
// intellectual property of MStar Semiconductor, Inc. ("MStar") and protected by
// law, including, but not limited to, copyright law and international treaties.
// Any use, modification, reproduction, retransmission, or republication of all
// or part of MStar Software is expressly prohibited, unless prior written
// permission has been granted by MStar.
//
// By accessing, browsing and/or using MStar Software, you acknowledge that you
// have read, understood, and agree, to be bound by below terms ("Terms") and to
// comply with all applicable laws and regulations:
//
// 1. MStar shall retain any and all right, ownership and interest to MStar
//    Software and any modification/derivatives thereof.
//    No right, ownership, or interest to MStar Software and any
//    modification/derivatives thereof is transferred to you under Terms.
//
// 2. You understand that MStar Software might include, incorporate or be
//    supplied together with third party's software and the use of MStar
//    Software may require additional licenses from third parties.
//    Therefore, you hereby agree it is your sole responsibility to separately
//    obtain any and all third party right and license necessary for your use of
//    such third party's software.
//
// 3. MStar Software and any modification/derivatives thereof shall be deemed as
//    MStar's confidential information and you agree to keep MStar's
//    confidential information in strictest confidence and not disclose to any
//    third party.
//
// 4. MStar Software is provided on an "AS IS" basis without warranties of any
//    kind. Any warranties are hereby expressly disclaimed by MStar, including
//    without limitation, any warranties of merchantability, non-infringement of
//    intellectual property rights, fitness for a particular purpose, error free
//    and in conformity with any international standard.  You agree to waive any
//    claim against MStar for any loss, damage, cost or expense that you may
//    incur related to your use of MStar Software.
//    In no event shall MStar be liable for any direct, indirect, incidental or
//    consequential damages, including without limitation, lost of profit or
//    revenues, lost or damage of data, and unauthorized system use.
//    You agree that this Section 4 shall still apply without being affected
//    even if MStar Software has been modified by MStar in accordance with your
//    request or instruction for your use, except otherwise agreed by both
//    parties in writing.
//
// 5. If requested, MStar may from time to time provide technical supports or
//    services in relation with MStar Software to you for your use of
//    MStar Software in conjunction with your or your customer's product
//    ("Services").
//    You understand and agree that, except otherwise agreed by both parties in
//    writing, Services are provided on an "AS IS" basis and the warranty
//    disclaimer set forth in Section 4 above shall apply.
//
// 6. Nothing contained herein shall be construed as by implication, estoppels
//    or otherwise:
//    (a) conferring any license or right to use MStar name, trademark, service
//        mark, symbol or any other identification;
//    (b) obligating MStar or any of its affiliates to furnish any person,
//        including without limitation, you and your customers, any assistance
//        of any kind whatsoever, or any information; or
//    (c) conferring any license or right under any intellectual property right.
//
// 7. These terms shall be governed by and construed in accordance with the laws
//    of Taiwan, R.O.C., excluding its conflict of law rules.
//    Any and all dispute arising out hereof or related hereto shall be finally
//    settled by arbitration referred to the Chinese Arbitration Association,
//    Taipei in accordance with the ROC Arbitration Law and the Arbitration
//    Rules of the Association by three (3) arbitrators appointed in accordance
//    with the said Rules.
//    The place of arbitration shall be in Taipei, Taiwan and the language shall
//    be English.
//    The arbitration award shall be final and binding to both parties.
//
//******************************************************************************
//<MStar Software>

package com.mstar.tv.ui.sidepanel.fragment.time;

import android.view.View;
import android.widget.TextView;
import android.util.Log;
import android.os.Handler;
import android.text.format.Time;

import com.mstar.tv.MainActivity;
import com.mstar.tv.R;
import com.mstar.tv.ui.dialog.TimePickerDialogFragment;
import com.mstar.tv.ui.sidepanel.item.ActionItem;
import com.mstar.tv.ui.sidepanel.item.Item;
import com.mstar.tv.ui.sidepanel.item.SwitchItem;
import com.mstar.tv.ui.sidepanel.fragment.SideFragment;
import com.mstar.android.tv.TvTimerManager;
import com.mstar.android.tvapi.common.vo.StandardTime;
import com.mstar.android.tv.TvTimerManager;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Shows the off time setting
 */
public class OnTimeFragment extends SideFragment {
    private final static String TAG = "OnTimeFragment";

    private static Handler mHandler = new Handler();

    private static OnTimeSwitchItem onTimeSwitchItem;

    private static OnTimeHourItem onTimeHourItem;

    private static OnTimeSourceItem onTimeSourceItem;

    private static boolean mEnabled;

    private static int mHour = 0, mMin = 0;

    private Time mDateTime;

    /**
     * Set boot time on/off.
     */
    public class OnTimeSwitchItem extends SwitchItem {
        public final String DIALOG_TAG = OnTimeSwitchItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public OnTimeSwitchItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_time_set_time_on));
            mMainActivity = mainActivity;
            setChecked(TvTimerManager.getInstance().isOnTimerEnable());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvTimerManager.getInstance() == null) {
                        return;
                    }
                    // Check if Ginga is enabled and then set the ui
                    if (TvTimerManager.getInstance().isOnTimerEnable()) {
                        onTimeHourItem.setEnabled(true);
                        onTimeSourceItem.setEnabled(true);
                        setChecked(true);
                    } else {
                        onTimeHourItem.setEnabled(false);
                        onTimeSourceItem.setEnabled(false);
                        setChecked(false);
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            super.onSelected();
            if (isChecked()) {
                mEnabled = true;
                onTimeHourItem.setEnabled(true);
                onTimeSourceItem.setEnabled(true);
                TvTimerManager.getInstance().setOnTimerEnable(true);
            } else {
                mEnabled = false;
                onTimeHourItem.setEnabled(false);
                onTimeSourceItem.setEnabled(false);
                TvTimerManager.getInstance().setOnTimerEnable(false);
            }
        }
    }

    /**
     * Set boot time.
     */
    public class OnTimeHourItem extends ActionItem {
        public final String DIALOG_TAG = OnTimeHourItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public OnTimeHourItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_time_set_time_hour_minute));
            mMainActivity = mainActivity;
            mDescription = String.format("%02d:%02d", mHour, mMin);
            setDescription(mDescription);
        }

        @Override
        public void onSelected() {
            TimePickerDialogFragment dialog = new TimePickerDialogFragment(
                    mMainActivity.getString(R.string.str_time_schedule_ontime), mHour, mMin,
                    new TimePickerDialogFragment.ResultListener() {
                        @Override
                        public void done(int hour, int min) {
                            setDescription(String.format("%02d:%02d", hour, min));
                            Log.d(TAG, "on time, hour: " + hour + "min: " + min);
                            mDateTime = TvTimerManager.getInstance().getCurrentTvTime();
                            mDateTime.hour = hour;
                            mDateTime.minute = min;
                            TvTimerManager.getInstance().setTvOnTimer(mDateTime);
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    TvTimerManager.getInstance().setOnTimerEnable(true);
                                }
                            }, 500);
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    /**
     * Shows Source
     */
    public class OnTimeSourceItem extends ActionItem {
        public final String DIALOG_TAG = OnTimeSourceItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public OnTimeSourceItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_time_schedule_source));
            mMainActivity = mainActivity;
            if (TvTimerManager.getInstance().isOnTimerEnable()) {
                mDescription = "";
            } else {
                mDescription = "";
            }
            setDescription(mDescription);
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new SourceFragment(mDateTime));
        }
    }

    public OnTimeFragment(boolean enabled) {
        mEnabled = enabled;
        if (enabled == false) {
            Time mdateTime = TvTimerManager.getInstance().getCurrentTvTime();
            mHour = mdateTime.hour;
            mMin = mdateTime.minute;
            TvTimerManager.getInstance().setTvOnTimer(mdateTime);
        }
        mHour = TvTimerManager.getInstance().getTvOnTimer().hour;
        mMin = TvTimerManager.getInstance().getTvOnTimer().minute;
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.str_time_schedule_ontime);
    }

    @Override
    protected List<Item> getItemList() {
        List<Item> items = new ArrayList<>();
        onTimeSwitchItem = new OnTimeSwitchItem((MainActivity) getActivity());
        onTimeHourItem = new OnTimeHourItem((MainActivity) getActivity());
        onTimeSourceItem = new OnTimeSourceItem((MainActivity) getActivity());
        items.add(onTimeSwitchItem);
        items.add(onTimeHourItem);
        items.add(onTimeSourceItem);
        return items;
    }
}
