package controller.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import controller.PhotoController;
import domain.Photo;
import domain.Student;
import dto.PhotoUploadResponse;
import service.PhotoService;

@RestController
public class PhotoControllerImpl implements PhotoController {

	@Autowired
	private PhotoService photoService;

	private Photo photoThis;
	private Student student;
	private String fileDownloadUriThis;

	@Override
	@PostMapping("/uploadFile")
	public PhotoUploadResponse uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
		Photo photo = photoService.storeFile(student, file);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
				.path(photo.getId()).toUriString();
		fileDownloadUriThis = fileDownloadUri;

		return new PhotoUploadResponse(student.getFirstName(), student.getLastName(), student.getAge(),
				photo.getFileName(), fileDownloadUri, file.getContentType(), file.getSize());
	}

	@Override
	@GetMapping("/downloadFile/{fileId}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws FileNotFoundException {
		Photo photoFile = photoService.getFile(fileId);
		photoThis = photoFile;
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(photoFile.getFileType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + photoFile.getFileName() + "\"")
				.body(new ByteArrayResource(photoFile.getData()));
	}

	@Override
	@PostMapping("/uploadMultipleFiles")
	public List<PhotoUploadResponse> uploadMultipleFiles(MultipartFile[] files) {
		return Arrays.asList(files).stream().map(file -> {
			try {
				return uploadFile(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
	}

	@Override
	@GetMapping("/downloadFileThis")
	public PhotoUploadResponse downloadFileThis() {
		return new PhotoUploadResponse(photoThis.getStudent().getFirstName(), photoThis.getStudent().getLastName(),
				photoThis.getStudent().getAge(), photoThis.getFileName(), fileDownloadUriThis, photoThis.getFileType(),
				0);
	}

	@Override
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public @ResponseBody PhotoUploadResponse registration(@RequestBody Student student) {
		this.student = student;
		return null;

	}
}