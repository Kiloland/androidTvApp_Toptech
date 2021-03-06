//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2016 MStar Semiconductor, Inc. All rights reserved.
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

package com.mstar.android.tv.inputservice.ui;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.mstar.android.tv.inputservice.R;

/**
 * OverlayView
 */
public class OverlayView extends RelativeLayout {
    private static final String TAG = "OverlayView";

    private static final boolean DEBUG = Log.isLoggable(TAG, Log.DEBUG);

    private TextView mEmergencyAlertTextView;

    private TextView mScreenSaverTextView;

    private TextView mSubtitleTextView;

    public OverlayView(Context context) {
        this(context, null);
    }

    public OverlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverlayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mEmergencyAlertTextView = (TextView) findViewById(R.id.emergency_alert_text);
        mScreenSaverTextView = (TextView) findViewById(R.id.screen_saver_text);
        mSubtitleTextView = (TextView) findViewById(R.id.subtitle);
    }

    /**
     * Update the emergency alert text view.
     *
     * @param text A String that includes emergency alert text.
     */
    public void updateEmergencyAlertTextView(String text) {
        if (mEmergencyAlertTextView == null ||
                mScreenSaverTextView.isShown()) {
            Log.d(TAG, "updateEmergencyAlertTextView: mEmergencyAlertTextView == null || mScreenSaverTextView.isShown(), return");
            return;
        }
        if (!TextUtils.isEmpty(text)) {
            if (mEmergencyAlertTextView.getText().equals(text) &&
                    mEmergencyAlertTextView.isShown()) {
                return;
            }
            if (DEBUG) Log.d(TAG, "updateEmergencyAlertTextView: text = " + text);
            mEmergencyAlertTextView.setText(text);
            mEmergencyAlertTextView.setSelected(true);
            mEmergencyAlertTextView.setVisibility(View.VISIBLE);
        } else {
            dismissEmergencyAlertTextView();
        }
    }

    /**
     * Stop scrolling emergency alert text.
     */
    public void stopScrollEmergencyAlertTextView() {
        if (mEmergencyAlertTextView == null) {
            return;
        }
        if (DEBUG) Log.d(TAG, "stopScrollEmergencyAlertTextView");
        mEmergencyAlertTextView.setSelected(false);
    }

    /**
     * Dismiss the emergency alert text view.
     */
    public void dismissEmergencyAlertTextView() {
        if (mEmergencyAlertTextView == null) {
            return;
        }
        if (DEBUG) Log.d(TAG, "dismissEmergencyAlertTextView");
        mEmergencyAlertTextView.setSelected(false);
        mEmergencyAlertTextView.setVisibility(View.GONE);
    }

    /**
     * Update the screen saver text view.
     *
     * @param text A String that includes screen saver text.
     */
    public void updateScreenSaverTextView(String text) {
        if (mScreenSaverTextView == null) {
            Log.d(TAG, "updateScreenSaverTextView: mScreenSaverTextView == null, return");
            return;
        }
        if (!TextUtils.isEmpty(text)) {
            if (mScreenSaverTextView.getText().equals(text) &&
                    mScreenSaverTextView.isShown()) {
                return;
            }
            if (DEBUG) Log.d(TAG, "updateScreenSaverTextView: text = " + text);
            if (mEmergencyAlertTextView.isShown()) {
                Log.d(TAG, "updateScreenSaverTextView: mEmergencyAlertTextView is shown, dismiss it");
                dismissEmergencyAlertTextView();
            }
            mScreenSaverTextView.setText(text);
            mScreenSaverTextView.setVisibility(View.VISIBLE);
        } else {
            dismissScreenSaverTextView();
        }
    }

    /**
     * Dismiss the screen saver text view.
     */
    public void dismissScreenSaverTextView() {
        if (mScreenSaverTextView == null) {
            return;
        }
        if (DEBUG) Log.d(TAG, "dismissScreenSaverTextView");
        mScreenSaverTextView.setVisibility(View.GONE);
    }
}
