package com.example.supportportal.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss")
    private Date timeStamp;
    private int statusCode;
    private HttpStatus httpStatus;
    private String reason;
    private String message;
}
