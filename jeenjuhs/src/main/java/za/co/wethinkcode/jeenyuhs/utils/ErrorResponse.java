package za.co.wethinkcode.jeenyuhs.utils;

import java.util.Map;

public class ErrorResponse {
    public String title;
    public int status;
    public String type;
    public Map<String, String> details;

    public ErrorResponse() {
    }

    public ErrorResponse(String title, int status, String type, Map<String, String> details) {
        this.title = title;
        this.status = status;
        this.type = type;
        this.details = details;
    }
}
