package com.amazon.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Config {

	private static final Logger      log             = LoggerFactory.getLogger(Config.class);
	private static final String      USER_PROPERTIES = "amazon-core.properties";
	private static final Properties  properties      = new Properties();

	static {
		try (InputStream input = Config.class
				.getClassLoader()
				.getResourceAsStream(USER_PROPERTIES)) {
			if (input != null) {
				properties.load(input);
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to load: " + USER_PROPERTIES, e);
		}
	}

	public enum ConfigProperty {

		BROWSER          ("browser",          "chrome"),
		RUN_LOCALLY      ("runLocally",       "true"),
		HEADLESS         ("headless",         "false"),
		SELENIUM_HOST    ("seleniumHost",     "localhost"),
		SELENIUM_PORT    ("seleniumPort",     "4444"),
		IMPLICIT_WAIT    ("implicitWait",     "10"),
		PAGE_LOAD_TIMEOUT("pageLoadTimeout",  "60"),
		RETRY_LIMIT      ("retryLimit",       "3"),
		BASE_URL         ("baseUrl",          "https://www.amazon.in");

		private final String name;
		private final String defaultValue;

		ConfigProperty(String name, String defaultValue) {
			this.name         = name;
			this.defaultValue = defaultValue;
		}

		public String getName() {
			return name;
		}

		public String getDefaultValue() {
			return defaultValue;
		}

	}

	public static void initConfig(Map<String, String> suiteParams) {
		if (!properties.isEmpty()) {
			return;                                          // ✅ guard — init once only
		}
		for (ConfigProperty prop : ConfigProperty.values()) {
			String value = suiteParams.get(prop.getName());
			properties.setProperty(
				prop.getName(),
				value != null ? value : prop.getDefaultValue()
			);
		}
	}

	public static String get(ConfigProperty prop) {
		return properties.getProperty(prop.getName(), prop.getDefaultValue());
	}

	public static boolean getBool(ConfigProperty prop) {
		return Boolean.parseBoolean(get(prop));
	}

	public static void printTestConfigValues() {
		log.info("===== Test Configuration =====");
		for (ConfigProperty prop : ConfigProperty.values()) {
			log.info(String.format("%-20s : %s", prop.getName(), get(prop)));
		}
		log.info("==============================");
	}

}