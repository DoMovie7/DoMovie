package com.red.domovie.ssh;

import java.util.Random;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;

@Profile("ssh")
@Configuration
@RequiredArgsConstructor
public class SSHTunnellingConfig {

	private final ApplicationContext application;
	private final SSHTunnellingProperties sshTunnellingProperties;
	private final DataSourceProperties dataSourceProperties;
	private final Environment env;

	@Bean
	HikariConfig hikariConfig() {
		HikariConfig config = new HikariConfig();
		config.setPoolName(env.getProperty("spring.datasource.hikari.pool-name"));
		config.setMinimumIdle(env.getProperty("spring.datasource.hikari.minimum-idle", Integer.class, 20));
		config.setMaximumPoolSize(env.getProperty("spring.datasource.hikari.maximum-pool-size", Integer.class, 150));
		config.setIdleTimeout(env.getProperty("spring.datasource.hikari.idle-timeout", Long.class, 1800000L));
		config.setMaxLifetime(env.getProperty("spring.datasource.hikari.max-lifetime", Long.class, 1800000L));
		config.setConnectionTimeout(env.getProperty("spring.datasource.hikari.connection-timeout", Long.class, 90000L));
		return config;
	}

	@Bean
	org.apache.ibatis.session.Configuration mybatisConfiguration() {
		org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
		config.setMapUnderscoreToCamelCase(
				env.getProperty("mybatis.configuration.map-underscore-to-camel-case", Boolean.class, true));
		// 필요한 다른 MyBatis 설정을 여기에 추가
		return config;
	}

	@Primary
	@Bean
	DataSource dataSource() throws JSchException {
		JSch jsch = new JSch();
		jsch.addIdentity(sshTunnellingProperties.getPrivateKey());

		Session session = jsch.getSession(sshTunnellingProperties.getUsername(), sshTunnellingProperties.getSshHost(),
				sshTunnellingProperties.getSshPort());
		session.setConfig("StrictHostKeyChecking", "no");
		session.connect();

		int lport = new Random().nextInt(999) + 33001;

		int localPort = session.setPortForwardingL(lport, sshTunnellingProperties.getRdsHost(),
				sshTunnellingProperties.getRdsPort());

		System.out.println("localPort:" + localPort);

		HikariConfig config = hikariConfig();
		config.setJdbcUrl(dataSourceProperties.getUrl().replace("[LOCAL_PORT]", String.valueOf(localPort)));
		config.setDriverClassName(dataSourceProperties.getDriverClassName());
		config.setUsername(dataSourceProperties.getUsername());
		config.setPassword(dataSourceProperties.getPassword());
		return new HikariDataSource(config);
	}

	@Bean
	SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setConfiguration(mybatisConfiguration());

		String locationPattern = "classpath*:mappers/**/*-mapper.xml";
		Resource[] resources = application.getResources(locationPattern);
		factoryBean.setMapperLocations(resources);

		return factoryBean.getObject();
	}

	@Bean
	SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}