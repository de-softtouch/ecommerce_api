package com.api.ecommerceweb.reponse;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class BaseResponseBody {

    private boolean isSuccess;
    private Integer statusCode;
    private String statusText;
    private String message;
    private String type;
    private Object data;

    public BaseResponseBody() {

    }

    public static BaseResponseBody resourceExist(String message) {
        BaseResponseBody baseResponseBody = new BaseResponseBody();
        baseResponseBody.setSuccess(false);
        baseResponseBody.setStatusCode(422);
        baseResponseBody.setStatusText(HttpStatus.UNPROCESSABLE_ENTITY.name());
        baseResponseBody.setMessage(message);
        return baseResponseBody;
    }

    public static BaseResponseBody badRequest(String message) {
        BaseResponseBody baseResponseBody = new BaseResponseBody();
        baseResponseBody.setSuccess(false);
        baseResponseBody.setStatusCode(400);
        baseResponseBody.setMessage(message);
        baseResponseBody.setStatusText(HttpStatus.BAD_REQUEST.name());
        return baseResponseBody;
    }

    public static BaseResponseBody success(Object data, String message, String type) {
        BaseResponseBody baseResponseBody = new BaseResponseBody();
        baseResponseBody.setSuccess(true);
        baseResponseBody.setStatusCode(200);
        baseResponseBody.setStatusText(HttpStatus.OK.name());
        baseResponseBody.setType(type);
        baseResponseBody.setData(data);
        baseResponseBody.setMessage(message);
        return baseResponseBody;
    }

    public static BaseResponseBody successMessage(String message) {
        BaseResponseBody baseResponseBody = new BaseResponseBody();
        baseResponseBody.setSuccess(true);
        baseResponseBody.setStatusCode(200);
        baseResponseBody.setStatusText(HttpStatus.OK.name());
        baseResponseBody.setMessage(message);
        return baseResponseBody;
    }

    public static BaseResponseBody notFound(String message) {
        BaseResponseBody baseResponseBody = new BaseResponseBody();
        baseResponseBody.setSuccess(true);
        baseResponseBody.setStatusCode(404);
        baseResponseBody.setStatusText(HttpStatus.NOT_FOUND.name());
        baseResponseBody.setMessage(message);
        return baseResponseBody;
    }
}
