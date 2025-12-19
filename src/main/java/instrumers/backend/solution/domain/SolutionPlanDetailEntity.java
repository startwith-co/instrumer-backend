package instrumers.backend.solution.domain;

import instrumers.backend.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "SOLUTION_PLAN_DETAIL")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
public class SolutionPlanDetailEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solution_plan_detail_seq")
    private Long solutionPlanDetailSeq;

    @Column(name = "name", nullable = false, unique = false)
    private String name;

    @Column(name = "context", nullable = false, unique = false)
    private String context;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solution_plan_seq", nullable = false)
    private SolutionPlanEntity solutionPlanEntity;
}
