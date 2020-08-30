package com.learning.uwuno.errors;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

// We can make these return specific html files and custom messages in the future
@ResponseStatus(HttpStatus.NOT_FOUND)
public class errorNotFound extends RuntimeException {}
