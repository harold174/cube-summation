package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.storage.StorageFileNotFoundException;
import com.storage.StorageService;
import com.summation.SummationService;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Main controller to upload and executes files
 * 
 * @author harold.murcia
 */
@Controller
public class MainController {

	/**
	 * Storage service for file operations
	 */
    private final StorageService storageService;
    
    /**
     * Summation service for file execution
     */
    @Autowired
    private SummationService summationService;

    /**
     * Main controller autowired with given storage service
     * @param storageService
     */
    @Autowired
    public MainController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * List all server files inside upload directory
     * @return uploadForm redirection
     * @throws IOException
     */
    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService
                .loadAll()
                .map(path ->
                        MvcUriComponentsBuilder
                                .fromMethodName(MainController.class, "serveFile", path.getFileName().toString())
                                .build().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }
    
    /**
     * Open in browser the selected file
     * @param filename selected file
     * @return open the resource from the selected file
     */
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
                .body(file);
    }

    /**
     * Upload and execute the input file, also creates and output file with the process results.
     * @param file to be uploaded
     * @param redirectAttributes
     * @return refresh form redirect
     */
    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
    	storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");
        
        summationService.executeFile();
        
        storageService.deleteFile("input.in");

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}