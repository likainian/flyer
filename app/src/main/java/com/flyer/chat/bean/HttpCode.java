package com.flyer.chat.bean;

/**
 * Created by mike.li on 2018/6/1.
 */

public class HttpCode {
    private String response;
    private String responseCode;
    private ErrorBean error;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public ErrorBean getError() {
        return error;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }

    public static class ErrorBean {
        /**
         * message : 您的账号已在其它地方登录，请注意您的账号安全！
         */

        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
    public boolean isOk(){
        return "0".equals(responseCode);
    }
    public boolean isNeedLogin(){
        return "4".equals(responseCode);
    }
}
