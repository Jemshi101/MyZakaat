package `com`.quartzbit.myzakaat.model

import `com`.quartzbit.myzakaat.util.AppConstants

/**
 * Created by Jemsheer K D on 05 September, 2017.
 * Package `com`.quartzbit.myzakaat.model
 * Project Dearest
 */
class PhotoListBean : BaseBean() {

    var photos: ArrayList<PhotoBean> = ArrayList()

    fun getMediaListBean(): MediaListBean {
        val mediaListBean = MediaListBean()


        for (photoBean: PhotoBean in photos) {
            val mediaBean = MediaBean()
            mediaBean.id = photoBean.id
            mediaBean.postID = photoBean.postID
            mediaBean.time = photoBean.time
//                    mediaBean.setImageThumbURL(App.getImagePath(getPhotoPath(snapshot, "snapshotURL")));
            mediaBean.imageThumbURL = photoBean.url
//                    mediaBean.setImageURL(App.getImagePath(getPhotoPath(snapshot, "url")));
            mediaBean.imageURL = photoBean.url
            mediaBean.videoURL = photoBean.url
            mediaBean.type = AppConstants.MEDIA_TYPE_PHOTO
            mediaListBean.mediaList.add(mediaBean)
        }

        return mediaListBean
    }
}