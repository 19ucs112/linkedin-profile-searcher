package com.profile.searcher.model.response;

import lombok.Data;

/***
 * This is the generic response wrapper class used to represent a successful response in the API. This class holds a
 * success message and an optional data payload of type.
 * @param <T> The type of the data being returned in the response.
 */

@Data
public class SuccessResponseVO<T> {

    /***
     * The success message to be returned to client.
     */
    private final String message;

    /***
     * The actual data or result of type T being returned to the client.
     */
    private final T data;

    /***
     * This static method to create a new instance.
     * @param message This is the success message.
     * @param data The data ti be returned.
     * @return SuccessResponseVO containing the message and data.
     * @param <T>
     */
    public static <T> SuccessResponseVO<T> of(String message, T data) {
        return new SuccessResponseVO<T>(message, data);
    }
}
