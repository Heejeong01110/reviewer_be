package org.movie.reviewer.domain.actor.repository;

import org.movie.reviewer.domain.actor.domain.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {

}
