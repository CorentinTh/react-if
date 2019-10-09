public enum HTTPStatusCode {
    OK(200, "OK"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    UNKNOWN_ERROR(500, "Unknown Error");

    private int code;
    private String message;

    HTTPStatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
