//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2014 MStar Semiconductor, Inc. All rights reserved.
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
package com.jrm.localmm.business.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jrm.localmm.R;
import com.jrm.localmm.ui.video.PlaySettingSubtitleDialog;

/**
 * @author 蒋华全 (jianghq@biaoqi.com.cn)
 * @since 1.0
 * @date 2012-2-16
 */
public class SubtitleSettingListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ViewHolder holder;
    private int[] settingData;
    private String[] optionData;
    private Context context;
    private boolean isInner = true;
    private int num;
    private PlaySettingSubtitleDialog mPlaySettingSubtitleDialog = null;

    public SubtitleSettingListAdapter(Context context, int[] settingData,
            String[] optionData, boolean isInner, PlaySettingSubtitleDialog playSettingSubtitleDialog) {
        try {
            mInflater = LayoutInflater.from(context);
            this.context = context;
            this.settingData = settingData;
            this.optionData = optionData;
            this.isInner = isInner;
            this.num = this.settingData.length - 1;
            this.mPlaySettingSubtitleDialog = playSettingSubtitleDialog;
        } catch (Exception e) {
        }
    }

    public int getCount() {
        return this.num;
    }

    public Object getItem(int arg0) {
        return arg0;
    }

    public long getItemId(int arg0) {
        return arg0;
    }

    public View getView(int position, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.subtitle_setting_list_item, null);
            holder = new ViewHolder();
            holder.settingNameTextView = (TextView) convertView
                    .findViewById(R.id.settingNameTextView);
            holder.leftImageView = (ImageView) convertView
                    .findViewById(R.id.leftImageView);
            holder.settingOptionTextView = (TextView) convertView
                    .findViewById(R.id.settingOptionTextView);
            holder.rightImageView = (ImageView) convertView
                    .findViewById(R.id.rightImageView);
            if (position == 0 || (position == 1 && !isInner) || position == 9) {
                holder.leftImageView.setVisibility(View.INVISIBLE);
                holder.rightImageView.setVisibility(View.GONE);
            }
            if (mPlaySettingSubtitleDialog != null) {
                setClickListens(position);
            }
            convertView.setTag(holder);
        } else {
            holder.settingNameTextView = (TextView) convertView
                    .findViewById(R.id.settingNameTextView);
            holder.leftImageView = (ImageView) convertView
                    .findViewById(R.id.leftImageView);
            holder.settingOptionTextView = (TextView) convertView
                    .findViewById(R.id.settingOptionTextView);
            holder.rightImageView = (ImageView) convertView
                    .findViewById(R.id.rightImageView);
        }
        if (position == 0) {
            holder.settingNameTextView.setText(context
                    .getString(settingData[position]));
            if (isInner) {
                holder.settingOptionTextView.setText(this.context
                        .getString(R.string.subtitle_0_value_2));
            } else {
                holder.settingOptionTextView.setText(this.context
                        .getString(R.string.subtitle_0_value_1));
            }
        } else {
            if (isInner) {
                if (position >= 1 && position < num) {
                    holder.settingNameTextView.setText(context
                            .getString(settingData[position + 1]));
                    holder.settingOptionTextView
                            .setText(optionData[position + 1]);
                }
            } else {
                if (position >= 2 && position < num) {
                    holder.settingNameTextView.setText(context
                            .getString(settingData[position + 1]));
                    holder.settingOptionTextView
                            .setText(optionData[position + 1]);
                } else if (position == 1) {
                    holder.settingNameTextView.setText(context
                            .getString(settingData[position]));
                    holder.settingOptionTextView.setText(optionData[position]);
                }
            }
        }
        return convertView;
    }

    private void setClickListens(final int positon) {
        holder.rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlaySettingSubtitleDialog.handleImageClick(positon, false);
            }
        });
        holder.leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlaySettingSubtitleDialog.handleImageClick(positon, true);
            }
        });
        holder.settingOptionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlaySettingSubtitleDialog.handleMidClick(positon);
            }
        });

    }

    static class ViewHolder {
        TextView settingNameTextView;
        ImageView leftImageView;
        TextView settingOptionTextView;
        ImageView rightImageView;
    }
}
