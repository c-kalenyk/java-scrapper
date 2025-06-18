package task.javascrapper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import task.javascrapper.model.JobFunction;

public interface JobFunctionRepository extends JpaRepository<JobFunction, Integer> {
    JobFunction findByFunction(String function);

    boolean existsByFunction(String function);
}
