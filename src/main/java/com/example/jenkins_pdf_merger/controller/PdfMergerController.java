package com.example.jenkins_pdf_merger.controller;

import java.io.IOException;

import com.example.jenkins_pdf_merger.service.PdfMergerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/pdf")
public class PdfMergerController {
  private final PdfMergerService pdfMergerService;
  public PdfMergerController(PdfMergerService pdfMergerService) {
    this.pdfMergerService = pdfMergerService;
  }

  @PostMapping("/merge")
  public ResponseEntity<byte[]> mergePdfs(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) throws IOException {

    System.out.println("Content-Type: " + request.getContentType());

    if(files.length < 2 || files == null){
      System.out.println("No files received or less than two files uploaded.");
      return ResponseEntity.badRequest().body("At least two files are required to merge".getBytes());
    }

    System.out.println("Received " + files.length + " files for merging.");
    byte[] mergedPdf = pdfMergerService.mergePdfs(files);

    return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=merged.pdf")
      .contentType(MediaType.APPLICATION_PDF)
      .body(mergedPdf);
  }

  @GetMapping("/test")
  public String test() {
    return "PDF Merger is working!";
  }
}
