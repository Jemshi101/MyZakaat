package com.quartzbit.myzakaat.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import com.quartzbit.myzakaat.R;
import com.quartzbit.myzakaat.adapter.MediaListRecyclerAdapter;
import com.quartzbit.myzakaat.adapter.MediaViewPagerAdapter;
import com.quartzbit.myzakaat.app.App;
import com.quartzbit.myzakaat.firebase.FirebaseDataManager;
import com.quartzbit.myzakaat.fragments.MediaViewFragment;
import com.quartzbit.myzakaat.listeners.BasicListener;
import com.quartzbit.myzakaat.model.BasicBean;
import com.quartzbit.myzakaat.model.MediaBean;
import com.quartzbit.myzakaat.model.MediaListBean;
import com.quartzbit.myzakaat.util.AppConstants;

public class MediaViewActivity extends BaseAppCompatNoDrawerActivity
        implements MediaViewFragment.MediaViewFragmentListener {

    private MediaListBean mediaListBean;
    private int mediaPosition;
    private ArrayList<MediaBean> mediaList;
    private int mediaSize = 0;
    private MediaViewPagerAdapter pageAdapter;
    private ViewPager pager;
    private RecyclerView rvGallery;
    private LinearLayoutManager linearLayoutManager;
    private TextView txtMediaTitle;
    private TextView txtMediaPosition;
    private MediaListRecyclerAdapter adapterGallery;
    private String galleryMusicURL = "";
    private MediaPlayer mPlayer;
    private boolean isPlaying=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_view);

        getSupportActionBar().hide();
        swipeView.setPadding(0, 0, 0, 0);

        if (null != getIntent().getExtras()) {
            mediaListBean = (MediaListBean) getIntent().getSerializableExtra(AppConstants.EXTRA_PROFILE_TO_IMAGE_MEDIA_LIST);
            mediaPosition = getIntent().getExtras().getInt(AppConstants.EXTRA_PROFILE_TO_IMAGE_SELECTED_POSITION);
            mediaList = mediaListBean.getMediaList();
            try {
                mediaSize = getIntent().getExtras().getInt(AppConstants.EXTRA_PROFILE_TO_IMAGE_LIST_SIZE);
            } catch (Exception e) {
                mediaSize = mediaList.size() - 1;
                e.printStackTrace();
            }
        }
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();

        fetchGalleryMusic();
    }

    private void fetchGalleryMusic() {

        JSONObject postData=new JSONObject();


        FirebaseDataManager.fetchGalleryMusic(postData, new BasicListener() {
            @Override
            public void onLoadCompleted(BasicBean basicBean) {

                galleryMusicURL=basicBean.getUrl();
                if(!galleryMusicURL.equals("")){
                    playBackgroundMusic();
                }

            }

            @Override
            public void onLoadFailed(String error) {

            }
        });
    }

    private void initViews() {
        pager = (ViewPager) findViewById(R.id.pager_media_view);
        r = getResources();
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, r.getDisplayMetrics());
        width = r.getDisplayMetrics().widthPixels;
        height = r.getDisplayMetrics().heightPixels;

        txtMediaTitle = (TextView) findViewById(R.id.txt_media_view_title);
        txtMediaPosition = (TextView) findViewById(R.id.txt_media_view_position);

        rvGallery = (RecyclerView) findViewById(R.id.rv_media_view);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvGallery.setLayoutManager(linearLayoutManager);

        if (mediaListBean != null && !mediaListBean.getMediaList().isEmpty()) {
            txtMediaTitle.setText(mediaList.get(0).getTitle());
            String strPos = (mediaPosition + 1) + AppConstants.SPACE + getString(R.string.label_of)
                    + AppConstants.SPACE + mediaSize;
            txtMediaPosition.setText(strPos);
        }

        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setLooping(true);

        pageAdapter = new MediaViewPagerAdapter(getSupportFragmentManager(), mediaListBean);

        pager.setAdapter(pageAdapter);
        pager.setCurrentItem(mediaPosition);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //System.out.println("############# page no: "+arg0);

                txtMediaTitle.setText(mediaList.get(position).getTitle());

                String strPos = (position + 1) + AppConstants.SPACE + getString(R.string.label_of)
                        + AppConstants.SPACE + mediaSize;
                txtMediaPosition.setText(strPos);
                rvGallery.smoothScrollToPosition(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });


        adapterGallery = new MediaListRecyclerAdapter(this, mediaListBean);
        adapterGallery.setMediaListRecyclerAdapterListener(new MediaListRecyclerAdapter.MediaListRecyclerAdapterListener() {
            @Override
            public void onItemClicked(int position) {
                pager.setCurrentItem(position);
                rvGallery.smoothScrollToPosition(position);
            }

            @Override
            public void onSnackBarShow(String message) {

            }
        });
        rvGallery.setAdapter(adapterGallery);

        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer player) {
                try {
                    player.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                isPlaying = true;
            }
        });

    }

    private void playBackgroundMusic() {

        if (mPlayer == null) {
            mPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(galleryMusicURL));
            /*mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    isPlaying = true;
                }
            });*/
        } else {
            try {
                mPlayer.setDataSource(getApplicationContext(), Uri.parse(galleryMusicURL));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mPlayer.prepareAsync();

    }

    private void stopPlaying() {
        try {
            if (mPlayer != null && isPlaying) {
                mPlayer.stop();
                isPlaying = false;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        stopPlaying();
        mPlayer.release();
        super.onDestroy();
    }
}
