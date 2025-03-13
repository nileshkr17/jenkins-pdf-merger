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


  @GetMapping("/home")
  public String home() {
    return "<html>" +
      "<head>" +
      "<title>PDF Merger|more |Haha new changes</title>" +
      "<link rel='icon' type='image/png' href='https://cdn-icons-png.flaticon.com/512/337/337946.png'>" +
      "<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css'>" +
      "<style>" +
      "body { font-family: Arial, sans-serif; margin: 50px; text-align: center; background-color: #f4f4f4; }" +
      "h1 { color: #333; }" +
      ".container { max-width: 500px; margin: auto; background: white; padding: 20px; border-radius: 10px; box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); }" +
      "form { padding: 20px; border-radius: 5px; }" +
      "input[type='file'] { margin: 10px 0; display: block; width: 100%; }" +
      "input[type='submit'] { background-color: #4CAF50; color: white; border: none; padding: 10px 20px; cursor: pointer; width: 100%; font-size: 16px; }" +
      "input[type='submit']:hover { background-color: #45a049; }" +
      ".footer { margin-top: 20px; padding: 10px; font-size: 14px; color: #777; }" +
      ".upload-icon { font-size: 50px; color: #4CAF50; margin-bottom: 10px; }" +
      "</style>" +
      "</head>" +
      "<body>" +
      "<div class='container'>" +
      "<h1><i class='fa-solid fa-file-pdf'></i> PDF Merger</h1>" +
      "<i class='fa-solid fa-upload upload-icon'></i>" +
      "<form method='POST' action='/api/pdf/merge' enctype='multipart/form-data'>" +
      "<input type='file' name='files' multiple required>" +
      "<br>" +
      "<input type='submit' value='Merge PDFs'>" +
      "</form>" +
      "</div>" +
      "<div class='footer'>© 2025 nileshkr17 :) | Built with ❤️ </div>" +
      "</body>" +
      "</html>";
  }




}
