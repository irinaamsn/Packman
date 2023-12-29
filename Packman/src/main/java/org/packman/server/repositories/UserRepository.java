package org.packman.server.repositories;

import org.packman.server.models.AppUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<AppUser, UUID> {
    @Query("SELECT ROW_NUMBER() OVER (ORDER BY u.bestPoints DESC) FROM AppUser u WHERE u.username = :username AND u.bestPoints = :points")
//    @Query("SELECT ROW_NUMBER() OVER (ORDER BY u.bestPoints DESC) AS position FROM AppUser u WHERE u.username = :username and u.bestPoints= :points ORDER BY u.bestPoints DESC")
    Optional<Integer> getPositionByUsernameAndPoints(@Param("username") String username, @Param("points") int points);

    @Query("SELECT u FROM AppUser u ORDER BY u.bestPoints DESC")
    List<AppUser> getSortUsers();

    @Query("SELECT u FROM AppUser u ORDER BY u.bestPoints DESC")
    List<AppUser> getTopUsers(Pageable pageable);

    @Query("select case when COUNT(u) > 0 then true else false end from AppUser u where u.username = :username and u.bestPoints= :points")
    boolean existsByUsernameAndBestPoints(@Param("username") String username, @Param("points") int points);

    @Modifying
    default void addUser(AppUser user) {
        this.save(user);
    }

}
