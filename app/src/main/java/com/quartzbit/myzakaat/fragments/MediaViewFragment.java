package com.quartzbit.myzakaat.fragments;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.Timer;
import java.util.TimerTask;

import com.quartzbit.myzakaat.R;
import com.quartzbit.myzakaat.app.App;
import com.quartzbit.myzakaat.model.MediaBean;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MediaViewFragmentListener} interface
 * to handle interaction events.
 */
public class MediaViewFragment extends BaseFragment {

    private static final int TYPE_PHOTO = 0;
    private MediaViewFragmentListener mListener;
    private ProgressBar progress;
    private ImageView ivPhoto;
    private LinearLayout llVideo;
    private VideoView videoView;
    private MediaBean mediaBean;
    private Timer timer;

    public MediaViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initBase(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_media_view, null);
        lytContent.addView(rootView);


        initView(rootView);

        return lytBase;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getArguments() != null && getArguments().containsKey("bean"))
            mediaBean = (MediaBean) getArguments().getSerializable("bean");

        if (mediaBean != null && getActivity() != null)
            populateMedia();


    }

    private void initView(View rootView) {

        progress = (ProgressBar) rootView.findViewById(R.id.progress_media_view);
        ivPhoto = (ImageView) rootView.findViewById(R.id.iv_media_view);
        llVideo = (LinearLayout) rootView.findViewById(R.id.ll_media_view_video);
        videoView = (VideoView) rootView.findViewById(R.id.videoView_media_view);

        progress.setVisibility(View.VISIBLE);
        ivPhoto.setVisibility(View.VISIBLE);
        llVideo.setVisibility(View.GONE);

    }

    private void populateMedia() {
        Glide.with(App.getInstance().getApplicationContext())
                .load(mediaBean.getImageURL())
                .apply(new RequestOptions()
                        .error(R.drawable.logo_splash)
                        .fallback(R.drawable.logo_splash)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerInside())
                .into(ivPhoto);
        if (mediaBean.getType() == TYPE_PHOTO) {
            progress.setVisibility(View.VISIBLE);
            ivPhoto.setVisibility(View.VISIBLE);
            llVideo.setVisibility(View.GONE);
        } else {
            progress.setVisibility(View.VISIBLE);
            ivPhoto.setVisibility(View.GONE);
            llVideo.setVisibility(View.VISIBLE);

            videoView.setVideoURI(Uri.parse(mediaBean.getVideoURL()));

//        mediaController.setAnchorView(videoView);

//        videoView.setMediaController(mediaController);

            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {
                    /*videoView.start();*/
                    startTimer();
                }
            });

            MediaController mediaController = new MediaController(getActivity());
            videoView.setMediaController(mediaController);

        }

    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (videoView != null) {
                            if (videoView.isPlaying()) {
                                Toast.makeText(App.getInstance().getApplicationContext(),
                                        getDuration(videoView.getCurrentPosition()),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        }, 0, 100);
    }

    private String getDuration(int duration) {
        String time = "00:00";

        duration /= 1000;
        time = String.valueOf(duration % 60);
        if (time.length() == 1)
            time = "0" + time;
        if (duration / 60 > 0) {
            duration /= 60;
            String temp = String.valueOf(duration % 60);
            if (temp.length() == 1)
                temp = "0" + temp;
            time = temp + ":" + time;
            if (duration / 60 > 0) {
                time = String.valueOf(duration / 60) + ":" + time;
            }
        } else {
            time = "00:" + time;
        }

        return time;
    }

    @NonNull
    private MediaController.MediaPlayerControl setMediaPlayerControl() {
        return new MediaController.MediaPlayerControl() {
            @Override
            public void start() {
                videoView.start();
            }

            @Override
            public void pause() {
                if (videoView.isPlaying())
                    videoView.pause();
            }

            @Override
            public int getDuration() {
                return videoView.getDuration();
            }

            @Override
            public int getCurrentPosition() {
                return videoView.getCurrentPosition();
            }

            @Override
            public void seekTo(int i) {

            }

            @Override
            public boolean isPlaying() {
                return videoView.isPlaying();
            }

            @Override
            public int getBufferPercentage() {
                return videoView.getBufferPercentage();
            }

            @Override
            public boolean canPause() {
                return false;
            }

            @Override
            public boolean canSeekBackward() {
                return false;
            }

            @Override
            public boolean canSeekForward() {
                return false;
            }

            @Override
            public int getAudioSessionId() {
                return 0;
            }
        };
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            setListener(activity);
        }
    }

    private void setListener(Context context) {
        if (getActivity() instanceof MediaViewFragmentListener) {
            mListener = (MediaViewFragmentListener) getActivity();
        } else if (getParentFragment() instanceof MediaViewFragmentListener) {
            mListener = (MediaViewFragmentListener) getParentFragment();
        } else if (context instanceof MediaViewFragmentListener) {
            mListener = (MediaViewFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MediaViewFragmentListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof MediaViewFragmentListener) {
            mListener = (MediaViewFragmentListener) getActivity();
        } else if (getParentFragment() instanceof MediaViewFragmentListener) {
            mListener = (MediaViewFragmentListener) getParentFragment();
        } else if (context instanceof MediaViewFragmentListener) {
            mListener = (MediaViewFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MediaViewFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface MediaViewFragmentListener {

    }
}
