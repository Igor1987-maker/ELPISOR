package tests.BasicTests;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import tests.misc.EdusoftBasicTest;

public class ContentCompareBasicTest extends EdusoftBasicTest {

	protected static final String SPEAKING_FOLDER = "\\\\frontqa3\\EDO_HTML_SR\\Runtime\\Content\\speaking";
	protected static final String LISTENING_FOLDER = "\\\\frontqa3\\EDO_HTML_SR\\Runtime\\Content\\listening";
	protected static final String GRAMMER_FOLDER = "\\\\frontq_a3\\EDO_HTML_SR\\Runtime\\Content\\grammar";
	// private static final String Vocabulary_FOLDER =
	// "\\\\frontqa3\\EDO_HTML_SR\\Runtime\\Content\\Vocabulary";
	protected static final String Vocabulary_FOLDER = "\\\\newstorage\\sendhere\\_EDOHTML\\EDO_GRAMMAR_CHANGES\\BaseCourses\\Alphabet";
	protected static final String tempTestFile = "C:\\automation\\vocabulary.grammar";

	// private final String grammerFilesPath =
	// "\\\\NEWSTORAGE\\Sendhere\\_EDOHTML\\EduSpeak\\sample-grammars\\English\\baseEDOCources.grammar";
	protected final String grammerFilesPath = "\\\\NEWSTORAGE\\Sendhere\\_EDOHTML\\SR_grammars\\OnlyExcel\\UnitedBaseEDO\\speakingBaseEDO.grammar";
	// private final String grammerFilesPath="C:\\automation\\testFile.txt";

	protected List<String[]> results = new ArrayList<>();

