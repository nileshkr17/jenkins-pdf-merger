package com.example.jenkins_pdf_merger.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class PdfMergerServiceTest {

  //private final PdfMergerService pdfMergerService = new PdfMergerService();
  @InjectMocks
  private PdfMergerService pdfMergerService;
  private AutoCloseable mocks;
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private MultipartFile createValidPdf(String name) throws IOException {
    Path tempFile = Files.createTempFile(name, ".pdf");
    try (PDDocument doc = new PDDocument()) {
      doc.addPage(new PDPage());
      doc.save(tempFile.toFile());
    }
    return new MockMultipartFile(name, name + ".pdf", "application/pdf", Files.newInputStream(tempFile));
  }

  @Test
  public void givenMultiplePdfFiles_whenMergePdfs_thenReturnMergedPdf() throws IOException {
    MultipartFile file1 = createValidPdf("file1");
    MultipartFile file2 = createValidPdf("file2");
    MultipartFile[] files = {file1, file2};

    byte[] result = pdfMergerService.mergePdfs(files);

    assertNotNull(result);
    assertTrue(result.length > 0);
  }

  @Test
  public void givenEmptyFileArray_whenMergePdfs_thenReturnEmptyByteArray() throws IOException {
    MultipartFile[] files = {};

    byte[] result = pdfMergerService.mergePdfs(files);

    assertNotNull(result);
    assertEquals(0, result.length);
  }

  @Test
  public void givenNullFileArray_whenMergePdfs_thenReturnEmptyByteArray() throws IOException {
    byte[] result = pdfMergerService.mergePdfs(null);
    assertNotNull(result);
    assertEquals(0, result.length);
  }


  @Test
  public void givenFileWithIOException_whenMergePdfs_thenThrowIOException() throws IOException {
    MultipartFile file = mock(MultipartFile.class);
    when(file.getInputStream()).thenThrow(new IOException("Test IOException"));  // Simulate IOException
    MultipartFile[] files = {file};

    Exception exception = assertThrows(IOException.class, () -> pdfMergerService.mergePdfs(files));

    assertEquals("Failed to merge PDFs.", exception.getMessage());  // Verify exception message
  }


}