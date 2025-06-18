package task.javascrapper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import task.javascrapper.model.JobItem;

public interface JobItemRepository extends JpaRepository<JobItem, Integer>,
        JpaSpecificationExecutor<JobItem> {
}
