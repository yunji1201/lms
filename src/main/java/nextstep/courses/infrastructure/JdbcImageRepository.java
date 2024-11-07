package nextstep.courses.infrastructure;

import nextstep.courses.domain.Image;
import nextstep.courses.domain.ImageRepository;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository("imageRepository")
public class JdbcImageRepository implements ImageRepository {
    private final JdbcOperations jdbcTemplate;

    public JdbcImageRepository(JdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(Image image) {
        String sql = "INSERT INTO image (file_name, file_size, file_type, width, height) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int result = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, image.getFileName());
            ps.setInt(2, image.getFileSize());
            ps.setString(3, image.getFileType());
            ps.setInt(4, image.getWidth());
            ps.setInt(5, image.getHeight());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            image.setId(keyHolder.getKey().longValue());
        }
        return result;
    }

    @Override
    public Image findById(Long id) {
        String sql = "SELECT id, file_name, file_size, file_type, width, height FROM image WHERE id = ?";
        RowMapper<Image> rowMapper = (rs, rowNum) -> {
            Image image = new Image(
                    rs.getString("file_name"),
                    rs.getInt("file_size"),
                    rs.getString("file_type"),
                    rs.getInt("width"),
                    rs.getInt("height")
            );
            return image;
        };
        List<Image> images = jdbcTemplate.query(sql, rowMapper, id);
        return images.isEmpty() ? null : images.get(0);
    }

    @Override
    public int update(Image image) {
        String sql = "update image set file_name = ?, file_size = ?, file_type = ?, width = ?, height = ? where id = ?";
        return jdbcTemplate.update(sql,
                image.getFileName(),
                image.getFileSize(),
                image.getFileType(),
                image.getWidth(),
                image.getHeight(),
                image.getId());
    }

    @Override
    public int deleteById(Long id) {
        String sql = "delete from image where id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
