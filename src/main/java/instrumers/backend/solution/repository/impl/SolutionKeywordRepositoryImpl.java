package instrumers.backend.solution.repository.impl;

import instrumers.backend.solution.domain.SolutionKeywordEntity;
import instrumers.backend.solution.repository.custom.SolutionKeywordRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SolutionKeywordRepositoryImpl implements SolutionKeywordRepositoryCustom {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void bulkSave(List<SolutionKeywordEntity> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.batchUpdate(
                """
                        INSERT INTO SOLUTION_KEYWORD
                        (keyword, solution_seq, created_at, updated_at)
                        VALUES (?, ?, ?, ?)
                        """,
                keywords,
                1000,
                (ps, keyword) -> {
                    ps.setString(1, keyword.getKeyword());
                    ps.setLong(2, keyword.getSolutionEntity().getSolutionSeq());
                    ps.setTimestamp(3, Timestamp.valueOf(now));
                    ps.setTimestamp(4, Timestamp.valueOf(now));
                }
        );
    }
}

