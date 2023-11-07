package com.server.ptitFood.domain.entity.repositories;

import com.server.ptitFood.domain.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {
    UserGroup findUserGroupById(Integer groupId);
}
