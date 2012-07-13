package jobs;

import java.util.concurrent.TimeoutException;

import lagoon.LintRobot;
import play.Logger;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import utils.LintConf;
import exceptions.LintException;

@OnApplicationStart
public class LintBootstrap extends Job {

	@Override
	public void doJob() {
		// Lint Initializer
		if(LintConf.PROTOCOL == null || LintConf.BASEURL == null || LintConf.LOGIN == null || LintConf.PORT == null || LintConf.PROTOCOL == null || LintConf.PASSWORD == null){
			Logger.error("Lint module configuration miss some properties!\nPlease review your configuration file");
			Logger.error("Stopping Application ... ");
			Play.stop();
		}

		Logger.info("Checking Lint module connection ... ");

		//sanitycheck
		try {
			LintRobot.checkConnection();
		} catch (LintException | TimeoutException e) {
			Logger.error("Lint - ", e.getLocalizedMessage());
		}
	}
}


