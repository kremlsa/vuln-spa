package vulnspa.model;

public class ErrorResponse {
    private String error;
    private String message;
    private String path;

    public ErrorResponse() {}

    public ErrorResponse(String error, String message, String path) {
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}