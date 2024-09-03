package com.red.domovie.service.hometheater;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${app.upload.dir}") // application.properties에서 경로 읽기
    private String uploadDir;

    public String uploadFile(MultipartFile file) throws IOException {
        // 파일 이름 생성
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // 파일 경로 설정
        File destinationFile = new File(uploadDir + File.separator + fileName);

        // 파일 저장
        file.transferTo(destinationFile);

        return fileName; // 저장된 파일 이름 반환
    }
}

