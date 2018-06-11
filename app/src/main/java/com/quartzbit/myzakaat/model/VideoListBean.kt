package `com`.quartzbit.myzakaat.model

import `com`.quartzbit.myzakaat.util.AppConstants

/**
 * Created by Jemsheer K D on 05 September, 2017.
 * Package `com`.quartzbit.myzakaat.model
 * Project Dearest
 */
class VideoListBean : BaseBean() {

    var videos: ArrayList<VideoBean> = ArrayList()

    fun getMediaListBean(): MediaListBean {
        val mediaListBean = MediaListBean()


        for (videoBean: VideoBean in videos) {
            val mediaBean = MediaBean()
            mediaBean.id = videoBean.id
            mediaBean.postID = videoBean.postID
            mediaBean.time = videoBean.time
//                    mediaBean.setImageThumbURL(App.getImagePath(getPhotoPath(snapshot, "snapshotURL")));
            mediaBean.imageThumbURL = videoBean.snapshotURL
//                    mediaBean.setImageURL(App.getImagePath(getPhotoPath(snapshot, "url")));
            mediaBean.imageURL = videoBean.snapshotURL
            mediaBean.videoURL = videoBean.url
            mediaBean.type = AppConstants.MEDIA_TYPE_VIDEO
            mediaListBean.mediaList.add(mediaBean)
        }

        return mediaListBean
    }
}