package com.example.demo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Camera {
    @Getter
    Vector3 origin = new Vector3(0, 0.7, 0);
    final Vector3 direction = null;
}
