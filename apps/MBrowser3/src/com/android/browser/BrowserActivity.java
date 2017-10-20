/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.browser;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.input.IInputManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.content.IntentFilter;

import com.mstar.android.MKeyEvent;

import com.android.browser.stub.NullController;
import com.google.common.annotations.VisibleForTesting;

import android.os.Debug;
import java.util.List;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.Handler;
import java.lang.Runnable;
import android.os.SystemProperties;
import java.io.IOException;
import android.content.BroadcastReceiver;
import java.util.Locale;
// MStar Android Patch Begin
//import android.webkit.HTML5VideoFullScreen;
// MStar Android Patch End

public class BrowserActivity extends Activity {

    public static final String ACTION_SHOW_BOOKMARKS = "show_bookmarks";
    public static final String ACTION_SHOW_BROWSER = "show_browser";
    public static final String ACTION_RESTART = "--restart--";
    private static final String EXTRA_STATE = "state";
    public static final String EXTRA_DISABLE_URL_OVERRIDE = "disable_url_override";
    private final static String LOGTAG = "browser";
    public static boolean mPIPOpen = false;

    private final static boolean LOGV_ENABLED = MBrowser.LOGV_ENABLED;

    private ActivityController mController = NullController.INSTANCE;


    // Mstar Android Patch Begin
    // add by liao.fan
    public static final String PIP_BROADCAST = "source.switch.from.storage";
    public static boolean mPipEnterFullScreen = false;
    // Mstar Android Patch End


    @Override
    public void onCreate(Bundle icicle) {
        if (LOGV_ENABLED) {
            Log.v(LOGTAG, this + " onStart, has state: "
                    + (icicle == null ? "false" : "true"));
        }
        super.onCreate(icicle);
		closeInputDevice();

        if (shouldIgnoreIntents()) {
            finish();
            return;
        }

        // If this was a web search request, pass it on to the default web
        // search provider and finish this activity.
        if (IntentHandler.handleWebSearchIntent(this, null, getIntent())) {
            finish();
            return;
        }
        mController = createController();

        Intent intent = (icicle == null) ? getIntent() : null;
        mController.start(intent);
        //SystemProperties.set("mstar.browser.optimized", "true");

        // Mstar Android Patch Begin
        // add by liao.fan
        IntentFilter filter = new IntentFilter();
        filter.addAction(PIP_BROADCAST);
        registerReceiver(mPipBroadcastReceiver, filter);
        // Mstar Android Patch End
    }

	public void closeInputDevice() {
		new Thread() {
			
			public void run() {
				IInputManager inputManager = IInputManager.Stub.asInterface(ServiceManager.getService(INPUT_SERVICE));
		        try {
					inputManager.closeInputDevice();
				} catch (RemoteException e) {
				}
			};
			
		}.start();
	}

