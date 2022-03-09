package server.database;

import org.springframework.data.jpa.repository.JpaRepository;
import server.entities.Activity;

import javax.transaction.Transactional;

@Transactional
public interface ActivitiesRepository extends JpaRepository<Activity, String> {
}
