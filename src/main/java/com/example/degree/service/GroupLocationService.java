package com.example.degree.service;

import com.example.degree.dto.GroupLocationDTO;
import com.example.degree.entities.GroupLocation;
import com.example.degree.repositories.GroupLocationRepo;
import com.example.degree.repositories.UserGroupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupLocationService {
    @Autowired
    GroupLocationRepo groupLocationRepo;

    @Autowired
    UserGroupRepo userGroupRepo;

    public GroupLocation saveGroupLocation(GroupLocation groupLocation) {
        return groupLocationRepo.save(groupLocation);
    }
    public void deleteGroupLocationById(Long id) {
        groupLocationRepo.deleteById(id);
    }



    public boolean existsById(Long id) {
        return groupLocationRepo.existsById(id);
    }

    public GroupLocation updateGroupLocation(GroupLocation groupLocation) {

        if (groupLocationRepo.existsById(groupLocation.getLocationId())) {
            return groupLocationRepo.save(groupLocation);
        }
        return null;
    }




}
