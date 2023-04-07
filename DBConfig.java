package moorthi.test.db.config;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan("moorthi.test.entity")
@EnableJpaRepositories(basePackages="moorthi.test.dao",
entityManagerFactoryRef="dartDistrictEntityManager",
transactionManagerRef="dartDistrictTransactionManager")


@EnableTransactionManagement
@PropertySource(value = {"classpath:persistence-db.properties"})
public class DARTDistrictDBConfig {
  
  @Value("${spring.dart-datasource.jndi-name}")
  private String jndiName;
 
  @Value("${schema.name}")
  private String schemaName;

  @Value("${package.name}")
  private String packageName;

  @Value("${proc.name}")
  private String procedureName1;

  @Value("${proc.name}")
  private String procedureName2;
  
  @Bean
  public LocalContainerEntityManagerFactoryBean dartDistrictEntityManager() throws NamingException {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dartDataSource());
    em.setPackagesToScan("moorthi.test.entity");
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);
    return em;
  }
  
  @Bean
  @ConfigurationProperties(prefix="spring.dart-datasource")
  public DataSource dartDataSource() throws NamingException {
    DataSource dataSource = null;
    Context context = new InitialContext();
    dataSource = (DataSource) context.lookup(jndiName);
    return dataSource;
  }

  @Bean(name="searchJdbcCall1")
  public SimpleJdbcCall searchJdbcCall1() throws NamingException {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dartDataSource());
    SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withProcedureName(procedureName1).withSchemaName(schemaName)
        .withCatalogName(packageName);
    return simpleJdbcCall;
  }

  @Bean(name="searchJdbcCall2")
  public SimpleJdbcCall searchJdbcCall2() throws NamingException {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dartDataSource());
    SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withProcedureName(procedureName2).withSchemaName(schemaName)
        .withCatalogName(packageName);
    return simpleJdbcCall;
  }
}
