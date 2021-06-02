package service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import domain.Photo;
import domain.Student;
import repository.PhotoRepository;
import service.PhotoService;

@Service
public class PhotoServiceImpl implements PhotoService {

	@Autowired
	private PhotoRepository photoRepository;

	@Override
	public Photo storeFile(Student student, MultipartFile file) throws IOException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		Photo multipart = null;

		if (!fileName.contains("..")) {
			multipart = new Photo(fileName, file.getContentType(), file.getBytes());
		}
		if (student != null) {
			multipart.setStudent(student);
			return photoRepository.save(multipart);
		}
		return photoRepository.save(multipart);
	}

	@Override
	public Photo getFile(String fileId) throws FileNotFoundException {
		return photoRepository.findById(fileId)
				.orElseThrow(() -> new FileNotFoundException("File not found with Id = " + fileId));
	}

}