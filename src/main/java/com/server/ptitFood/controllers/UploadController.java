package com.server.ptitFood.controllers;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("admin/upload")
public class UploadController {

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/images";
    @PostMapping("image")
    @ResponseBody
    public HashMap<String, String> uploadImage(
            @RequestParam("image") MultipartFile file) throws IOException {
//        StringBuilder fileNames = new StringBuilder();

        String newFileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, newFileName);
//        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
//        System.out.println("fileNameAndPath: " + fileNameAndPath);
//        System.out.println("fileNames: " + fileNames);

        HashMap<String, String> map = new HashMap<>();
        map.put("fileName", newFileName);
        map.put("filePath", fileNameAndPath.toString());
        return map;
    }
}