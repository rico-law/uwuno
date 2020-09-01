package com.learning.uwuno.errors;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class internalServerError extends RuntimeException {}