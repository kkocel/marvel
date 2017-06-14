package com.example.kkocel.marvel.network.model;

class ImageModel {

    lateinit var path: String
    lateinit var extension: String

    val pathWithExtension: String
        get() = path + extension
}