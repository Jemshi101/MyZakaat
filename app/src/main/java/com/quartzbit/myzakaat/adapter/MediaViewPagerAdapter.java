package com.quartzbit.myzakaat.adapter;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import com.quartzbit.myzakaat.fragments.MediaViewFragment;
import com.quartzbit.myzakaat.model.MediaBean;
import com.quartzbit.myzakaat.model.MediaListBean;

/**
 * Created by Jemsheer K D on 07 May, 2018.
 * Package com.quartzbit.myzakaat.adapter
 * Project Dearest
 */
public class MediaViewPagerAdapter extends FragmentStatePagerAdapter {

    private MediaListBean mediaListBean;
    private List<MediaViewFragment> fragmentList;

    public MediaViewPagerAdapter(FragmentManager fm, MediaListBean mediaListBean) {
        super(fm);
        this.mediaListBean = mediaListBean;
        setFragmentList();
    }

    @Override
    public Fragment getItem(int position) {

        return getFragment(position);
    }

    @Override
    public int getCount() {
        return mediaListBean.getMediaList().size();
    }

    private void setFragmentList() {
        fragmentList = new ArrayList<>();
        for (MediaBean bean : mediaListBean.getMediaList()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("bean", bean);
            MediaViewFragment mediaViewFragment = new MediaViewFragment();
            mediaViewFragment.setArguments(bundle);
            fragmentList.add(mediaViewFragment);
        }
    }

    public MediaViewFragment getFragment(int position) {
        if (fragmentList != null && !fragmentList.isEmpty()) {
            return fragmentList.get(position);
        } else {
            setFragmentList();
            return getFragment(position);
        }
    }
}
