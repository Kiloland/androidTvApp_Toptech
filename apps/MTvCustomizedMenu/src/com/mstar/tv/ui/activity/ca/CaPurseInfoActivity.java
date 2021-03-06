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

package com.mstar.tv.ui.activity.ca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mstar.android.tv.TvCaManager;
import com.mstar.android.tvapi.dtv.vo.CaSlotIDs;
import com.mstar.android.tvapi.dtv.vo.CaSlotInfo;
import com.mstar.tv.R;
import com.mstar.tv.ui.activity.LittleDownTimer;
import com.mstar.tv.ui.activity.ca.CaErrorType.RETURN_CODE;

public class CaPurseInfoActivity extends Activity {
    private final String TAG = "CaPurseInfoActivity";

    private String OperatorId;

    private ListView listpurse;

    private final String CURRNT_OPERATOR_ID = "OperatorId";

    TvCaManager tvCaManager = null;

    int ST_CA_MAXNUM_SLOT = 20;

    private final List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (msg.what == LittleDownTimer.TIME_OUT_MSG) {
                finish();
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ca_purse_info);
        if ((getIntent() != null) && (getIntent().getExtras() != null)) {
            OperatorId = getIntent().getStringExtra(CURRNT_OPERATOR_ID);
            Log.d(TAG, "get String extra,OperatorId=" + OperatorId);
        }
        LittleDownTimer.setHandler(handler);
        findViews();
    }

    @Override
    protected void onResume() {
        Log.d("TvApp", "CaPurseInfoActivity onResume");
        LittleDownTimer.resumeMenu();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("TvApp", "CaPurseInfoActivity onPause");
        LittleDownTimer.pauseMenu();
        super.onPause();

    }

    @Override
    public void onUserInteraction() {
        Log.d("TvApp", "CaPurseInfoActivity onUserInteraction");
        LittleDownTimer.resetMenu();
        super.onUserInteraction();
    }

    private void findViews() {
        listpurse = (ListView) findViewById(R.id.listpurse);

        contents.clear();
        SimpleAdapter adapter = new SimpleAdapter(this, contents, R.layout.ca_purse_item,
                new String[] {
                        "purse_id", "spend_points", "credit"
                }, new int[] {
                        R.id.textview_ca_purse_item_id, R.id.textview_ca_purse_item_points,
                        R.id.textview_ca_purse_item_credit
                });

        getInitData();
        listpurse.setAdapter(adapter);
    }

    private void getInitData() {
        tvCaManager = TvCaManager.getInstance();
        CaSlotIDs SlotIDs = tvCaManager.CaGetSlotIDs(Short.parseShort(OperatorId));

        if (SlotIDs == null) {
            return;
        }
        if (SlotIDs.sSlotIDsState == RETURN_CODE.ST_CA_RC_OK.getRetCode()) {
            if (SlotIDs.pbySlotID.length() > 0) {
                int slotId[] = new int[ST_CA_MAXNUM_SLOT];
                int len = SlotIDs.pbySlotID.length();
                for (int i = 0; i < len; i++) {
                    if (SlotIDs.pbySlotID.codePointAt(i) == 0)// CA spec:if
                                                              // SlotId=0,need
                                                              // break
                    {
                        break;
                    }
                    slotId[i] = SlotIDs.pbySlotID.codePointAt(i);
                    Log.d(TAG, "sSlotIDsState :" + slotId[i]);

                    CaSlotInfo SlotInfo = tvCaManager.CaGetSlotInfo(Short.parseShort(OperatorId),
                            (short) slotId[i]);
                    if (SlotInfo == null
                            || SlotInfo.sSlotInfoState != RETURN_CODE.ST_CA_RC_OK.getRetCode()) {
                        continue;
                    }
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("purse_id", slotId[i]);
                    map.put("spend_points", SlotInfo.wBalance);
                    map.put("credit", SlotInfo.wCreditLimit);
                    contents.add(map);
                }
            }
        } else if (SlotIDs.sSlotIDsState == RETURN_CODE.ST_CA_RC_CARD_INVALID.getRetCode()) {
            toastShow(R.string.st_ca_rc_card_invalid);
        } else if (SlotIDs.sSlotIDsState == RETURN_CODE.ST_CA_RC_POINTER_INVALID.getRetCode()) {
            toastShow(R.string.st_ca_rc_pointer_invalid);
        } else if (SlotIDs.sSlotIDsState == RETURN_CODE.ST_CA_RC_DATA_NOT_FIND.getRetCode()) {
            toastShow(R.string.st_ca_rc_data_not_find);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_MENU:
                intent.setClass(CaPurseInfoActivity.this, CaInformationActivity.class);
                intent.putExtra(CURRNT_OPERATOR_ID, OperatorId);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void toastShow(int resId) {
        Toast toast = new Toast(this);
        TextView MsgShow = new TextView(this);
        toast.setDuration(Toast.LENGTH_LONG);
        MsgShow.setTextColor(Color.RED);
        MsgShow.setTextSize(25);
        MsgShow.setText(resId);
        toast.setView(MsgShow);
        toast.show();
    }
}
