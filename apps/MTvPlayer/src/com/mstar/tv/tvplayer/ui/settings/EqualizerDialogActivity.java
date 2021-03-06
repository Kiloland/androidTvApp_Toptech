//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2013 MStar Semiconductor, Inc. All rights reserved.
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

package com.mstar.tv.tvplayer.ui.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.LinearLayout;

import com.mstar.android.tv.TvAudioManager;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.LittleDownTimer;
import com.mstar.tv.tvplayer.ui.MainMenuActivity;
import com.mstar.tv.tvplayer.ui.RootActivity;
import com.mstar.tv.tvplayer.ui.TVRootApp;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tv.tvplayer.ui.component.SeekBarButton;
import com.mstar.tvframework.MstarBaseActivity;
import com.mstar.util.Utility;

public class EqualizerDialogActivity extends MstarBaseActivity {

    private SeekBarButton seekBarBtn120Hz;

    private SeekBarButton seekBarBtn500Hz;

    private SeekBarButton seekBarBtn1_5KHz;// 1.5khz

    private SeekBarButton seekBarBtn5KHz;

    private SeekBarButton seekBarBtn10KHz;

    private static final int STEP = 1;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == LittleDownTimer.TIME_OUT_MSG) {
                Intent intent = new Intent(EqualizerDialogActivity.this, RootActivity.class);
                startActivity(intent);
                finish();
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equalizer_dialog);
        TVRootApp app = (TVRootApp) getApplication();
        seekBarBtn120Hz = new SeekBarButton(this, R.id.linearlayout_sound_equalizer120hz, STEP,
                false) {
            @Override
            public void doUpdate() {
                TvAudioManager.getInstance().setEqBand120(seekBarBtn120Hz.getProgress());
            }
        };

        seekBarBtn500Hz = new SeekBarButton(this,

        R.id.linearlayout_sound_equalizer500hz, STEP, false) {
            @Override
            public void doUpdate() {
                TvAudioManager.getInstance().setEqBand500(seekBarBtn500Hz.getProgress());
            }
        };

        seekBarBtn1_5KHz = new SeekBarButton(this, R.id.linearlayout_sound_equalizer1_5khz, STEP,
                false) {
            @Override
            public void doUpdate() {
                TvAudioManager.getInstance().setEqBand1500(seekBarBtn1_5KHz.getProgress());
            }
        };

        seekBarBtn5KHz = new SeekBarButton(this, R.id.linearlayout_sound_equalizer5khz, STEP, false) {
            @Override
            public void doUpdate() {
                TvAudioManager.getInstance().setEqBand5k(seekBarBtn5KHz.getProgress());
            }
        };

        seekBarBtn10KHz = new SeekBarButton(this, R.id.linearlayout_sound_equalizer10khz, STEP,
                false) {
            @Override
            public void doUpdate() {
                TvAudioManager.getInstance().setEqBand10k(seekBarBtn10KHz.getProgress());
            }
        };

        LoadDataToDialog();
        setOnFocusChangeListeners();
        seekBarBtn120Hz.setFocused();
        LittleDownTimer.setHandler(handler);
    }

    @Override
    protected void onResume() {
        LittleDownTimer.resumeMenu();
        super.onResume();
    }

    @Override
    public void onUserInteraction() {
        LittleDownTimer.resetMenu();
        super.onUserInteraction();
    }

    @Override
    protected void onPause() {
        LittleDownTimer.pauseMenu();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TvIntent.MAINMENU);
        intent.putExtra("currentPage", MainMenuActivity.SOUND_PAGE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        finish();
        super.onBackPressed();
    }

    private void LoadDataToDialog() {
        seekBarBtn120Hz.setProgress((short) TvAudioManager.getInstance().getEqBand120());
        seekBarBtn500Hz.setProgress((short) TvAudioManager.getInstance().getEqBand500());
        seekBarBtn1_5KHz.setProgress((short) TvAudioManager.getInstance().getEqBand1500());
        seekBarBtn5KHz.setProgress((short) TvAudioManager.getInstance().getEqBand5k());
        seekBarBtn10KHz.setProgress((short) TvAudioManager.getInstance().getEqBand10k());
    }

    private void setOnFocusChangeListeners() {
        OnFocusChangeListener seekBarBtnFocusListenser = new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    LinearLayout container = (LinearLayout) v;
                    container.getChildAt(2).setVisibility(View.VISIBLE);
                    Utility.ttsSepakLinearLayout(container);
                } else {
                    LinearLayout container = (LinearLayout) v;
                    container.getChildAt(2).setVisibility(View.GONE);
                }
            }
        };

        seekBarBtn120Hz.setOnFocusChangeListener(seekBarBtnFocusListenser);
        seekBarBtn500Hz.setOnFocusChangeListener(seekBarBtnFocusListenser);
        seekBarBtn1_5KHz.setOnFocusChangeListener(seekBarBtnFocusListenser);
        seekBarBtn5KHz.setOnFocusChangeListener(seekBarBtnFocusListenser);
        seekBarBtn10KHz.setOnFocusChangeListener(seekBarBtnFocusListenser);

        OnClickListener seekBarBtnOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        };

        seekBarBtn120Hz.setOnClickListener(seekBarBtnOnClickListener);
        seekBarBtn500Hz.setOnClickListener(seekBarBtnOnClickListener);
        seekBarBtn1_5KHz.setOnClickListener(seekBarBtnOnClickListener);
        seekBarBtn5KHz.setOnClickListener(seekBarBtnOnClickListener);
        seekBarBtn10KHz.setOnClickListener(seekBarBtnOnClickListener);
    }
}
