package com.example.ekta.evaluation.utilities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.example.ekta.evaluation.constants.Constants;


/**
 * Utility class for showing/dismissing loader.
 */
public class LoaderDialogUtil {

    private static LoaderDialogUtil sDialogUtil;

    private LoaderDialogUtil() {

    }


    public static LoaderDialogUtil getInstance() {

        if (sDialogUtil == null)
            sDialogUtil = new LoaderDialogUtil();

        return sDialogUtil;
    }


    public void showLoader(FragmentActivity activity) {
        try {
            if (findFragmentByTag(activity.getSupportFragmentManager(), Constants
                    .LOADER_FRAGMENT) == null) {
                LoaderDialogFragment dialogFragment = new LoaderDialogFragment();
                dialogFragment.setCancelable(false);
                dialogFragment.show(activity.getSupportFragmentManager(), Constants.
                        LOADER_FRAGMENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dismissLoader(FragmentActivity activity) {
        try {
            if (findFragmentByTag(activity.getSupportFragmentManager(), Constants
                    .LOADER_FRAGMENT) != null) {
                LoaderDialogFragment dialogFragment = (LoaderDialogFragment)
                        findFragmentByTag(activity.getSupportFragmentManager(), Constants
                                .LOADER_FRAGMENT);
                dialogFragment.dismissAllowingStateLoss();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Fragment findFragmentByTag(FragmentManager fragmentManager, String tag) {
        if (fragmentManager != null) {
            return fragmentManager.findFragmentByTag(tag);
        }

        return null;
    }

}
