package pl.decerto.hyperon.train.product.example;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Maciej Główka on 23.06.2020
 */
@Getter
@Component
@EqualsAndHashCode
public class DatabaseProperties {
	private final String dialect;
	private final String dbUsername;
	private final String dbPassword;
	private final String dbUrl;

	public DatabaseProperties(@Value("${hyperon.database.dialect}") String dialect,
		@Value("${hyperon.database.username}") String dbUsername,
		@Value("${hyperon.database.password}") String dbPassword,
		@Value("${hyperon.database.url}") String dbUrl) {
		this.dialect = dialect;
		this.dbUsername = dbUsername;
		this.dbPassword = dbPassword;
		this.dbUrl = dbUrl;
	}
}
