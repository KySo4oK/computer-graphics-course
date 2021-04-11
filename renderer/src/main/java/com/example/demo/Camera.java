package com.example.demo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor
public class Camera {
    @Getter
    Vector3 origin = new Vector3(0,0,5);
    final Vector3 direction = null;
}
