package com.elfiky.data.network.entities

import com.elfiky.data.network.Network
import com.elfiky.domain.entities.Biography
import com.elfiky.domain.entities.BirthDay
import com.elfiky.domain.entities.DeathDay
import com.elfiky.domain.entities.Department
import com.elfiky.domain.entities.Gender
import com.elfiky.domain.entities.ImageThumbnail
import com.elfiky.domain.entities.PersonDetails
import com.elfiky.domain.entities.PersonId
import com.elfiky.domain.entities.PersonName
import com.elfiky.domain.entities.Place
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class PersonApi(
    val id: Int,
    val name: String,
    val also_known_as: List<String>,
    val gender: Int,
    val known_for_department: String,
    val biography: String,
    val profile_path: String?,
    val place_of_birth: String?,
    val birthday: String?,
    val deathday: String?,
)

internal fun PersonApi.toPersonDetails(width: Int = 500): PersonDetails {
    val gender = when(gender) {
        1 -> Gender.Female
        2 -> Gender.Male
        else -> Gender.Unknown
    }
    return PersonDetails(
        id = PersonId(id),
        name = PersonName(name),
        otherKnownNames = also_known_as.map { PersonName(it) },
        thumbnail = ImageThumbnail("${Network.imagesBaseUrl}w$width$profile_path"),
        gender = gender,
        department = Department(known_for_department),
        biography = Biography(biography),
        placeOfBirth = if (place_of_birth != null) Place(place_of_birth) else null,
        birthday = if (birthday != null) BirthDay(birthday) else null,
        deathDay = if (deathday != null) DeathDay(deathday) else null
    )
}