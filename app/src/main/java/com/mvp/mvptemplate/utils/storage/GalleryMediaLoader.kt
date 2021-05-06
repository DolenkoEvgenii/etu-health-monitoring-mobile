package com.mvp.mvptemplate.utils.storage

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import io.reactivex.Observable
import java.io.File


class GalleryMediaLoader(val context: Context) {
    fun getFolders(): List<Folder> {
        val videoFolders = getVideoFolders()
        val resultFolders = getImageFolders().toMutableList()

        videoFolders.forEach { folder ->
            val existingFolder = resultFolders.find { it.folderName == folder.folderName }
            if (existingFolder == null) {
                resultFolders.add(folder)
            }
        }

        return resultFolders
    }

    fun getFolderMedia(bucketPath: String): Observable<Media> {
        val videosObservable = getFolderVideos(bucketPath)
        val imagesObservable = getFolderImages(bucketPath)

        return Observable.merge(imagesObservable, videosObservable)
    }

    private fun getVideoFolders(): List<Folder> {
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATA)
        val orderBy = MediaStore.Video.Media.DATE_ADDED + " DESC"

        return getMediaFolders(uri, projection, orderBy)
    }

    private fun getImageFolders(): List<Folder> {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA)
        val orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC"

        return getMediaFolders(uri, projection, orderBy)
    }

    fun getFolderVideos(bucketPath: String): Observable<Media> {
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val selection = MediaStore.Video.Media.BUCKET_DISPLAY_NAME + " =?"
        val orderBy = MediaStore.Video.Media.DATE_ADDED + " DESC"

        return getAllFolderMedia(uri, projection, orderBy, bucketPath, selection)
    }

    fun getFolderImages(bucketPath: String): Observable<Media> {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " =?"
        val orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC"

        return getAllFolderMedia(uri, projection, orderBy, bucketPath, selection)
    }

    private fun getMediaFolders(uri: Uri, projection: Array<String>, orderBy: String): List<Folder> {
        val buckets = ArrayList<Folder>()

        val cursor = context.contentResolver.query(uri, projection, null, null, orderBy)
        if (cursor != null) {
            var file: File
            while (cursor.moveToNext()) {
                val bucketPath = cursor.getString(cursor.getColumnIndex(projection[0]))
                val firstMedia = cursor.getString(cursor.getColumnIndex(projection[1]))
                file = File(firstMedia)

                val folder = Folder(bucketPath, firstMedia)
                if (file.exists() && !buckets.contains(folder)) {
                    buckets.add(folder)
                }
            }
            cursor.close()
        }
        return buckets
    }

    private fun getAllFolderMedia(uri: Uri, projection: Array<String>, orderBy: String, bucketPath: String, selection: String): Observable<Media> {
        return Observable.create { emitter ->
            val cursor = context.contentResolver.query(uri, projection, selection, arrayOf(bucketPath), orderBy)

            if (cursor != null) {
                var file: File
                while (cursor.moveToNext()) {
                    val path = cursor.getString(cursor.getColumnIndex(projection[0]))
                    file = File(path)
                    if (file.exists()) {
                        emitter.onNext(Media(bucketPath, path))
                    }

                    if (emitter.isDisposed) {
                        cursor.close()
                        return@create
                    }
                }
                emitter.onComplete()
                cursor.close()
            }
        }
    }
}
