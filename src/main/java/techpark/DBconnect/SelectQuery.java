package techpark.DBconnect;

import java.net.URISyntaxException;
import java.sql.*;

/**
 * Created by Варя on 28.03.2017.
 */
public class SelectQuery {

    public static <T> T execute(String query, PrepStatement<T> st) throws SQLException {
        Connection connection = DBConnect.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                final T result = st.getStatement(preparedStatement);
                return result;
            }
    }
}
