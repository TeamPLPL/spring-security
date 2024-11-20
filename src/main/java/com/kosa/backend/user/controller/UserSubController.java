package com.kosa.backend.user.controller;

import com.kosa.backend.common.dto.FileDTO;
import com.kosa.backend.common.entity.enums.ImgType;
import com.kosa.backend.common.service.S3CustomService;
import com.kosa.backend.common.service.S3Service;
import com.kosa.backend.funding.project.service.RewardSubService;
import com.kosa.backend.user.dto.CustomUserDetails;
import com.kosa.backend.user.entity.User;
import com.kosa.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserSubController {
    private final UserService userService;
    private final RewardSubService rewardService;
    private final S3Service s3Service;
    private final S3CustomService s3CustomService;

    // 프로필 이미지 업로드
    @PostMapping("/upload/profileimage")
    public ResponseEntity<?> uploadProfileImage(@AuthenticationPrincipal CustomUserDetails cud,
                                            @RequestParam("file") MultipartFile file) throws IOException {
        // 인증된 User 체크 메소드 따로 빼기
        String userEmail = cud.getUsername();
        User user = userService.getUser(userEmail);
        if(user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return s3Service.uploadImgFile(user, file, ImgType.PROFILE_IMAGE);
    }

    // 프로필 이미지가져오는 컨트롤러
    @GetMapping("/get/profileimage")
    public ResponseEntity<?> getThubnail(@AuthenticationPrincipal CustomUserDetails cud) throws IOException {
        // 인증된 User 체크 메소드 따로 빼기
        String userEmail = cud.getUsername();
        User user = userService.getUser(userEmail);
        if(user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        FileDTO profileImage =  s3CustomService.getprofile(user.getId());
        System.out.println(profileImage.toString());

        return ResponseEntity.ok(profileImage);
    }

    // 프로필 이미지 삭제 컨트롤러
    @GetMapping("{fileId}/delete/profileimage")
    public ResponseEntity<?> deleteImage(@AuthenticationPrincipal CustomUserDetails cud,
                                         @PathVariable(name = "fileId") int fileId) {
        // 인증된 User 체크 메소드 따로 빼기
        String userEmail = cud.getUsername();
        User user = userService.getUser(userEmail);
        if(user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        s3Service.deleteImgFile(user, fileId);

        return ResponseEntity.ok().build();
    }
}