package org.challenge.traceip.repository;

import java.util.Optional;

import org.challenge.traceip.model.DistanceToBsAsAverage;
import org.challenge.traceip.model.RequestTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestTrackerRepository extends JpaRepository<RequestTracker, Long>, JpaSpecificationExecutor<RequestTracker> {

    Optional<RequestTracker> findTop1OptionalByOrderByDistanceToBsAsAsc();
    
    Optional<RequestTracker> findTop1OptionalByOrderByDistanceToBsAsDesc();
    
    @Query("SELECT new org.challenge.traceip.model.DistanceToBsAsAverage(AVG(distanceToBsAs) as distanceToBsAs) FROM RequestTracker")
    DistanceToBsAsAverage findAverageDistanceToBsAs();
    
}
