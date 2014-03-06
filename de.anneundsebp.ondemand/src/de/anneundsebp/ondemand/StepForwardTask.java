package de.anneundsebp.ondemand;

import de.anneundsebp.ondemand.parser.Category;
import de.anneundsebp.ondemand.parser.Processor;
import de.anneundsebp.ondemand.parser.Step;
import android.os.AsyncTask;

public class StepForwardTask extends AsyncTask<Processor, Integer, Step> {
	
	StepHandler stepHandler;
	Category nextCategory = null;
	

	public StepForwardTask(StepHandler stepHandler) {
		this.stepHandler = stepHandler;
	}
	
	public Category getNextCategory() {
		return nextCategory;
	}

	public void setNextCategory(Category nextCategory) {
		this.nextCategory = nextCategory;
	}
	
	@Override
	protected Step doInBackground(Processor... params) {
		return params[0].forward(nextCategory);
	}
	
	@Override
	protected void onPostExecute(Step result) {
		super.onPostExecute(result);
		stepHandler.stepUpdate(result);
	}


}
