package nextstep.courses.infrastructure;

import nextstep.courses.domain.SessionRepository;
import nextstep.courses.domain.session.FreeSession;
import nextstep.courses.domain.session.PaidSession;
import nextstep.courses.domain.session.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class JdbcSessionRepository implements SessionRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcSessionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(Session session) {
        String sql = "INSERT INTO session (course_id, type, duration, start_time, end_time, price, title) VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int result = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, session.getCourseId());
            ps.setString(2, session instanceof PaidSession ? "PAID" : "FREE");
            ps.setInt(3, (int) session.getDuration());
            ps.setObject(4, session.getStartTime());
            ps.setObject(5, session.getEndTime());
            ps.setInt(6, session instanceof PaidSession ? ((PaidSession) session).getSessionFee() : 0);
            ps.setString(7, session.getTitle());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            session.setId(keyHolder.getKey().longValue());
        }
        return result;
    }

    @Override
    public Session findById(Long id) {
        String sql = "SELECT id, course_id, type, duration, start_time, end_time, price, title FROM session WHERE id = ?";
        RowMapper<Session> rowMapper = new SessionRowMapper();
        List<Session> sessions = jdbcTemplate.query(sql, rowMapper, id);
        return sessions.isEmpty() ? null : sessions.get(0);
    }

    @Override
    public int update(Session session) {
        String sql = "UPDATE session SET course_id = ?, title = ?,  type = ?, duration = ?, start_time = ?, end_time = ?, price = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                session.getCourseId(),
                session.getTitle(),
                session instanceof PaidSession ? "PAID" : "FREE",
                session.getDuration(),
                session.getStartTime(),
                session.getEndTime(),
                session instanceof PaidSession ? ((PaidSession) session).getSessionFee() : 0,
                session.getId()
        );
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM session WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    private static class SessionRowMapper implements RowMapper<Session> {
        @Override
        public Session mapRow(ResultSet rs, int rowNum) throws SQLException {
            String type = rs.getString("type");
            Long id = rs.getLong("id");
            Long courseId = rs.getLong("course_id");
            String title = rs.getString("title");
            LocalDate startDate = rs.getObject("start_time", LocalDate.class);
            LocalDate endDate = rs.getObject("end_time", LocalDate.class);
            int duration = rs.getInt("duration");
            int price = rs.getInt("price");

            if ("PAID".equalsIgnoreCase(type)) {
                int maxEnrollment = 30;
                return new PaidSession(courseId, title, startDate, endDate, null, maxEnrollment, price);
            }
            return new FreeSession(courseId, title, startDate, endDate, null);
        }
    }
}
