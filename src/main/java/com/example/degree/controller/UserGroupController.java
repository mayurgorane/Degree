package com.example.degree.controller;

import com.example.degree.entities.UserGroups;
import com.example.degree.repositories.UserGroupRepo;
import com.example.degree.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class UserGroupController {
@Autowired
    UserGroupService userGroupService;

    @PostMapping("/user-groups/{userId}")
    public ResponseEntity<UserGroups> createUserGroup(@PathVariable Long userId, @RequestBody UserGroups userGroup) {
        UserGroups createdUserGroup = userGroupService.createUserGroup(userId, userGroup);
        return new ResponseEntity<>(createdUserGroup, HttpStatus.CREATED);
    }


    @PutMapping("/user-groups/{groupId}")
    public ResponseEntity<UserGroups> updateUserGroup(
            @PathVariable Long groupId,
            @RequestBody UserGroups updatedUserGroup) {

        try {
            UserGroups updatedGroup = userGroupService.updateUserGroup(groupId, updatedUserGroup);
            return ResponseEntity.ok(updatedGroup);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @GetMapping("/user-groups")
    public ResponseEntity<List<UserGroups>> getAllUserGroups() {
        List<UserGroups> userGroups = userGroupService.getAllUserGroups();
        return ResponseEntity.ok(userGroups);
    }

    @GetMapping("/user-groups/{groupId}")
    public ResponseEntity<UserGroups> getUserGroupById(@PathVariable Long groupId) {
        UserGroups userGroup = userGroupService.getUserGroupById(groupId);
        if (userGroup != null) {
            return ResponseEntity.ok(userGroup);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/user-groups/{groupId}")
    public ResponseEntity<Void> deleteUserGroup(@PathVariable Long groupId) {
        userGroupService.deleteUserGroup(groupId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/userGroup/{userId}")
    public ResponseEntity<List<UserGroups>> getGroupsByUserId(@PathVariable Long userId) {
        List<UserGroups> userGroups = userGroupService.getGroupsByUserId(userId);
        if (userGroups != null && !userGroups.isEmpty()) {
            return ResponseEntity.ok(userGroups);
        } else {
            return ResponseEntity.ok(userGroups != null ? userGroups : Collections.emptyList());
        }
    }

}
