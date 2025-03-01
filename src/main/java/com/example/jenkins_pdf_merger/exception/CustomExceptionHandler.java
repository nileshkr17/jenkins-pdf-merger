package com.example.jenkins_pdf_merger.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(IOException.class)
  public ResponseEntity<String> handleIOException(IOException ex){
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body("An error occurred while processing the PDF files"+ ex.getMessage());
  }
}
