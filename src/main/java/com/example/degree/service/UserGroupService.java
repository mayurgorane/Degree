package com.example.degree.service;

import com.example.degree.entities.UserGroups;
import com.example.degree.entities.Users;
import com.example.degree.repositories.UserGroupRepo;
import com.example.degree.repositories.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserGroupService {

    @Autowired
    private UserGroupRepo userGroupRepository;

    @Autowired
    private UsersRepo userRepository;

    public UserGroups createUserGroup(Long userId, UserGroups userGroup) {

        if (userId == null || userGroup == null) {
            throw new IllegalArgumentException("userId and userGroup must not be null");
        }
        if(Objects.equals(userGroup.getName(), "") ||userGroup.getName()== null){
            throw new IllegalArgumentException(" userGroup must not be null");
        }

        // Check if a UserGroups entity with the same name exists for any user
        UserGroups existingUserGroup = userGroupRepository.findByName(userGroup.getName());

        if (existingUserGroup != null) {
            // If the existing UserGroups entity belongs to a different user, allow creation
            if (!existingUserGroup.getUser().getUserId().equals(userId)) {
                Users user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found with userId: " + userId));
                userGroup.setUser(user);

                if (userGroup.getCreatedDate() == null) {
                    userGroup.setCreatedDate(LocalDate.now());
                }
                userGroup.setModifiedDate(LocalDate.now());

                return userGroupRepository.save(userGroup);
            } else {
                throw new IllegalArgumentException("User group with name '" + userGroup.getName() + "' already exists "  );
            }
        } else {
            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with userId: " + userId));
            userGroup.setUser(user);

            if (userGroup.getCreatedDate() == null) {
                userGroup.setCreatedDate(LocalDate.now());
            }
            userGroup.setModifiedDate(LocalDate.now());

            return userGroupRepository.save(userGroup);
        }
    }


    public UserGroups updateUserGroup(Long groupId, UserGroups updatedUserGroup) {
        Optional<UserGroups> existingUserGroupOptional = userGroupRepository.findById(groupId);

        if (existingUserGroupOptional.isPresent()) {
            UserGroups existingUserGroup = existingUserGroupOptional.get();

            // Check if the updated name is different from the current name
            String updatedName = updatedUserGroup.getName();
            if (!existingUserGroup.getName().equals(updatedName)) {
                // Check if the updated name already exists for the same user
                UserGroups existingWithNameForUser = userGroupRepository.findByNameAndUserId(updatedName, existingUserGroup.getUser().getUserId());

                if (existingWithNameForUser != null && !existingWithNameForUser.getGroupId().equals(existingUserGroup.getGroupId())) {
                    // If another UserGroups entity with the same name exists for the same user, disallow update
                    throw new IllegalArgumentException("User group with name '" + updatedName + "' already exists for this user.");
                }
            }

            // Update the group properties
            existingUserGroup.setName(updatedName);
            existingUserGroup.setModifiedDate(LocalDate.now());
            return userGroupRepository.save(existingUserGroup);
        } else {
            // Handle case where user group with given ID is not found
            throw new IllegalArgumentException("User group with ID " + groupId + " not found.");
        }
    }

    public List<UserGroups> getAllUserGroups() {
        return userGroupRepository.findAll();
    }


    public UserGroups getUserGroupById(Long groupId) {
        Optional<UserGroups> userGroup = userGroupRepository.findById(groupId);
        return userGroup.orElse(null); // Return null or handle not found case as needed
    }

    public void deleteUserGroup(Long groupId) {
        userGroupRepository.deleteById(groupId);
    }

    public List<UserGroups> getGroupsByUserId(Long userId) {
        return userGroupRepository.findByUserId(userId);
    }
}
