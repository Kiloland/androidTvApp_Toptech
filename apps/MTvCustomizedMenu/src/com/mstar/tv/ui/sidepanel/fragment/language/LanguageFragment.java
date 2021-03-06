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

package com.mstar.tv.ui.sidepanel.fragment.language;

import android.view.View;
import android.widget.TextView;
import android.util.Log;
import android.os.Handler;
import android.os.Bundle;

import com.mstar.tv.MainActivity;
import com.mstar.tv.R;

import java.util.ArrayList;
import java.util.List;

import com.mstar.tv.ui.sidepanel.item.ActionItem;
import com.mstar.tv.ui.sidepanel.item.CheckBoxItem;
import com.mstar.tv.ui.sidepanel.item.CompoundButtonItem;
import com.mstar.tv.ui.sidepanel.item.DividerItem;
import com.mstar.tv.ui.sidepanel.item.Item;
import com.mstar.tv.ui.sidepanel.item.RadioButtonItem;
import com.mstar.tv.ui.sidepanel.item.SubMenuItem;
import com.mstar.tv.ui.sidepanel.item.SwitchItem;
import com.mstar.tv.ui.sidepanel.fragment.SideFragment;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvLanguage;
import com.mstar.android.tv.TvChannelManager;

/**
 * Shows the language setting information.
 */
public class LanguageFragment extends SideFragment {
    private final static String TAG = "LanguageFragment";

    private final int TYPE_AUDIO_PRIMARY_LANGUAGE = 0;

    private final int TYPE_AUDIO_SECONDARY_LANGUAGE = 1;

    private final int TYPE_SUBTITLE_PRIMARY_LANGUAGE = 2;

    private final int TYPE_SUBTITLE_SECONDARY_LANGUAGE = 3;

    private String[] mAudioLanguages;

    private String[] mSubtitleLanguages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAudioLanguages = getActivity().getResources().getStringArray(
                R.array.str_arr_language_audio_language);
        mSubtitleLanguages = getActivity().getResources().getStringArray(
                R.array.str_arr_language_subtitle_language);
    }

    /**
     * Shows the audio primary language setting
     */
    public class AudioPrimaryLanguageItem extends ActionItem {
        public final String DIALOG_TAG = AudioPrimaryLanguageItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public AudioPrimaryLanguageItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_language_audio_language1));
            mMainActivity = mainActivity;
            setDescription(getLanguageText(TYPE_AUDIO_PRIMARY_LANGUAGE));
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new AudioPrimaryLanguageFragment());
        }
    }

    /**
     * Shows the audio secondary language setting
     */
    public class AudioSecondaryLanguageItem extends ActionItem {
        public final String DIALOG_TAG = AudioSecondaryLanguageItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public AudioSecondaryLanguageItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_language_audio_language2));
            mMainActivity = mainActivity;
            setDescription(getLanguageText(TYPE_AUDIO_SECONDARY_LANGUAGE));
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new AudioSecondaryLanguageFragment());
        }
    }

    /**
     * Shows the audio subtitle setting
     */
    public class SubtitleItem extends SwitchItem {
        public final String DIALOG_TAG = SubtitleItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public SubtitleItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_language_subtitle_switch));
            mMainActivity = mainActivity;
            setChecked(TvCommonManager.getInstance().isSubtitleEnable());
        }

        @Override
        public void onSelected() {
            super.onSelected();
            TvCommonManager.getInstance().setSubtitleEnable(isChecked());
        }
    }

    /**
     * Shows the subtitle primary language setting
     */
    public class SubtitlePrimaryLanguageItem extends ActionItem {
        public final String DIALOG_TAG = SubtitlePrimaryLanguageItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public SubtitlePrimaryLanguageItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_language_subtitle_language1));
            mMainActivity = mainActivity;
            setDescription(getLanguageText(TYPE_SUBTITLE_PRIMARY_LANGUAGE));
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new SubtitlePrimaryLanguageFragment());
        }
    }

    /**
     * Shows the subtitle secondary language setting
     */
    public class SubtitleSecondaryLanguageItem extends ActionItem {
        public final String DIALOG_TAG = SubtitleSecondaryLanguageItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public SubtitleSecondaryLanguageItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_language_subtitle_language2));
            mMainActivity = mainActivity;
            setDescription(getLanguageText(TYPE_SUBTITLE_SECONDARY_LANGUAGE));
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new SubtitleSecondaryLanguageFragment());
        }
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.str_language_title);
    }

    @Override
    protected List<Item> getItemList() {
        List<Item> items = new ArrayList<>();
        items.add(new AudioPrimaryLanguageItem((MainActivity) getActivity()));
        items.add(new AudioSecondaryLanguageItem((MainActivity) getActivity()));
        //items.add(new SubtitleItem((MainActivity) getActivity()));
        //items.add(new SubtitlePrimaryLanguageItem((MainActivity) getActivity()));
        //items.add(new SubtitleSecondaryLanguageItem((MainActivity) getActivity()));
        return items;
    }

    private String getLanguageText(int type) {
        int idx;
        String strText = "";
        switch (type) {
            case TYPE_AUDIO_PRIMARY_LANGUAGE:
                idx = TvChannelManager.getInstance().getAudioLanguageDefaultValue();
                if (idx <= mAudioLanguages.length) {
                    strText = mAudioLanguages[idx];
                }
                break;
            case TYPE_AUDIO_SECONDARY_LANGUAGE:
                idx = TvChannelManager.getInstance().getAudioLanguageSecondaryValue();
                if (idx <= mAudioLanguages.length) {
                    strText = mAudioLanguages[idx];
                }
                break;
            case TYPE_SUBTITLE_PRIMARY_LANGUAGE:
                idx = TvCommonManager.getInstance().getSubtitlePrimaryLanguage();
                if (idx <= mSubtitleLanguages.length) {
                    strText = mSubtitleLanguages[idx];
                }
                break;
            case TYPE_SUBTITLE_SECONDARY_LANGUAGE:
                idx = TvCommonManager.getInstance().getSubtitleSecondaryLanguage();
                if (idx <= mSubtitleLanguages.length) {
                    strText = mSubtitleLanguages[idx];
                }
                break;
            default:
                break;
        }
        return strText;
    }
}
