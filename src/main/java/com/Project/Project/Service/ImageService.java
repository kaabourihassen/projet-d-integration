package com.Project.Project.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Project.Project.Entity.Image;
import com.Project.Project.Repository.ImageRepository;

@Service
public class ImageService {

	@Autowired
	ImageRepository imgRepo;
	
	public void deleteImage(Long imageId) {
		imgRepo.deleteById(imageId);
	}

	public void newImage(Image img) {
		imgRepo.save(img);
	}
	
	public List<Image> getAllImages(){
		return imgRepo.findAll();
	}
	
	public Image getImageById(Long id) {
		return imgRepo.findById(id).get();
	}
	
	public void updateImage(Image img) {
		imgRepo.save(img);
	}
}
