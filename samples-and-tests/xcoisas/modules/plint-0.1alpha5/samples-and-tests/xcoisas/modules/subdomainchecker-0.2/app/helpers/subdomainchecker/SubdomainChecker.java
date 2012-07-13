package helpers.subdomainchecker;

import play.Logger;
import play.Play;
import play.mvc.Http;
import play.mvc.Http.Request;

/**
 * Computes Subdomain on current request
 * 
 * @author dpeixoto
 *
 */
public class SubdomainChecker {

	public static final String SUBDOMAIN = "_subdomain";
	public static final String DEFAULT_SUBDDOMAIN_KEY = "default.subdomain";

	/**
	 * check subdomain based on request and cookie default domain 
	 * 
	 * @param request
	 */
	private static void checkSubdomain(Request request) {

		String subdomain = null;
		String defaultDomain = Http.Cookie.defaultDomain;

		if(defaultDomain == null || defaultDomain.isEmpty() || request.domain == null || request.domain.isEmpty()){
			Logger.warn("Http Cookie defaultDomain or Request domain not defined!");
		}else{
			subdomain = computesSubdomain(request.domain, defaultDomain);
		}
		request.routeArgs.put(SUBDOMAIN, subdomain);
	}

	/*
	 * current Request Subdomain
	 */
	public static String currentSubdomain(Request request){

		if(request != null && !alreadyComputesSubdomain(request)){
			checkSubdomain(request);
		}

		if(request.routeArgs.get(SUBDOMAIN) == null && Play.configuration.containsKey(DEFAULT_SUBDDOMAIN_KEY)){
			request.routeArgs.put(SUBDOMAIN, Play.configuration.getProperty(DEFAULT_SUBDDOMAIN_KEY));
		}
		return request.routeArgs.get(SUBDOMAIN);
	}

	/**
	 * verify if already computes subdomain for request
	 * 
	 * @param request
	 * @return boolean
	 */
	private static boolean alreadyComputesSubdomain(Request request){
		return request.routeArgs.containsKey(SUBDOMAIN);
	}

	/**
	 * computes Subdomain based on request domain and default domain
	 * 
	 * @param domain
	 * @param defaultDomain
	 * @return
	 */
	private static String computesSubdomain(String domain, String defaultDomain){
		String subdomain = null;
		int endIndex = domain.indexOf(defaultDomain);
		if (endIndex <= 0) {
			Logger.warn("Default Domain: \"" + defaultDomain + "\" doesn't exist on Request Domain: \"" + domain + "\"");
		}else{
			subdomain = domain.substring(0, endIndex);
			Logger.debug("Computed Subdomain: " + subdomain);
		}
		return subdomain;
	}
}
