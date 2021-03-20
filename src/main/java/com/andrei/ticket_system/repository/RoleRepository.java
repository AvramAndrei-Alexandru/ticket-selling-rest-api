package com.andrei.ticket_system.repository;

import com.andrei.ticket_system.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Modifying
    @Query(value = "delete from role where login not in (select login from user)", nativeQuery = true)
    void cleanUp();

    boolean existsByLogin(String login);
}
