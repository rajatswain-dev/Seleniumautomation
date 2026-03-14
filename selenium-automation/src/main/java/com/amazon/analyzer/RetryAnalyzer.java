package com.amazon.analyzer;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.amazon.config.Config;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Rajat
 * @Desc:   Retries failed tests up to the configured retry limit
 */

@Slf4j
public class RetryAnalyzer implements IRetryAnalyzer {

	private int count      = 0;
	private int retryLimit = 0;

	@Override
	public boolean retry(ITestResult result) {

		if (retryLimit == 0) {
			retryLimit = Integer.parseInt(
				Config.get(Config.ConfigProperty.RETRY_LIMIT)
			);
		}

		if (count < retryLimit) {
			count++;
			log.info("Retrying test: {} — attempt {}/{}",
				result.getName(), count, retryLimit);
			return true;
		}

		count = 0;   // ✅ reset to 0 — next test starts fresh
		return false;

	}

}
