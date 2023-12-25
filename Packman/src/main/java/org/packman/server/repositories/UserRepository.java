package org.packman.server.repositories;

import org.packman.server.models.AppUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<AppUser, UUID> {
    @Query("SELECT  ROW_NUMBER() OVER (ORDER BY u.bestPoints DESC) AS position FROM AppUser u WHERE u.username = :username ORDER BY u.bestPoints DESC")
    Optional<Integer> getPositionByUsernameAndPoints(@Param("username") String username);

    @Query("SELECT u FROM AppUser u ORDER BY u.bestPoints DESC")
    List<AppUser> getTopNUsers(Pageable pageable);

    List<AppUser> findAll();

    @Modifying
    default void addUser(AppUser user) {
        this.save(user);
    }

    @Modifying
    @Query("update AppUser u set u.bestPoints= :points where u.username = :username")
    void updateByUserName(@Param("username") String username, @Param("points") int points);
}
