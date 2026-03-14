package com.amazon.analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.xml.XmlSuite;

import com.amazon.config.Config;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomTestNGReporter implements IReporter {

	private static final String TEMPLATE_PATH = "custom-email-report/emailable-report-template.html";
	private static final String TARGET_PATH = "target/custom-emailable-report.html";

	private static final String TD_PASS = "<td bgcolor='#81e891'>";
	private static final String TD_SKIP = "<td bgcolor='yellow'>";
	private static final String TD_FAIL = "<td bgcolor='#ff6666'>";
	private static final String TD_CLOSE = "</td>";

	private String suiteName = null;

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {

		try (FileWriter fw = new FileWriter(new File(TARGET_PATH))) {

			String template = readEmailableReportTemplate();
			String reportTitle = getCustomReportTitle("Test Report");
			String suiteSummary = getTestSuiteSummary(suites);

			template = template.replaceAll("\\$Suite name\\$", getSuiteName());
			template = template.replaceAll("\\$TestNG_Custom_Report_Title\\$", reportTitle);
			template = template.replaceAll("\\$Test_Case_Summary\\$", suiteSummary);

			fw.write(template);
			fw.flush();

		} catch (Exception ex) {
			log.error("TestNG report not generated !!", ex);
		}

	}

	private String readEmailableReportTemplate() {
		StringBuilder retBuf = new StringBuilder();

		try (BufferedReader br = new BufferedReader(new FileReader(new File(TEMPLATE_PATH)))) {

			String line;
			while ((line = br.readLine()) != null) {
				retBuf.append(line);
			}

		} catch (Exception ex) {
			log.error("Failed to read email report template", ex); // ✅ log.error
		}

		return retBuf.toString();
	}

	private String getCustomReportTitle(String title) {
		return title + " " + getDateInStringFormat(new Date()); // ✅ no unnecessary StringBuilder
	}

	private String getSuiteName() {
		return suiteName != null ? suiteName : "Unknown Suite"; // ✅ null safe
	}

	private String getTestSuiteSummary(List<ISuite> suites) {
		StringBuilder retBuf = new StringBuilder();

		try {
			for (ISuite tempSuite : suites) {
				suiteName = tempSuite.getName();
				Map<String, ISuiteResult> testResults = tempSuite.getResults();

				for (ISuiteResult result : testResults.values()) {
					ITestContext testObj = result.getTestContext();

					int passed = testObj.getPassedTests().getAllMethods().size();
					int skipped = testObj.getSkippedTests().getAllMethods().size();
					int failed = testObj.getFailedTests().getAllMethods().size();
					int total = passed + skipped + failed;

					String browserType = tempSuite.getParameter("browser");
					String url = Config.get(Config.ConfigProperty.BASE_URL);

					Date startDate = testObj.getStartDate();
					Date endDate = testObj.getEndDate();
					int duration = (int) ((endDate.getTime() - startDate.getTime()) / 60000);

					retBuf.append("<tr align='center'>").append("<td>").append(testObj.getName()).append(TD_CLOSE)
							.append("<td>").append(total).append(TD_CLOSE).append(TD_PASS).append(passed)
							.append(TD_CLOSE).append(TD_SKIP).append(skipped).append(TD_CLOSE).append(TD_FAIL)
							.append(failed).append(TD_CLOSE).append("<td>").append(browserType).append(TD_CLOSE)
							.append("<td>").append(url).append(TD_CLOSE).append("<td>").append(duration).append(" min")
							.append(TD_CLOSE).append("</tr>");
				}
			}

		} catch (Exception ex) {
			log.error("Test suite summary not found !!", ex);
		}

		return retBuf.toString(); // ✅ return outside finally
	}

	private String getDateInStringFormat(Date date) {
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
		return df.format(date != null ? date : new Date());
	}

}