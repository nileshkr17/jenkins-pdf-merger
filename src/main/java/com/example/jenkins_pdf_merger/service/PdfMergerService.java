package com.example.jenkins_pdf_merger.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PdfMergerService {
  public byte[] mergePdfs(MultipartFile[] files) throws IOException {

    if (files == null || files.length == 0) {
      System.out.println("No files provided for merging. Returning empty byte array.");
      return new byte[0];
    }

    ByteArrayOutputStream mergedOutputStream = new ByteArrayOutputStream();
    List<PDDocument> documents = new ArrayList<>();
    PDDocument mergedDocument = new PDDocument();


    try {
      System.out.println("Starting PDF merge process...");
      for (MultipartFile file : files) {
        System.out.println("Merging file: " + file.getOriginalFilename());
        PDDocument document = PDDocument.load(file.getInputStream());
        documents.add(document);
      }
      // Merge all loaded PDFs
      PDFMergerUtility pdfMerger = new PDFMergerUtility();
      pdfMerger.setDestinationStream(mergedOutputStream);

      for (PDDocument doc : documents) {
        pdfMerger.appendDocument(mergedDocument, doc);
      }

      // Save merged document to output stream
      mergedDocument.save(mergedOutputStream);
      System.out.println("PDF merge successful!");

      System.out.println("PDF merge successful!");
    } catch (Exception e) {
      System.out.println("Error while merging PDFs: " + e.getMessage());
      e.printStackTrace();
      throw new IOException("Failed to merge PDFs.", e);
    }finally {
      for (PDDocument doc : documents) {
        doc.close();
      }
      mergedDocument.close();
    }

    return mergedOutputStream.toByteArray();
  }
}