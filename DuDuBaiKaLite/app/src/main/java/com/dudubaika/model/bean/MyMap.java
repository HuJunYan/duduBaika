package com.dudubaika.model.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by zhangchi on 2016/7/6.
 */
public class MyMap implements Serializable {
    private Map<String, byte[]> images;

    public Map<String, byte[]> getImages() {
        return images;
    }

    public void setImages(Map<String, byte[]> images) {
        this.images = images;
    }
}
