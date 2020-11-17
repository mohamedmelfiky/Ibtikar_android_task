package com.elfiky.data.network.entities

import com.elfiky.data.network.Network
import com.elfiky.domain.entities.ImageOriginal
import com.elfiky.domain.entities.ImageThumbnail
import com.elfiky.domain.entities.ProfileImage
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ProfileApi(
    val file_path: String,
)

@JsonClass(generateAdapter = true)
internal data class PersonImagesApi(
    val id: Int,
    val profiles: List<ProfileApi>
)

internal fun ProfileApi.toProfileImage(width: Int = 500): ProfileImage {
    return ProfileImage(
        thumbnail = ImageThumbnail("${Network.imagesBaseUrl}w$width$file_path"),
        original = ImageOriginal("${Network.imagesBaseUrl}original$file_path"),
    )
}