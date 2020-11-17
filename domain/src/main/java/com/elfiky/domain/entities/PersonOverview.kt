package com.elfiky.domain.entities

inline class KnownFor(val value: String)

data class PersonOverview(
    val id: PersonId,
    val name: PersonName,
    val image: ImageThumbnail,
    val knownFor: KnownFor,
)