package server.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import commons.Activity;

@Transactional
public interface ActivitiesRepository extends JpaRepository<Activity, String> {

    @Query("select a from Activity a where a.energyConsumption <= ?1 order by a.energyConsumption desc")
    public List<Activity> findByEnergyConsumptionDesc(long energyConsumption );

    @Query("select a from Activity a where a.energyConsumption >= ?1 and a.energyConsumption <= ?2")
    public Optional<List<Activity>> findActivitiesInRange(long small, long big);

}