	public void compareAllGrammers(String testFolder, String csvFile,
			String grammarFileCFL2) throws Exception {
		startStep("Iterate on all Speakeing folders");

		// testFolder = testFolder;
		int passed = 0;
		int failed = 0;
		// \\frontqa3\EDO_HTML_SR\Runtime\Content\speaking
		List<String> folders = getSubFolders(testFolder, "files/csvFIles/"
				+ csvFile, true);

		startStep("For all folder, search for js file, get its contend and compare texts to grammer file according the grammer id");
		String filePath = null;
		try {
			outerloop: for (int i = 0; i < folders.size(); i++) {
				System.out.println("Checking folder:" + folders.get(i));

				startStep("check if js file exist");
				// if testing vocabalary files
				if (testFolder == Vocabulary_FOLDER) {
					filePath = testFolder + "\\" + folders.get(i) + "\\"
							+ folders.get(i) + ".xml";
				} else {
					filePath = testFolder + "\\" + folders.get(i) + "\\"
							+ folders.get(i) + ".js";
				}
				boolean fileExist = textService.checkIfFileExist(filePath);

				testResultService.assertEquals(true, fileExist, "File: "
						+ folders.get(i) + " was not found");
				if (fileExist == false) {
					String[] str = new String[] { "File not found",
							folders.get(i), "File " + filePath + " Not Found" };
					results.add(str);
					failed++;
					continue;
				}
				String fileContent = textService.getTextFromFile(filePath,
						Charset.defaultCharset());
				String[] segments = textService.getHtmlElementFromHtmlFile(
						"//span[@class='segment']", fileContent);
				segments = textService.trimLowerCaseAndRemoveChars(segments);

				startStep("Compare each segment with the text from grammer file");
				String[] textFromGrammer = new String[segments.length];
				innerloop: for (int j = 0; j < segments.length; j++) {
					int index = j + 1;
					String grammerText = getGrammerTextFromGrammerFiles("."
							+ folders.get(i).toUpperCase() + "_" + index,
							grammarFileCFL2);
					if (grammerText == null) {
						report.report("Grammer text not found in grammer file:"
								+ folders.get(i).toUpperCase() + "_" + index);

						failed++;
						continue innerloop;
					} else {
						textFromGrammer[j] = grammerText;
					}

				}

				startStep("split sentence to string arrays and compare");
				for (int k = 0; k < segments.length; k++) {
					String[] segmentsWords = textService.splitStringToArray(
							segments[k], "\\s+");
					if (textFromGrammer[k] != null) {
						String[] grammerWords = textService.splitStringToArray(
								textFromGrammer[k], "\\s+");

						if (testResultService
								.assertEquals(
										true,
										segmentsWords.length == grammerWords.length,
										"number of words is not the same for grammer: "
												+ folders.get(i)
												+ "_"
												+ k
												+ " segment words: "
												+ textService
														.printStringArray(segmentsWords)
												+ " grammer words:"
												+ textService
														.printStringArray(grammerWords)) == false) {
							System.out.println("segment words:");
							report.report("segment words:");

							textService.printStringArray(segmentsWords);
							System.out.println("Grammer words:");
							report.report("grammer words:");
							textService.printStringArray(grammerWords);
							String[] str = new String[] {
									"Error",
									folders.get(i),
									"Missing word count",
									"Grammer words:"
											+ textService
													.printStringArray(grammerWords),
									"Segment words:"
											+ textService
													.printStringArray(segmentsWords) };
							results.add(str);
							failed++;
							continue outerloop;

						}
						for (int h = 0; h < segmentsWords.length; h++) {
							boolean wordMatch = testResultService.assertEquals(
									segmentsWords[h], grammerWords[h],
									"problem in grammer: " + folders.get(i)
											+ "_" + k);
							if (wordMatch == false) {
								String[] str = new String[] { "Word mismatch",
										folders.get(i), segmentsWords[h],
										grammerWords[h] };
								results.add(str);
							}
						}
					}
				}
				System.out.println(i + "Finished checking folder: "
						+ folders.get(0) + ". folder is OK");
				report.report(
						i + "Finished checking folder: " + folders.get(0)
								+ ". folder is OK");
				passed++;
			}
			System.out.println("Passed:" + passed);
			System.out.println("Failed:" + failed);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String csvFilepath = "D:\\Logs\\automationLogs\\csvFiles\\grammerTests\\resultOutput"
				+ testFolder.substring(testFolder.length() - 6,
						testFolder.length()) + "_" + dbService.sig(8) + ".csv";
		textService.writeArrayistToCSVFile(csvFilepath, results);
		System.out.println("csv file path:" + "file:///" + csvFilepath);
	}

	public List<String> getSubFolders(String path) throws Exception {
		return getSubFolders(path, true, false);
	}

	public List<String> getSubFolders(String path, boolean isGrammarTest,
			boolean useCodesList) throws Exception {
		return getSubFolders(path, null, isGrammarTest, useCodesList);
	}

	public List<String> getSubFoldersSimple(String path) {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		List<String> folders = new ArrayList<String>();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isDirectory()) {
				String str = listOfFiles[i].getName();
				folders.add(str);
				System.out.println(str);
			}
		}
		System.out.println(folders.size());
		return folders;
	}

	public List<String> getSubFolders(String path, String csvFilePath,
			boolean isGrammarTest) throws Exception {

		return getSubFolders(path, csvFilePath, isGrammarTest, false);

	}

	public List<String> getSubFolders(String path, String csvFilePath,
			boolean isGrammarTest, boolean useCodesList) throws Exception {
		List<String> folders = null;
		List<String[]> codesList = null;
		if (useCodesList) {
			String codeListCsv = "files/csvFiles/codesList.csv";
			codesList = textService.getStr2dimArrFromCsv(codeListCsv);
			for (int i = 0; i < codesList.size(); i++) {
				// System.out.println(codesList.get(i)[0].length());
				if (codesList.get(i)[0].length() == 7) {

					codesList.get(i)[0] = codesList.get(i)[0].substring(0, 6);
					System.out.println(codesList.get(i)[0]);
				}
			}
		}

		List<String> grammarsFromExcel = null;
		if (csvFilePath != null) {
			grammarsFromExcel = textService.getListFromCsv(csvFilePath, 0);
		}
		try {
			File file = new File(path);
			String[] names = file.list();
			if (names == null) {
				System.out.println("Folder is empty");
				Assert.fail("Test folder is empty");
			}
			folders = new ArrayList<String>();

			for (String name : names) {

				if (isGrammarTest == true) {
					if (new File(path + "\\" + name).isDirectory()
							&& grammarsFromExcel.contains(name.toUpperCase())) {
						System.out.println(name);
						if (name.length() <= 6) {
							folders.add(name);
						}
					}
				} else {
					if (new File(path + "\\" + name).isDirectory()) {

						if (name.length() <= 6) {
							if (useCodesList) {
								// check if content code appear in CSV file
								for (int j = 0; j < codesList.size(); j++) {
									if (codesList.get(j)[0].equals(name)) {
										folders.add(name);
										System.out.println("Folder match:"
												+ name);
									}
								}

							} else {
								folders.add(name);
								System.out.println("Folder added:" + name);
							}

						}
					}
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Path is: " + path);
			System.out.println(" Failed in getSubFolders:" + e.getMessage());
			testResultService.addFailTest("Failed on getSybFolder "
					+ e.getMessage());
			e.printStackTrace();
		}
		return folders;

	}

	public List<String> getFilesInFolder(String folderPath, int charNum)
			throws IOException {
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();
		List<String> files = new ArrayList<String>();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String str = listOfFiles[i].getName();
				if (charNum > 0) {
					str = str.substring(0, charNum);
				}
				files.add(str);
				System.out.println(str);
			}
		}
		System.out.println(files.size());
		return files;

	}

	public String getGrammerTextFromGrammerFiles(String grammerID,
			String grammarFilePath) throws IOException {
		String text = null;
		try {
			text = textService.getLineFromTextFile(new File(grammarFilePath),
					grammerID);
			int begin = text.indexOf("(");
			begin++;
			text = text.substring(begin, text.lastIndexOf(")"));
			text.trim();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			testResultService
					.addFailTest("Problem getting text for grammer in grammer file:"
							+ grammerID);
			String[] str = new String[] { "Missing grammer",

			"Grammer missing: " + grammerID };
			results.add(str);

		}
		return text;
	}

}
