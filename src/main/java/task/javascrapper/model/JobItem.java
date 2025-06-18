package task.javascrapper.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "job_items")
public class JobItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String positionTitle;
    @Column(nullable = false)
    private String url;
    private String logo;
    @Column(nullable = false)
    private String organisationTitle;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_function_id", nullable = false)
    private JobFunction jobFunction;
    @Column(nullable = false)
    private String functionName;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> locations;
    @Column(nullable = false)
    private Long postedOn;
    @Column(length = 65535)
    private String description;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> tags;
}
