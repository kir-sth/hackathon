package scraper.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scraper.config.DbConfig;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ScraperDao {

    DbConfig dbConfig;

    public ScraperDao(
            @Autowired DbConfig dbConfig
    ) {
        this.dbConfig = dbConfig;
    }

    public Connection connect() throws ClassNotFoundException, SQLException {
        String dbUrl = "jdbc:postgresql://" + dbConfig.getDbHost() + ":" + dbConfig.getDbPort() +
                "/" + dbConfig.getDbName() + "?&targetServerType=master&prepareThreshold=0&ssl=true&sslmode=verify-full" +
                "&sslrootcert=.pg/root.crt";

        Class.forName(dbConfig.getDbDriver());
        return DriverManager.getConnection(dbUrl, dbConfig.getDbUser(), dbConfig.getDbPass());
    }
}
