/*
 * Copyright (C) 2023 LineVall
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
package com.linevall.unsa.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;
import android.provider.SearchIndexableResource;

import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexable;

import java.util.Locale;
import android.text.TextUtils;
import android.view.View;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

@SearchIndexable
public class Misc extends SettingsPreferenceFragment {

    public static final String TAG = "Misc";

    private static final String LOCKSCREEN_GESTURES_CATEGORY = "lockscreen_gestures_category";
    private static final String KEY_RIPPLE_EFFECT = "enable_ripple_effect";

    private Preference mRippleEffect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.linevall_misc);

        PreferenceCategory gestCategory = (PreferenceCategory) findPreference(LOCKSCREEN_GESTURES_CATEGORY);

        FingerprintManager mFingerprintManager = (FingerprintManager)
                getActivity().getSystemService(Context.FINGERPRINT_SERVICE);
        mRippleEffect = (Preference) findPreference(KEY_RIPPLE_EFFECT);

        if (mFingerprintManager == null || !mFingerprintManager.isHardwareDetected()) {
            gestCategory.removePreference(mRippleEffect);
        }
    }

    public static void reset(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        Settings.System.putIntForUser(resolver,
                Settings.System.ENABLE_RIPPLE_EFFECT, 1, UserHandle.USER_CURRENT);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.LINEVALL;
    }

    /**
     * For search
     */
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider(R.xml.linevall_misc) {

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    List<String> keys = super.getNonIndexableKeys(context);

                    FingerprintManager mFingerprintManager = (FingerprintManager)
                            context.getSystemService(Context.FINGERPRINT_SERVICE);
                    if (mFingerprintManager == null || !mFingerprintManager.isHardwareDetected()) {
                        keys.add(KEY_RIPPLE_EFFECT);
                    }

                    return keys;
                }
            };
}
