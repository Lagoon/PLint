package utils;

import groovy.lang.Singleton;

import java.util.HashMap;
import java.util.Map;

import play.Play;

public class LintConf {

	/**
	 * Lint Version
	 */
	public static final String VERSION = "0.1alpha11";

	/**
	 * Lint Configuration properties
	 */
	public static final String PROTOCOL = Play.configuration.getProperty("lint.protocol", "http");
	public static final String BASEURL=Play.configuration.getProperty("lint.baseUrl", null);
	public static final String PORT = Play.configuration.getProperty("lint.port", null);
	public static final String LOGIN = Play.configuration.getProperty("lint.login", "guest").trim();
	public static final String PASSWORD = Play.configuration.getProperty("lint.password", "guest").trim();

	/**
	 * This is used as default content type related to the web: request, response
	 */
	public static final String CONTENT_TYPE = "json";

	/**
	 * Computed common base url
	 */
	public static final String URL = PROTOCOL + "://" + BASEURL + ":" + PORT + "/api/";

	/**
	 * Lint Arguments
	 */
	protected static LintArgs lintArgs = null;

	/**
	 * Lint args
	 */
	@Singleton
	public static class LintArgs {

		public Map<String, Object> data = new HashMap<String, Object>();        // ThreadLocal access
		public static ThreadLocal<LintArgs> current = new ThreadLocal<LintArgs>();

		public static LintArgs current() {
			return current.get();
		}

		public void put(String key, Object arg) {
			data.put(key, arg);
		}

		public Object get(String key) {
			return data.get(key);
		}

		@SuppressWarnings("unchecked")
		public <T> T get(String key, Class<T> clazz) {
			return (T) this.get(key);
		}

		@Override
		public String toString() {
			return data.toString();
		}
	}

}
