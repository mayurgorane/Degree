package com.example.degree.controller;

import com.example.degree.dto.GroupLocationDTO;
import com.example.degree.entities.Degree;
import com.example.degree.entities.GroupLocation;
import com.example.degree.entities.UserGroups;
import com.example.degree.exception.ResourceNotFoundException;
import com.example.degree.repositories.GroupLocationRepo;
import com.example.degree.repositories.UserGroupRepo;
import com.example.degree.service.GroupLocationService;
import com.example.degree.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GroupLocationController {
    @Autowired
    UserGroupService userGroupsService;

    @Autowired
    GroupLocationService groupLocationService;

    @Autowired
    UserGroupRepo userGroupRepo;

    @Autowired
    GroupLocationRepo groupLocationRepo;


    @PostMapping("/group/{groupId}/location")
    public ResponseEntity<GroupLocation> addGroupLocation(
            @PathVariable Long groupId,
            @RequestBody GroupLocation groupLocation
    ) {
        // Retrieve the user group by groupId from the URL
        UserGroups userGroup = userGroupsService.getUserGroupById(groupId);
        if (userGroup == null) {
            return ResponseEntity.notFound().build();
        }

        // Set the retrieved user group to the group location
        groupLocation.setUserGroup(userGroup);

        // Save the group location
        GroupLocation savedLocation = groupLocationService.saveGroupLocation(groupLocation);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLocation);
    }

    @DeleteMapping("/group/location/{locationId}")
    public ResponseEntity<?> deleteGroupLocation(
            @PathVariable Long locationId
    ) {
        try {

            groupLocationService.deleteGroupLocationById(locationId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/group/location/{locationId}")
    public ResponseEntity<GroupLocation> updateGroupLocation(
            @PathVariable Long locationId,
            @RequestBody GroupLocation groupLocation
    ) {
        try {
            GroupLocation groupLocation1 =groupLocationRepo.findById(locationId).
                    orElseThrow(() -> new ResourceNotFoundException("groupLocation not found"));;

            UserGroups userGroups = userGroupRepo.findById(groupLocation1.getUserGroup().getGroupId())
                    .orElseThrow(() -> new ResourceNotFoundException("userGroup not found"));

            if (!groupLocationService.existsById(locationId)) {
                return ResponseEntity.notFound().build();
            }


            groupLocation.setLocationId(locationId);
            groupLocation.setUserGroup(userGroups);


            // Update the location
            GroupLocation updatedLocation = groupLocationService.updateGroupLocation(groupLocation);

            return ResponseEntity.ok(updatedLocation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
