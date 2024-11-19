package com.kosa.backend.funding.support.controller;

import com.kosa.backend.funding.support.dto.SupporterBoardDTO;
import com.kosa.backend.funding.support.entity.SupporterBoard;
import com.kosa.backend.funding.support.service.SupporterBoardService;
import com.kosa.backend.user.dto.CustomUserDetails;
import com.kosa.backend.user.entity.User;
import com.kosa.backend.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supporter-boards")
public class SupporterBoardController {

    private final SupporterBoardService supporterBoardService;
    private final UserService userService;

    public SupporterBoardController(SupporterBoardService supporterBoardService, UserService userService) {
        this.supporterBoardService = supporterBoardService;
        this.userService = userService;
    }

    // Create
    @PostMapping("/{fundingId}")
    public ResponseEntity<SupporterBoardDTO> addSupporterBoard(
            @PathVariable("fundingId") int fundingId,
            @RequestBody SupporterBoardDTO supporterBoardDTO,
            @AuthenticationPrincipal CustomUserDetails cud
    ) {
        String userEmail = cud.getUsername();
        User user = userService.getUser(userEmail);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        SupporterBoard savedBoard = supporterBoardService.addSupporterBoard(fundingId, supporterBoardDTO, user);

        supporterBoardDTO.setId(savedBoard.getId());
        supporterBoardDTO.setBoardDate(savedBoard.getBoardDate());
        return ResponseEntity.ok(supporterBoardDTO);
    }

    // Read (by ID)
    @GetMapping("/{id}")
    public ResponseEntity<SupporterBoard> getSupporterBoardById(@PathVariable("id") int id) {
        return supporterBoardService.getSupporterBoardById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Read (all by funding ID)
    @GetMapping("/funding/{fundingId}")
    public ResponseEntity<List<SupporterBoardDTO>> getSupporterBoardsByFundingId(@PathVariable("fundingId") int fundingId) {
        List<SupporterBoardDTO> boards = supporterBoardService.getSupporterBoardsByFundingId(fundingId);
        return ResponseEntity.ok(boards);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupporterBoardById(@PathVariable("id") int id) {
        supporterBoardService.deleteSupporterBoardById(id);
        return ResponseEntity.noContent().build();
    }
}
