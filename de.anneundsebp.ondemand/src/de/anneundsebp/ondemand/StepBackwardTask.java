package de.anneundsebp.ondemand;

import de.anneundsebp.ondemand.parser.Processor;
import de.anneundsebp.ondemand.parser.Step;
import android.os.AsyncTask;

public class StepBackwardTask extends AsyncTask<Processor, Integer, Step> {
	
	StepHandler stepHandler;

	public StepBackwardTask(StepHandler stepHandler) {
		this.stepHandler = stepHandler;
	}
	
	@Override
	protected Step doInBackground(Processor... params) {
		return params[0].backward();
	}
	
	@Override
	protected void onPostExecute(Step result) {
		super.onPostExecute(result);
		stepHandler.stepUpdate(result);
	}


}
