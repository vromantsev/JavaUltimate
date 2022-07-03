package com.bobocode.nasa;

import com.bobocode.nasa.service.ImageService;

public class DemoApp {
    public static void main(String[] args) {
        new ImageService().findPictureOfLargestSize();
    }
}
