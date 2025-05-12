
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
      "<title>PDF Merger | Modern UI</title>" +
      "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
      "<link rel='icon' type='image/png' href='https://cdn-icons-png.flaticon.com/512/337/337946.png'>" +
      "<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css'>" +
      "<style>" +
      "* { box-sizing: border-box; margin: 0; padding: 0; }" +
      "body { font-family: 'Segoe UI', Roboto, Arial, sans-serif; line-height: 1.6; color: #333; background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%); min-height: 100vh; display: flex; flex-direction: column; }" +
      "main { flex: 1; display: flex; align-items: center; justify-content: center; padding: 2rem; }" +
      ".card { background: white; max-width: 600px; width: 100%; border-radius: 12px; box-shadow: 0 10px 30px rgba(0,0,0,0.1); overflow: hidden; }" +
      ".card-header { background: #6366f1; padding: 1.5rem; color: white; text-align: center; }" +
      ".card-body { padding: 2rem; }" +
      ".upload-area { border: 2px dashed #d1d5db; border-radius: 8px; padding: 2rem; text-align: center; margin-bottom: 1.5rem; transition: all 0.3s ease; position: relative; }" +
      ".upload-area.drag-over { border-color: #6366f1; background-color: rgba(99, 102, 241, 0.05); }" +
      ".upload-icon { font-size: 48px; color: #6366f1; margin-bottom: 1rem; display: block; }" +
      ".file-input { position: absolute; top: 0; left: 0; width: 100%; height: 100%; opacity: 0; cursor: pointer; z-index: 10; }" +
      ".upload-text { margin-bottom: 0.5rem; font-weight: 500; }" +
      ".upload-hint { color: #6b7280; font-size: 0.875rem; margin-bottom: 1rem; }" +
      ".files-container { margin-bottom: 1.5rem; }" +
      ".file-list { max-height: 200px; overflow-y: auto; border: 1px solid #e5e7eb; border-radius: 6px; margin-top: 1rem; }" +
      ".file-list:empty { display: none; }" +
      ".file-item { display: flex; align-items: center; justify-content: space-between; padding: 0.75rem 1rem; border-bottom: 1px solid #e5e7eb; }" +
      ".file-item:last-child { border-bottom: none; }" +
      ".file-name { display: flex; align-items: center; font-size: 0.875rem; font-weight: 500; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }" +
      ".file-icon { color: #f87171; margin-right: 0.5rem; }" +
      ".file-remove { background: none; border: none; color: #9ca3af; cursor: pointer; font-size: 1rem; transition: color 0.2s; }" +
      ".file-remove:hover { color: #f87171; }" +
      ".file-count { font-size: 0.875rem; color: #6b7280; margin-bottom: 0.5rem; }" +
      ".btn { display: inline-block; border: none; border-radius: 6px; padding: 0.75rem 1.5rem; font-weight: 600; cursor: pointer; transition: all 0.3s ease; text-align: center; width: 100%; font-size: 1rem; }" +
      ".btn-primary { background: #6366f1; color: white; }" +
      ".btn-primary:hover { background: #4f46e5; }" +
      ".btn-primary:disabled { background: #a5b4fc; cursor: not-allowed; }" +
      ".error-message { background-color: #fee2e2; border: 1px solid #fecaca; color: #ef4444; padding: 0.75rem; border-radius: 6px; margin-bottom: 1.5rem; display: flex; align-items: center; }" +
      ".error-icon { margin-right: 0.5rem; flex-shrink: 0; }" +
      ".loading { display: none; text-align: center; margin-bottom: 1.5rem; }" +
      ".loading.active { display: block; }" +
      ".progress-container { height: 6px; background-color: #e5e7eb; border-radius: 3px; overflow: hidden; margin-bottom: 0.5rem; }" +
      ".progress-bar { height: 100%; background-color: #6366f1; width: 0%; transition: width 0.3s ease; }" +
      ".loading-text { font-size: 0.875rem; color: #6b7280; }" +
      ".footer { background: white; text-align: center; padding: 1.5rem; border-top: 1px solid #e5e7eb; color: #6b7280; font-size: 0.875rem; }" +
      ".footer a { color: #6366f1; text-decoration: none; margin: 0 0.5rem; }" +
      ".footer a:hover { text-decoration: underline; }" +
      "@media (max-width: 640px) { main { padding: 1rem; } .card-body { padding: 1.5rem; } }" +
      "</style>" +
      "</head>" +
      "<body>" +
      "<main>" +
      "  <div class='card'>" +
      "    <div class='card-header'>" +
      "      <h1><i class='fa-solid fa-file-pdf'></i> PDF Merger</h1>" +
      "      <p>Merge multiple PDF files into one document</p>" +
      "    </div>" +
      "    <div class='card-body'>" +
      "      <form id='pdfForm' method='POST' action='/api/pdf/merge' enctype='multipart/form-data'>" +
      "        <div class='upload-area' id='dropArea'>" +
      "          <input type='file' name='files' id='fileInput' multiple accept='.pdf' class='file-input' required>" +
      "          <i class='fa-solid fa-cloud-arrow-up upload-icon'></i>" +
      "          <h3 class='upload-text'>Drag & Drop PDF files here</h3>" +
      "          <p class='upload-hint'>Or click to browse your files</p>" +
      "          <button type='button' class='btn btn-primary' style='max-width: 200px;' onclick='document.getElementById(\"fileInput\").click()'>Select Files</button>" +
      "        </div>" +
      "        <div id='errorMessage' class='error-message' style='display: none;'>" +
      "          <i class='fa-solid fa-circle-exclamation error-icon'></i>" +
      "          <span id='errorText'></span>" +
      "        </div>" +
      "        <div class='files-container'>" +
      "          <div class='file-count' id='fileCount'>No files selected</div>" +
      "          <div class='file-list' id='fileList'></div>" +
      "        </div>" +
      "        <div id='loading' class='loading'>" +
      "          <div class='progress-container'>" +
      "            <div class='progress-bar' id='progressBar'></div>" +
      "          </div>" +
      "          <p class='loading-text'>Merging PDF files... Please wait</p>" +
      "        </div>" +
      "        <button type='submit' id='mergeBtn' class='btn btn-primary' disabled>" +
      "          <i class='fa-solid fa-layer-group'></i> Merge PDFs" +
      "        </button>" +
      "      </form>" +
      "    </div>" +
      "  </div>" +
      "</main>" +
      "<footer class='footer'>" +
      "  <p>© " + java.time.Year.now().getValue() + " PDF Merger | Built with <i class='fa-solid fa-heart' style='color: #ef4444;'></i> by Nileshkr17</p>" +
      "  <div>" +
      "    <a href='#'>Terms of Service</a> | " +
      "    <a href='#'>Privacy Policy</a>" +
      "  </div>" +
      "</footer>" +
      "<script>" +
      "  document.addEventListener('DOMContentLoaded', function() {" +
      "    const dropArea = document.getElementById('dropArea');" +
      "    const fileInput = document.getElementById('fileInput');" +
      "    const fileList = document.getElementById('fileList');" +
      "    const fileCount = document.getElementById('fileCount');" +
      "    const mergeBtn = document.getElementById('mergeBtn');" +
      "    const errorMessage = document.getElementById('errorMessage');" +
      "    const errorText = document.getElementById('errorText');" +
      "    const pdfForm = document.getElementById('pdfForm');" +
      "    const loading = document.getElementById('loading');" +
      "    const progressBar = document.getElementById('progressBar');" +
      "    let selectedFiles = [];" +
      "" +
      "    // Drag and drop events" +
      "    ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {" +
      "      dropArea.addEventListener(eventName, preventDefaults, false);" +
      "    });" +
      "" +
      "    function preventDefaults(e) {" +
      "      e.preventDefault();" +
      "      e.stopPropagation();" +
      "    }" +
      "" +
      "    ['dragenter', 'dragover'].forEach(eventName => {" +
      "      dropArea.addEventListener(eventName, highlight, false);" +
      "    });" +
      "" +
      "    ['dragleave', 'drop'].forEach(eventName => {" +
      "      dropArea.addEventListener(eventName, unhighlight, false);" +
      "    });" +
      "" +
      "    function highlight() {" +
      "      dropArea.classList.add('drag-over');" +
      "    }" +
      "" +
      "    function unhighlight() {" +
      "      dropArea.classList.remove('drag-over');" +
      "    }" +
      "" +
      "    dropArea.addEventListener('drop', handleDrop, false);" +
      "" +
      "    function handleDrop(e) {" +
      "      const dt = e.dataTransfer;" +
      "      const files = dt.files;" +
      "      handleFiles(files);" +
      "    }" +
      "" +
      "    fileInput.addEventListener('change', function() {" +
      "      handleFiles(this.files);" +
      "    });" +
      "" +
      "    function handleFiles(files) {" +
      "      hideError();" +
      "      const validFiles = [];" +
      "      const invalidFiles = [];" +
      "" +
      "      for (let i = 0; i < files.length; i++) {" +
      "        if (files[i].type === 'application/pdf') {" +
      "          validFiles.push(files[i]);" +
      "        } else {" +
      "          invalidFiles.push(files[i].name);" +
      "        }" +
      "      }" +
      "" +
      "      if (invalidFiles.length > 0) {" +
      "        showError('Only PDF files are allowed. Invalid files: ' + invalidFiles.join(', '));" +
      "      }" +
      "" +
      "      if (validFiles.length > 0) {" +
      "        // Add to selected files array" +
      "        for (let i = 0; i < validFiles.length; i++) {" +
      "          if (!fileExists(validFiles[i])) {" +
      "            selectedFiles.push(validFiles[i]);" +
      "          }" +
      "        }" +
      "        updateFileList();" +
      "      }" +
      "    }" +
      "" +
      "    function fileExists(file) {" +
      "      return selectedFiles.some(f => f.name === file.name && f.size === file.size);" +
      "    }" +
      "" +
      "    function updateFileList() {" +
      "      fileList.innerHTML = '';" +
      "      fileInput.files = createFileList(selectedFiles);" +
      "" +
      "      if (selectedFiles.length === 0) {" +
      "        fileCount.textContent = 'No files selected';" +
      "        mergeBtn.disabled = true;" +
      "        return;" +
      "      }" +
      "" +
      "      fileCount.textContent = selectedFiles.length + ' file(s) selected';" +
      "      mergeBtn.disabled = selectedFiles.length < 2;" +
      "" +
      "      if (selectedFiles.length < 2) {" +
      "        showError('Please select at least two PDF files to merge.');" +
      "      } else {" +
      "        hideError();" +
      "      }" +
      "" +
      "      selectedFiles.forEach((file, index) => {" +
      "        const fileItem = document.createElement('div');" +
      "        fileItem.className = 'file-item';" +
      "" +
      "        const fileName = document.createElement('div');" +
      "        fileName.className = 'file-name';" +
      "        fileName.innerHTML = `<i class=\"fa-solid fa-file-pdf file-icon\"></i>${file.name}`;" +
      "" +
      "        const fileRemove = document.createElement('button');" +
      "        fileRemove.className = 'file-remove';" +
      "        fileRemove.innerHTML = '<i class=\"fa-solid fa-xmark\"></i>';" +
      "        fileRemove.addEventListener('click', () => removeFile(index));" +
      "" +
      "        fileItem.appendChild(fileName);" +
      "        fileItem.appendChild(fileRemove);" +
      "        fileList.appendChild(fileItem);" +
      "      });" +
      "    }" +
      "" +
      "    function removeFile(index) {" +
      "      selectedFiles.splice(index, 1);" +
      "      updateFileList();" +
      "    }" +
      "" +
      "    function showError(message) {" +
      "      errorText.textContent = message;" +
      "      errorMessage.style.display = 'flex';" +
      "    }" +
      "" +
      "    function hideError() {" +
      "      errorMessage.style.display = 'none';" +
      "    }" +
      "" +
      "    // Helper function to create a FileList object from an array of files" +
      "    function createFileList(files) {" +
      "      const dataTransfer = new DataTransfer();" +
      "      files.forEach(file => dataTransfer.items.add(file));" +
      "      return dataTransfer.files;" +
      "    }" +
      "" +
      "    pdfForm.addEventListener('submit', function(e) {" +
      "      if (selectedFiles.length < 2) {" +
      "        e.preventDefault();" +
      "        showError('Please select at least two PDF files to merge.');" +
      "        return false;" +
      "      }" +
      "" +
      "      loading.classList.add('active');" +
      "      mergeBtn.disabled = true;" +
      "" +
      "      // Simulate progress (in a real app this would be replaced with actual progress monitoring)" +
      "      let progress = 0;" +
      "      const interval = setInterval(() => {" +
      "        progress += 5;" +
      "        if (progress > 90) {" +
      "          clearInterval(interval);" +
      "        }" +
      "        progressBar.style.width = progress + '%';" +
      "      }, 300);" +
      "" +
      "      return true;" +
      "    });" +
      "  });" +
      "</script>" +
      "</body>" +
      "</html>";
  }
}