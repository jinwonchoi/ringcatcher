package com.eclues.ringcatcher.ctrl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.eclues.ringcatcher.service.FileService;

@Controller
public class FileUploadController {
	
	@Autowired
	private FileService fileService;
		
	@RequestMapping(method = RequestMethod.POST, value="/upload")
	public ModelAndView uploadFile(@RequestParam("file") MultipartFile multipartFile,
			@RequestParam("name") String name)
	{
		
		System.out.println("Name is "+name);
		long fileSize = multipartFile.getSize();
		String fileName = multipartFile.getOriginalFilename();
		ModelAndView modelAndView = new ModelAndView("upload_result");
		Map<String, Object> modelMap = new HashMap<>();
		modelMap.put("fileName", fileName);
		modelMap.put("fileSize", fileSize);
		if (fileService.saveFile(multipartFile)) {
			modelMap.put("uploadResult", "upload completed");
		} else {
			modelMap.put("uploadResult", "upload failed");
		}
		modelAndView.addAllObjects(modelMap);
		return modelAndView;
	}
}
