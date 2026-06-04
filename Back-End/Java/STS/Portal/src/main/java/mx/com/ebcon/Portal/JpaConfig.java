
package mx.com.ebcon.Portal;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaConfig
{
    @Bean
    DataSource dataSource()
    {
        DataSourceBuilder<?> dsb = DataSourceBuilder.create();
        dsb.driverClassName("com.mysql.cj.jdbc.Driver");
        dsb.password("portaluser");
        dsb.username("portaluser");
        dsb.url("jdbc:mysql://localhost:3306/portalcfdi?autoReconnect=true&useSSL=true");
        return dsb.build();
    }
}
