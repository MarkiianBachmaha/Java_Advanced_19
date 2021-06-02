package controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import domain.Student;
import dto.PhotoUploadResponse;

public interface PhotoController {

	PhotoUploadResponse uploadFile(MultipartFile file) throws IOException;

	ResponseEntity<Resource> downloadFile(String fileId) throws FileNotFoundException;

	List<PhotoUploadResponse> uploadMultipleFiles(MultipartFile[] files);

	PhotoUploadResponse downloadFileThis();

	PhotoUploadResponse registration(Student students);

}