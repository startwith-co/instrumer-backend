package instrumers.backend.solution.repository.impl;

import instrumers.backend.solution.domain.SolutionPlanDetailEntity;
import instrumers.backend.solution.repository.custom.SolutionPlanDetailRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SolutionPlanDetailRepositoryImpl implements SolutionPlanDetailRepositoryCustom {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void bulkSave(List<SolutionPlanDetailEntity> planDetails) {
        if (planDetails == null || planDetails.isEmpty()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.batchUpdate(
                """
                        INSERT INTO SOLUTION_PLAN_DETAIL
                        (name, context, solution_plan_seq, created_at, updated_at)
                        VALUES (?, ?, ?, ?, ?)
                        """,
                planDetails,
                1000,
                (ps, detail) -> {
                    ps.setString(1, detail.getName());
                    ps.setString(2, detail.getContext());
                    ps.setLong(3, detail.getSolutionPlanEntity().getSolutionPlanSeq());
                    ps.setTimestamp(4, Timestamp.valueOf(now));
                    ps.setTimestamp(5, Timestamp.valueOf(now));
                }
        );
    }
}
