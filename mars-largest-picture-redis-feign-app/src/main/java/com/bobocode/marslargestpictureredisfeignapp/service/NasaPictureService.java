package com.bobocode.marslargestpictureredisfeignapp.service;

public interface NasaPictureService {

    byte[] getLargestPicture(final int sol, final String camera);

}
