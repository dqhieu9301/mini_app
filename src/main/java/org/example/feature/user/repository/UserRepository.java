package org.example.feature.user.repository;

import org.example.feature.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);

    @Query("select u.totalPoint from User u where u.id =:userId")
    Long getTotalPointsByUserId(@Param("userId") UUID userId);

    @Modifying
    @Query("update User u set u.lastToken =: lastToken, u.totalPoint =:totalPoint where u.id =:userId")
    void updateUser(@Param("lastToken") Long lastToken, @Param("totalPoint") Long totalPoint, @Param("userId") UUID userId);
}
