package pl.decerto.hyperon.train.product.example;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import pl.decerto.hyperon.runtime.core.HyperonEngine;
import pl.decerto.hyperon.runtime.core.HyperonEngineFactory;
import pl.decerto.hyperon.runtime.profiler.jdbc.proxy.DataSourceProxy;
import pl.decerto.hyperon.runtime.sql.DialectRegistry;
import pl.decerto.hyperon.runtime.sql.DialectTemplate;
import pl.decerto.hyperon.train.product.example.handler.TrainProductHandler;
import pl.decerto.hyperon.train.product.example.handler.TrainProductHyperonHandler;

@SpringBootApplication
public class TrainProductApplication {
	@Bean
	public RouterFunction<ServerResponse> route(TrainProductHyperonHandler trainProductHyperonHandler, TrainProductHandler trainProductHandler) {
		return RouterFunctions
			.route(RequestPredicates.GET("/product/hyperon/v1")
				.and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), request -> trainProductHyperonHandler.getProduct(request, 1))
			.andRoute(RequestPredicates.GET("/product/hyperon/v2")
				.and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), request -> trainProductHyperonHandler.getProduct(request, 2))
			.andRoute(RequestPredicates.GET("/product/hyperon/v3")
				.and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), request -> trainProductHyperonHandler.getProduct(request, 3))
			.andRoute(RequestPredicates.GET("/product/hyperon/v4")
				.and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), request -> trainProductHyperonHandler.getProduct(request, 4))
			.andRoute(RequestPredicates.GET("/product/v1")
				.and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), request -> trainProductHandler.getProduct(request, 1))
			.andRoute(RequestPredicates.GET("/product/v2")
				.and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), request -> trainProductHandler.getProduct(request, 2));
	}

	@Bean
	public DialectRegistry dialectRegistry(DatabaseProperties databaseProperties) {
		DialectRegistry registry = new DialectRegistry();
		registry.setDialect(databaseProperties.getDialect());
		return registry;
	}

	@Bean
	public DialectTemplate dialectTemplate(DialectRegistry dialectRegistry) {
		return dialectRegistry.create();
	}

	@Bean
	@Qualifier("dataSource")
	public DataSource dataSourceProxy(@Qualifier("_dataSource") DataSource dataSource) {
		return new DataSourceProxy(dataSource);
	}

	@Bean
	@Qualifier("_dataSource")
	public DataSource dataSource(DialectTemplate dialectTemplate, DatabaseProperties databaseProperties) {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setUsername(databaseProperties.getDbUsername());
		dataSource.setPassword(databaseProperties.getDbPassword());
		dataSource.setJdbcUrl(databaseProperties.getDbUrl());
		dataSource.setDriverClassName(dialectTemplate.getJdbcDriverClassName());
		return dataSource;
	}

	@Bean(destroyMethod = "destroy")
	public HyperonEngineFactory hyperonEngineFactory(DataSource dataSource) {
		HyperonEngineFactory hyperonEngineFactory = new HyperonEngineFactory();
		hyperonEngineFactory.setDataSource(dataSource);
		hyperonEngineFactory.setDeveloperMode(true);
		hyperonEngineFactory.setUsername("admin");

		return hyperonEngineFactory;
	}

	@Bean
	public HyperonEngine hyperonEngine(HyperonEngineFactory engineFactory) {
		return engineFactory.create();
	}

	public static void main(String[] args) {
		SpringApplication.run(TrainProductApplication.class, args);
	}

}