    public static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.isTablet);
    }

    private Controller createController() {
        Controller controller = new Controller(this);
        boolean xlarge = isTablet(this);
        UI ui = null;
        if (xlarge) {
            ui = new XLargeUi(this, controller);
        } else {
            ui = new PhoneUi(this, controller);
        }
        controller.setUi(ui);
        return controller;
    }

    @VisibleForTesting
    Controller getController() {
        return (Controller) mController;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (shouldIgnoreIntents()) return;
        if (ACTION_RESTART.equals(intent.getAction())) {
            Bundle outState = new Bundle();
            mController.onSaveInstanceState(outState);
            finish();
            getApplicationContext().startActivity(
                    new Intent(getApplicationContext(), BrowserActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(EXTRA_STATE, outState));
            return;
        }
        mController.handleNewIntent(intent);
    }

    private KeyguardManager mKeyguardManager;
    private PowerManager mPowerManager;
    private boolean shouldIgnoreIntents() {
        // Only process intents if the screen is on and the device is unlocked
        // aka, if we will be user-visible
        if (mKeyguardManager == null) {
            mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        }
        if (mPowerManager == null) {
            mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        }
        boolean ignore = !mPowerManager.isScreenOn();
        ignore |= mKeyguardManager.inKeyguardRestrictedInputMode();
        if (LOGV_ENABLED) {
            Log.v(LOGTAG, "ignore intents: " + ignore);
        }
        return ignore;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LOGV_ENABLED) {
            Log.v(LOGTAG, "BrowserActivity.onResume: this=" + this);
        }
        // Mstar Android Patch Begin
        SystemProperties.set("ctl.start", "browserserver");
        // add by liao.fan
        mPipEnterFullScreen = false;
        // Mstar Android Patch End
        mController.onResume();
		String mCurrentLanguage = Locale.getDefault().getLanguage();
        if(mCurrentLanguage.equals("zh")){
        	((Controller)mController).loadUrlFromContext("http://www.baidu.com");
		}
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (Window.FEATURE_OPTIONS_PANEL == featureId) {
            mController.onMenuOpened(featureId, menu);
        }
        return true;
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        mController.onOptionsMenuClosed(menu);
    }

    @Override
    public void onContextMenuClosed(Menu menu) {
        super.onContextMenuClosed(menu);
        mController.onContextMenuClosed(menu);
    }

    /**
     *  onSaveInstanceState(Bundle map)
     *  onSaveInstanceState is called right before onStop(). The map contains
     *  the saved state.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (LOGV_ENABLED) {
            Log.v(LOGTAG, "BrowserActivity.onSaveInstanceState: this=" + this);
        }
        mController.onSaveInstanceState(outState);
    }

    // MStar Android Patch Begin
    @Override
    protected void onStop() {
        // Mstar Android Patch Begin
        // add by liao.fan
        mPipEnterFullScreen = false;
        // Mstar Android Patch End

        mController.onStop();
        super.onStop();
        SystemProperties.set("ctl.stop", "browserserver");
    }
    // MStar Android Patch End

    @Override
    protected void onPause() {
        mController.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (LOGV_ENABLED) {
            Log.v(LOGTAG, "BrowserActivity.onDestroy: this=" + this);
        }
        super.onDestroy();
        mController.onDestroy();
        mController = NullController.INSTANCE;
        unregisterReceiver(mPipBroadcastReceiver);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mController.onConfgurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.i(LOGTAG,"onLowMeory is received");
        mController.onLowMemory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return mController.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return mController.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!mController.onOptionsItemSelected(item)) {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        mController.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return mController.onContextItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mController.onKeyDown(keyCode, event) ||
            super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return mController.onKeyLongPress(keyCode, event) ||
            super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mController.onKeyUp(keyCode, event) ||
            super.onKeyUp(keyCode, event);
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        super.onActionModeStarted(mode);
        mController.onActionModeStarted(mode);
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
        mController.onActionModeFinished(mode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent intent) {
        mController.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public boolean onSearchRequested() {
        return mController.onSearchRequested();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
      // MStar Android Patch Begin
        if(event.getAction() == KeyEvent.ACTION_DOWN &&
            ( event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP ||
              event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)){
            return false;
        }else if(event.getAction() == KeyEvent.ACTION_DOWN &&
            ( event.getKeyCode() == MKeyEvent.KEYCODE_TV_SETTING ||
              event.getKeyCode() == KeyEvent.KEYCODE_TV_INPUT)){
            mPIPOpen = true;
            return false;
        }
      // MStar Android Patch End
        return mController.dispatchKeyEvent(event)
                || super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        return mController.dispatchKeyShortcutEvent(event)
                || super.dispatchKeyShortcutEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mController.dispatchTouchEvent(ev)
                || super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent ev) {
        return mController.dispatchTrackballEvent(ev)
                || super.dispatchTrackballEvent(ev);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        return mController.dispatchGenericMotionEvent(ev) ||
                super.dispatchGenericMotionEvent(ev);
    }


    // Mstar Android Patch Begin
    // add by liao.fan
    private BroadcastReceiver mPipBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (PIP_BROADCAST.equals(action)) {
                Log.d(LOGTAG,"browser broadcast receive: " + action);
                mPipEnterFullScreen = true;
            }
        }
    };
    // Mstar Android Patch End

}
