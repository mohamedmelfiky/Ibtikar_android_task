package com.elfiky.data.network.entities

import com.elfiky.data.network.Network
import com.elfiky.domain.entities.ImageThumbnail
import com.elfiky.domain.entities.KnownFor
import com.elfiky.domain.entities.PersonId
import com.elfiky.domain.entities.PersonName
import com.elfiky.domain.entities.PersonOverview
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KnownForApi(
    val id: Int,
    val title: String?,
    val original_title: String?,
    val name: String?,
    val overview: String,
    val media_type: String
)

@JsonClass(generateAdapter = true)
data class PersonOverviewApi(
    val id: Int,
    val name: String,
    val gender: Int,
    val popularity: Float,
    val profile_path: String,
    val known_for_department: String,
    val known_for: List<KnownForApi>
)

fun PersonOverviewApi.toPersonOverview(width: Int = 500): PersonOverview {
    val knownFor = known_for.joinToString(",") {
        it.original_title ?: (it.title ?: (it.name ?: ""))
    }
    return PersonOverview(
        id = PersonId(id),
        name = PersonName(name),
        image = ImageThumbnail("${Network.imagesBaseUrl}w$width$profile_path"),
        knownFor = KnownFor(knownFor)
    )
}