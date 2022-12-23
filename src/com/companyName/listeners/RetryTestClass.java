package com.companyName.listeners;

import com.companyName.utils.CommonUtils;
import com.companyName.utils.GetFrameworkKeys;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryTestClass implements IRetryAnalyzer{

	private int retryCnt=0;
	private int maxRetryCnt=Integer.parseInt(GetFrameworkKeys.getPropValue("retryCount"));

	/**
	 * Retry failed test case : method leveel
	 * @param result
	 * @return boolean
	 */
	public boolean retry(ITestResult result) {
		if(retryCnt<maxRetryCnt) {
			CommonUtils.logInfo("Retrying " + result.getName() + " again and the count is " + (retryCnt+1));
			result.setStatus(3);
			retryCnt++;
			return true;
		}
		return false;
	}

}
