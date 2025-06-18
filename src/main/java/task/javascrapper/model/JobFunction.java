package task.javascrapper.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "job_functions")
public class JobFunction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "function_name")
    private String function; //I've thought about creating some constant values for it, but I suppose it would be easier to choose this value from some dropdown list on frontend side
    private Integer count;
    @OneToMany(mappedBy = "jobFunction", fetch = FetchType.EAGER, //I know that it isn't the best choice, but since we need to fetch everything in category anyway, I think it will be simpler
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobItem> jobs;
}
