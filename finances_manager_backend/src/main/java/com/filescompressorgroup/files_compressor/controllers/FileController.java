package com.filescompressorgroup.files_compressor.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.filescompressorgroup.files_compressor.dtos.UploadFileResponseDTO;
import com.filescompressorgroup.files_compressor.models.FilePresentation;
import com.filescompressorgroup.files_compressor.services.FileStorageService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "File Endpoint")
@RestController
@RequestMapping("/api/file/v1")
public class FileController {

	private Logger logger = Logger.getLogger(FileController.class.getName());

	@Autowired
	private FileStorageService service;
	
	
	@PostMapping("/compressFile")
	public UploadFileResponseDTO compressFile(@RequestParam("file") MultipartFile file) {
		logger.info("Storing file to disk");

		var filename = service.compress(file);
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/file/v1/downloadCompressedFile/")
				.path(filename)
				.toUriString();

		return new UploadFileResponseDTO(
				filename, fileDownloadUri, file.getContentType(), file.getSize());
	}
	
	
	@GetMapping("/downloadCompressedFile/{filename:.+}")
	public ResponseEntity<Resource> downloadCompressedFile(
			@PathVariable String filename, HttpServletRequest request) {

		logger.info("Reading a file on disk");

		Resource resource = service.loadCompressedFileAsResource(filename);
		String contentType = "";

		try {
			contentType = request.getServletContext().getMimeType(
					resource.getFile().getAbsolutePath());
		} catch (Exception e) {
			logger.info("Could not determine file type!");
		}

		if (contentType.isBlank()) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(
						HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
	
	
	@PostMapping("/decompressFile")
	public UploadFileResponseDTO decompressFile(@RequestParam("file") MultipartFile file) {
		logger.info("Storing file to disk");

		var filename = service.decompress(file);
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/file/v1/downloadDecompressedFile/")
				.path(filename)
				.toUriString();

		return new UploadFileResponseDTO(
				filename, fileDownloadUri, file.getContentType(), file.getSize());
	}
	
	
	@GetMapping("/downloadDecompressedFile/{filename:.+}")
	public ResponseEntity<Resource> downloadDecompressedFile(
			@PathVariable String filename, HttpServletRequest request) {

		logger.info("Reading a file on disk");

		Resource resource = service.loadDecompressedFileAsResource(filename);
		String contentType = "";

		try {
			contentType = request.getServletContext().getMimeType(
					resource.getFile().getAbsolutePath());
		} catch (Exception e) {
			logger.info("Could not determine file type!");
		}

		if (contentType.isBlank()) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(
						HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
	/*@GetMapping("/downloadFile/{filename:.+}")
	public ResponseEntity<Resource> downloadFile(
			@PathVariable String filename, HttpServletRequest request) {

		logger.info("Reading a file on disk");

		Resource resource = service.loadFileAsResource(filename);
		String contentType = "";

		try {
			contentType = request.getServletContext().getMimeType(
					resource.getFile().getAbsolutePath());
		} catch (Exception e) {
			logger.info("Could not determine file type!");
		}

		if (contentType.isBlank()) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(
						HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}*/

	@GetMapping("/findAllFiles")
	public ResponseEntity<List<FilePresentation>> findAll() throws Exception {
		List<FilePresentation> filesPresentations = service.findAll();
		return new ResponseEntity<List<FilePresentation>>(filesPresentations, HttpStatus.OK);
	}
	
	
	/*@PostMapping("/compressMultipleFiles")
	public List<UploadFileResponseDTO> compressMultipleFiles(
			@RequestParam("files") MultipartFile[] files) {
		logger.info("Storing files to disk");

		return Arrays.asList(files)
				.stream()
				.map(file -> compressFile(file))
				.collect(Collectors.toList());
	}*/

	/*@PostMapping("/uploadFile")
	public UploadFileResponseDTO uploadFile(@RequestParam("file") MultipartFile file) {
		logger.info("Storing file to disk");

		var filename = service.storeFile(file);
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/file/v1/downloadFile/")
				.path(filename)
				.toUriString();

		return new UploadFileResponseDTO(
				filename, fileDownloadUri, file.getContentType(), file.getSize());
	}*/

	/*@PostMapping("/uploadMultipleFiles")
	public List<UploadFileResponseDTO> uploadMultipleFiles(
			@RequestParam("files") MultipartFile[] files) {
		logger.info("Storing files to disk");

		return Arrays.asList(files)
				.stream()
				.map(file -> uploadFile(file))
				.collect(Collectors.toList());
	}*/
}
