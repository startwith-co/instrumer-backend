package instrumers.backend.solution.plan.model;

import instrumers.backend.solution.plan.util.PlanType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
public class PlanEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "plan_seq")
	private Long planSeq;

	@Column(name = "name", nullable = false, unique = false)
	private String name;

	@Column(name = "sub_name", nullable = true, unique = false)
	private String subName;

	@Column(name = "price", nullable = true, unique = false)
	private Long price;

	@Enumerated(EnumType.STRING)
	@Column(name = "plan_type", nullable = false, unique = false)
	private PlanType planType;
}
