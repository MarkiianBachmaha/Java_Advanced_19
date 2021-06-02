package service;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import domain.Photo;
import domain.Student;

public interface PhotoService {

	Photo storeFile(Student student, MultipartFile file) throws IOException;

	Photo getFile(String fileId) throws FileNotFoundException;
}