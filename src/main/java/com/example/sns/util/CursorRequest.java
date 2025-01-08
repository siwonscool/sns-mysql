package com.example.sns.util;

public record CursorRequest(
        Long key,
        int size
) {
    public static final Long NONE_KEY = -1L;

    public boolean hasKey() {
        return null != key;
    }

    public CursorRequest next(Long key){
        return new CursorRequest(key, size);
    }
}
