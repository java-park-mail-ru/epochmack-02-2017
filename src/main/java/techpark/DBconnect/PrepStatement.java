package techpark.DBconnect;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Варя on 29.03.2017.
 */
public interface PrepStatement<T> {
    T getStatement(PreparedStatement preparedStatement) throws SQLException;
}
