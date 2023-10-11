package com.toyproject.instagram.service;

import com.toyproject.instagram.dto.UploadFeedReqDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FeedService {
    @Value("${file.path}") // yml에서 가져오는 것(ioc에서 주입(DI)될 때 yml에서 참조해서 가져옴)
    private String filePath;

    public void upload(UploadFeedReqDto uploadFeedReqDto) {
        uploadFeedReqDto.getFiles().forEach(file -> {
            String originName = file.getOriginalFilename();
            String extensionName = originName.substring(originName.lastIndexOf(".")); // 확장자명
            // A사용자와 B사용자가 같은 파일명으로 업로드시, 겹치지 않도록 전처리
            String newName = UUID.randomUUID().toString().replaceAll("-", "").concat(extensionName);
            Path uploadPath = Paths.get(filePath + "/feed/" + newName);
            File f = new File(filePath + "/feed");

            if(!f.exists()) {
                // 파일경로가 존재하지 않으면 ioException이 뜨므로 파일경로 없으면 위의 폴더 mkdir해라.
                f.mkdir();
            }

            try {
                Files.write(uploadPath, file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
