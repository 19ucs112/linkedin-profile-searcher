package com.profile.searcher.model.response;

public class SuccessResponseVO<T> {
    private final String message;
    private final T data;

    private SuccessResponseVO(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public static <T> SuccessResponseVO<T> of(String message, T data) {
        return new SuccessResponseVO<T>(message, data);
    }
}
