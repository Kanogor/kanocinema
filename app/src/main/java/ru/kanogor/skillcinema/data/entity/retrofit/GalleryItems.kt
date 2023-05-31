package ru.kanogor.skillcinema.data.entity.retrofit

interface GalleryItems {
    val total: Int
    val totalPages: Int
    val items: List<GalleryPicture>
}

interface GalleryPicture {
    val imageUrl: String
    val previewUrl: String
}
