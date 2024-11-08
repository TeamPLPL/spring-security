package com.kosa.backend.funding.project.controller;

import com.kosa.backend.funding.project.dto.requestdto.RequestProjectIntroDTO;
import com.kosa.backend.funding.project.dto.requestdto.RequestRewardDTO;
import com.kosa.backend.funding.project.dto.requestdto.RequestRewardInfoDTO;
import com.kosa.backend.funding.project.service.ProjectService;
import com.kosa.backend.funding.project.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RewardApiController {
    private final RewardService rewardService;

    // 프로젝트 리워드 생성
    @PostMapping("/studio/{id}/reward")
    public ResponseEntity<?> reward(@RequestBody List<RequestRewardDTO> rewardsDTOList
            , @PathVariable(name = "id") int projectId) {
        // 리워드 여러개일 수 있음
        int projectIdFromReward = rewardService.save(rewardsDTOList, projectId);

        Map<String, Object> response = new HashMap<>();
        response.put("projectId", projectIdFromReward);

        return ResponseEntity.ok()
                .body(response);
    }

    // 프로젝트 정책 생성
    @PostMapping("/studio/{id}/policy")
    public ResponseEntity<?> policy(@RequestBody RequestRewardInfoDTO requestRewardInfoDTO
            , @PathVariable(name = "id") int projectId ) {

        int projectIdFromRewardInfo = rewardService.savePolicy(requestRewardInfoDTO, projectId);

        Map<String, Object> response = new HashMap<>();
        response.put("projectId", projectIdFromRewardInfo);

        return ResponseEntity.ok()
                .body(response);
    }

    // 프로젝트 리워드 업테이트
//    @PostMapping("/studio/{id}/policy")
//    public ResponseEntity<?> policy(@RequestBody List<RequestRewardDTO> rewardsDTOList
//            , @PathVariable(name = "id") int projectId ) {
//
//        int projectIdFromReward = rewardService.update(rewardsDTOList, projectId);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("projectId", projectIdFromReward);
//
//        return ResponseEntity.ok()
//                .body(response);
//    }
}
