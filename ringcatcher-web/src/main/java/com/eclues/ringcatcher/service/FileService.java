package com.eclues.ringcatcher.service;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eclues.ringcatcher.dao.EnvironmentBean;

@Service("fileService")
public class FileService {
	@Autowired
	private EnvironmentBean environmentBean;
	
	public static final String SAVE_LOCATION = System.getProperty("work.path")+"/file_repo/";
	public static final String FILE_URL = //"http://%s:%s/ringcatcher/filerepo/%s/%s_%s.%s";
			"/ringmedia/%s/%s";
	
	public boolean saveFile(MultipartFile multipartFile) {
		String saveFileName = multipartFile.getOriginalFilename();
		return _saveFile(multipartFile, null, saveFileName);
	}

	public String getYYYYMMDDPath(){
		return new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());
	}

	public String getSaveFileName(MultipartFile multipartFile, String callingNum, String calledNum) {
		int dotPos = multipartFile.getOriginalFilename().lastIndexOf('.');
		String extension = multipartFile.getOriginalFilename().substring(dotPos+1);
		return String.format("%s_%s_%s.%s", callingNum, calledNum, RandomStringUtils.randomAlphanumeric(8), extension);
	}
	
	public String getSaveFileUrl(String yyyymmdd, String fileName) {
		return String.format(FILE_URL /*,environmentBean.getDomainName()
				,environmentBean.getPort()*/
				, yyyymmdd, fileName);
	}

	public String getSaveFilePath(String yyyymmdd, String fileName) {
		return String.format("%s/%s/%s",SAVE_LOCATION, yyyymmdd, fileName);
	}
	
	public boolean saveFile(MultipartFile multipartFile,String pathYYYYMMDD, String saveFileName) {
		return _saveFile(multipartFile, pathYYYYMMDD, saveFileName);
	}
	public boolean _saveFile(MultipartFile multipartFile,String pathYYYYMMDD, String saveFileName) {
		boolean result = false;
		//set the saved location and create a directory location
		String fileName = saveFileName;//multipartFile.getOriginalFilename();
		String location = SAVE_LOCATION;
		if (pathYYYYMMDD != null) location +="/"+pathYYYYMMDD+"/";
		File pathFile = new File(location);
		//check if directory exist, if not , create directory
		if (!pathFile.exists()) {
			pathFile.mkdir();
		}
		//create the actual file
		pathFile = new File(fileName);
		//save the actual file
		try {
			multipartFile.transferTo(pathFile);
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
