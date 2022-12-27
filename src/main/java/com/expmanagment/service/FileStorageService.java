package com.expmanagment.service;


import com.alibaba.fastjson.JSONObject;
import com.expmanagment.exception.FileStorageException;
import com.expmanagment.exception.MyFileNotFoundException;
import com.expmanagment.payload.UploadFileResponse;
import com.expmanagment.property.FileStorageProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class FileStorageService {
    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            //创建文件夹
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileStorageException("无法创建目录", e);
        }
    }

    public String getFileStorageLocation () {
        return this.fileStorageLocation.toString();
    }

    public String getOriginalFileName(MultipartFile file) {
        return StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
    }

    /**
     * 上传文件到根目录
     */
    public String storeFile(MultipartFile file) {
        String fileName = getOriginalFileName(file);

        try {
            //检查文件名是否含有非法字符
            if (fileName.contains((".."))) {
                throw new FileStorageException("文件名含有非法路径序列" + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException e) {
            throw new FileStorageException("存储文件" + fileName + "失败，请重新上传" , e);
        }
    }

    /**
     * 上传文件到指定位置
     */
    public String storeToSpecifiedDirectory(MultipartFile file, String location) {
        String fileName = getOriginalFileName(file);
        upload(file, fileName, location, fileName);
        return fileName;
    }

    /**
     * 上传文件并重命名
     */
    public void storeToSpecifiedDirectoryAndRename(MultipartFile file, String newFileName, String location) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        upload(file, newFileName, location, fileName);
    }

    private void upload(MultipartFile file, String newFileName, String location, String fileName) {
        try {
            //检查文件名是否含有非法字符
            if (fileName.contains((".."))) {
                throw new FileStorageException("文件名含有非法路径序列" + fileName);
            }

            String targetLocation = this.fileStorageLocation.toString() + location;

            File targetFile = new File(targetLocation, newFileName);
            logger.info("fileStorageLocation: "+ targetLocation);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }

            file.transferTo(targetFile);

        } catch (Exception e) {
            e.printStackTrace();
            throw new FileStorageException("存储文件" + fileName + "失败，请重新上传" , e);
        }
    }

    public JSONObject uploadFile(MultipartFile file) {
        String fileName = storeFile(file);
        JSONObject json = new JSONObject();
        json.put("code", 200);
        json.put("message", "成功上传文件到根目录");
        return json;
    }

    public JSONObject uploadFileToSpecifiedDirectory(MultipartFile file, String location) {
        String fileName = storeToSpecifiedDirectory(file, location);
        JSONObject json = new JSONObject();
        json.put("code", 200);
        json.put("message", "成功上传文件到目录: " + location);
        json.put("fileName", fileName);
        return json;
    }

    public JSONObject uploadFileToSpecifiedDirectoryAndRename(MultipartFile file, String newFileName, String location) {
        storeToSpecifiedDirectoryAndRename(file, newFileName, location);
        JSONObject json = new JSONObject();
        json.put("code", 200);
        json.put("message", "成功上传文件到目录: " + location);
        return json;
    }

    /**
     * 上传头像的供Controller调用的方法
     */
    public UploadFileResponse storeAvatar(MultipartFile file, Integer id) {
        String newFileName = "avatar_" + id +".jpg";
        this.storeToSpecifiedDirectoryAndRename(file, newFileName, "/avatar");

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/file/downloadFile/"+"/avatar/")
                .path(newFileName)
                .toUriString();

        return new UploadFileResponse(newFileName,
                fileDownloadUri,
                "image/jpg",
                file.getSize());
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("未找到文件" + fileName);
            }
        } catch (MalformedURLException e) {
            throw new MyFileNotFoundException("未找到文件" + fileName, e);
        }
    }

    public Resource loadFile(String fileName, String dirName) {
        try {
            Path filePath = Paths.get(this.fileStorageLocation + "/" + dirName).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("未找到文件" + fileName);
            }
        } catch (MalformedURLException e) {
            throw new MyFileNotFoundException("未找到文件" + fileName, e);
        }
    }

    public ResponseEntity<Resource> downloadFile(String fileName, String dirName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = this.loadFile(fileName, dirName);

        //Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext()
                    .getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            logger.info("无法确定文件类型");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + resource.getFilename() + "\"")
                .body(resource);

    }

    public Path loadFileAsPath(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return filePath;
            } else {
                throw new MyFileNotFoundException("未找到文件" + fileName);
            }
        } catch (MalformedURLException e) {
            throw new MyFileNotFoundException("未找到文件" + fileName, e);
        }
    }

    public List<JSONObject> getAllFiles(String path) {
        File file = new File(fileStorageLocation.toString() + path);
        File[] fileList = file.listFiles();
        List<JSONObject> fileNameList = new ArrayList<>();

        if (fileList != null) {
            for (File value : fileList) {
                if (value.isFile() && value.getName().charAt(0) != '.') {
                    log.info("value.getName(): " + value.getName());
                    String fileName = value.getName();
                    JSONObject json = new JSONObject();
                    json.put("name", fileName);
                    fileNameList.add(json);
                }
            }
        }
        return fileNameList;
    }

}
