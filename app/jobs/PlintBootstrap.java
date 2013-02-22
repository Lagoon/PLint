package jobs;

import java.util.concurrent.TimeoutException;

import lagoon.PlintRobot;
import play.Logger;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import utils.LintConf;
import exceptions.LintException;

@OnApplicationStart
public class PlintBootstrap extends Job {

	@Override
	public void doJob() {
		// Lint Initializer
		if(LintConf.PROTOCOL == null || LintConf.BASEURL == null || LintConf.LOGIN == null || LintConf.PORT == null || LintConf.PROTOCOL == null || LintConf.PASSWORD == null){
			Logger.error("PLint module configuration miss some properties!\nPlease review your configuration file");
			Logger.error("Stopping Application ... ");
			Play.stop();
		}

		Logger.info(LintConf.LOG_PREFIX + "Checking Lagoon Security connection at " + LintConf.URL);

		//sanitycheck
		try {
			PlintRobot.getInstance().checkConnection();
			Logger.info(LintConf.LOG_PREFIX + Play.configuration.getProperty("application.name") + " is now connected to Lagoon Security at " + LintConf.URL + " @ " + LintConf.LOGIN);
		} catch (LintException | TimeoutException e) {
			Logger.error(LintConf.LOG_PREFIX + e.getLocalizedMessage());
		}
	}
}


