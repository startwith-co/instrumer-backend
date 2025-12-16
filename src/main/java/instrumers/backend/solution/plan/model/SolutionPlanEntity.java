package instrumers.backend.solution.plan.model;

import instrumers.backend.base.BaseTimeEntity;
import instrumers.backend.solution.solution.model.SolutionEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "SOLUTION_PLAN")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
public class SolutionPlanEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solution_plan_seq")
    private Long solutionPlanSeq;

    @Column(name = "name", nullable = false, unique = false)
    private String name;

    @Column(name = "sub_name", nullable = true, unique = false)
    private String subName;

    @Column(name = "price", nullable = true, unique = false)
    private Long price;

    @Column(name = "plan_type", nullable = false, unique = false)
    private String planType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solution_seq", nullable = false)
    private SolutionEntity solutionEntity;
}
