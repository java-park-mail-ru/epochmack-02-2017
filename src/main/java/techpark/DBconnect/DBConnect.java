package techpark.DBconnect;

import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DataSourceUtils;

import java.sql.Connection;

/**
 * Created by Варя on 28.03.2017.
 */
public abstract class DBConnect {

    protected static DataSource dataSource;


    public static Connection getConnection(){
        return DataSourceUtils.getConnection(dataSource);
    }

}
