package com.diegodrp.gallery.data

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.diegodrp.gallery.R
import com.diegodrp.gallery.exception.ImageLoadException
import com.diegodrp.gallery.exception.VideoLoadException
import com.diegodrp.gallery.extensions.isQPlus
import com.diegodrp.gallery.helpers.Resource
import com.diegodrp.gallery.helpers.StringResolver
import com.diegodrp.gallery.model.Album
import com.diegodrp.gallery.model.Image
import com.diegodrp.gallery.model.Video
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MediaRepositoryImpl(
    private val context: Context,
    private val stringResolver: StringResolver
) : MediaRepository {
    override fun loadImages(): Map<String, List<Image>> {
        val images = mutableMapOf<String, MutableList<Image>>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATE_MODIFIED
        )
        val imageUri = getImageUri()

        val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"

        val cursor = context.contentResolver.query(imageUri, projection, null, null, sortOrder)

        if (cursor == null) {
            throw ImageLoadException()
        } else {
            cursor.use {
                val idColumn = cursor.getColumnIndexOrThrow(projection[0])
                val bucketNameColumn = cursor.getColumnIndexOrThrow(projection[1])
                val nameColumn = cursor.getColumnIndexOrThrow(projection[2])
                val sizeColumn = cursor.getColumnIndexOrThrow(projection[3])
                val dateModifiedColumn = cursor.getColumnIndexOrThrow(projection[4])

                while (cursor.moveToNext()) {
                    val id = cursor.getLongOrNull(idColumn)
                    val albumName = cursor.getStringOrNull(bucketNameColumn)
                        ?: stringResolver.getString(R.string.unknown_album)
                    val name = cursor.getStringOrNull(nameColumn)
                    val size = cursor.getIntOrNull(sizeColumn)
                    val dateModified = cursor.getIntOrNull(dateModifiedColumn)

                    if (id == null || name == null || size == null) continue

                    val uri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    images.getOrPut(albumName) { mutableListOf() }
                        .add(Image(id, name, size, dateModified ?: 0, uri))
                }

            }
        }
        return images
    }

    override fun loadVideos(): Map<String, List<Video>> {
        val videos = mutableMapOf<String, MutableList<Video>>()
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.DATE_MODIFIED
        )
        val videoUri = getVideoUri()

        val sortOrder = "${MediaStore.Video.Media.DATE_MODIFIED} DESC"

        val cursor = context.contentResolver.query(videoUri, projection, null, null, sortOrder)

        if (cursor == null) {
            throw VideoLoadException()
        } else {
            cursor.use {
                val idColumn = cursor.getColumnIndexOrThrow(projection[0])
                val bucketNameColumn = cursor.getColumnIndexOrThrow(projection[1])
                val nameColumn = cursor.getColumnIndexOrThrow(projection[2])
                val sizeColumn = cursor.getColumnIndexOrThrow(projection[3])
                val durationColumn = cursor.getColumnIndexOrThrow(projection[4])
                val dateModifiedColumn = cursor.getColumnIndexOrThrow(projection[5])

                while (cursor.moveToNext()) {
                    val id = cursor.getLongOrNull(idColumn)
                    val albumName = cursor.getStringOrNull(bucketNameColumn)
                        ?: stringResolver.getString(R.string.unknown_album)
                    val name = cursor.getStringOrNull(nameColumn)
                    val size = cursor.getIntOrNull(sizeColumn)
                    val duration = cursor.getIntOrNull(durationColumn)
                    val dateModified = cursor.getIntOrNull(dateModifiedColumn)

                    if (id == null || name == null || size == null || duration == null) continue

                    val uri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    videos.getOrPut(albumName) { mutableListOf() }
                        .add(Video(id, name, size, duration, dateModified ?: 0, uri))
                }
            }
        }
        return videos
    }


    override fun loadAlbums(): Flow<Resource<List<Album>>> {
        return flow {
            val albums = mutableListOf<Album>()

            emit(Resource.Loading(isLoading = true))

            val images = try {
                loadImages()
            } catch (e: Exception) {
                null
            }

            val videos = try {
                loadVideos()
            } catch (e: Exception) {
                null
            }

            images?.let {
                for (imageAlbum in images) {
                    // Create album if not exist
                    var album = albums.find { it.name == imageAlbum.key }
                    if (album == null) {
                        val newAlbum = Album(
                            name = imageAlbum.key,
                            images = mutableListOf(),
                            videos = mutableListOf()
                        )
                        albums += newAlbum
                        album = newAlbum
                    }

                    for (image in imageAlbum.value) {
                        (album.images as MutableList<Image>).add(image)
                    }
                }
            }

            videos?.let {
                for (videoAlbum in videos) {
                    // Create album if not exist
                    var album = albums.find { it.name == videoAlbum.key }
                    if (album == null) {
                        val newAlbum = Album(
                            name = videoAlbum.key,
                            images = mutableListOf(),
                            videos = mutableListOf()
                        )
                        albums += newAlbum
                        album = newAlbum
                    }

                    for (video in videoAlbum.value) {
                        (album!!.videos as MutableList<Video>).add(video)
                    }
                }
            }

            if (images != null && videos != null) {
                emit(Resource.Loading(isLoading = false))
                emit(Resource.Success(data = albums))
                return@flow
            }

            if (images == null) {
                emit(Resource.Loading(isLoading = false))
                emit(
                    Resource.Error(
                        data = albums,
                        error = stringResolver.getString(R.string.failed_loading_images)
                    )
                )
            }

            if (videos == null) {
                emit(Resource.Loading(isLoading = false))
                emit(
                    Resource.Error(
                        data = albums,
                        error = stringResolver.getString(R.string.failed_loading_videos)
                    )
                )
            }

        }
    }

    private fun getImageUri(): Uri {
        return if (isQPlus()) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
    }

    private fun getVideoUri(): Uri {
        return if (isQPlus()) {
            MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        }
    }
}