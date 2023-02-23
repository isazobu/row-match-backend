package com.isazobu.rowmatch.backend.team.repository;

import com.isazobu.rowmatch.backend.team.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByName(String name);
//    @Query("SELECT t FROM Team t WHERE (SELECT COUNT(u) FROM User u WHERE u.team = t) < 20 ORDER BY RAND()")
//    List<Team> findRandomJoinableTeams(Pageable pageable);


}
