package com.elfiky.domain.entities

inline class PersonId(val value: Int)
inline class PersonName(val value: String)
inline class Department(val name: String)
inline class Biography(val value: String)
inline class ImageThumbnail(val url: String)
inline class ImageOriginal(val url: String)
inline class Place(val name: String)
inline class BirthDay(val date: String)
inline class DeathDay(val date: String)

data class PersonDetails(
    val id: PersonId,
    val name: PersonName,
    val otherKnownNames: List<PersonName>,
    val thumbnail: ImageThumbnail,
    val gender: Gender,
    val department: Department,
    val biography: Biography,
    val placeOfBirth: Place?,
    val birthday: BirthDay?,
    val deathDay: DeathDay?,
)

data class ProfileImage(
    val thumbnail: ImageThumbnail,
    val original: ImageOriginal,
)

data class Person(
    val details: PersonDetails,
    val images: List<ProfileImage>
)