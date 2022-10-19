package com.bobocode.marslargestpicturefeignapp.service;

public interface NasaPictureService {

    byte[] getLargestPicture(final int sol, final String camera);

}
