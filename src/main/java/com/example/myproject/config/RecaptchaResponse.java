package com.example.myproject.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.lang.annotation.Pointcut;

@Setter
@Getter
public class RecaptchaResponse {


    private boolean success;
    private String challenge_ts;
    private String hostname;
    @JsonProperty("error-codes")
    private String[] error_codes;
}
