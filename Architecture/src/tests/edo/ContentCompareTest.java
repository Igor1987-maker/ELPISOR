package tests.edo;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



import org.junit.Test;



import tests.BasicTests.ContentCompareBasicTest;

public class ContentCompareTest extends ContentCompareBasicTest {

	private String oldContentBaseFolder = "\\\\frontqa3\\EDO_HTML_QA\\Runtime\\Content\\";
	List<String[]> testResults = new ArrayList<String[]>();// folder
	// id,segment
	// id,status,explanation
	// private String changedFileInCFL1 =
	// "\\\\NEWSTORAGE\\Sendhere\\omers\\contentFiles\\";
	private String CFL1BaseFolder = "\\\\NEWSTORAGE\\Sendhere\\omers\\contentFiles\\";
	// private String newContentBaseFolder =
	// "\\\\frontdev2003\\EDOPedagogical\\EDO\\Runtime\\Content\\";
	// // 17.11.2014 test
	// private String newContentBaseFolder =
	// "\\\\storage\\storage\\MARKETING\\Production\\EDO Facelift\\updated Package\\Runtime\\Content\\";
	private String newContentBaseFolder = "\\\\STORAGE\\storage\\MARKETING\\Production\\EDO Facelift\\Package 23-11\\Runtime\\Content\\";
	private int numberOfSegmentsNotTheSame;
	private int wordsMisMatch;
	private int numberOfWordsInSubSegmentsNotTheSame;
	private int passedTests;

	boolean copyFolder = false;
	boolean CFL1diff = false;

	@Test
	public void testFolderReading() throws Exception {
		compareOldAndNewContent("reading", true);
	}

	@Test
	public void testFolderSpeaking() throws Exception {
		compareOldAndNewContent("speaking", true);
	}

	@Test
	public void testFolderListening() throws Exception {
		compareOldAndNewContent("listening", true);
	}

	@Test
	public void testFolderGrammer() throws Exception {
		compareOldAndNewContent("grammar", true);
	}

	@Test
	public void deleteUpdatedContant() throws Exception {
		List<String[]> filesToDelete = new ArrayList<String[]>();
		// get list of updated files from D:\Content\Explore\English
		String baseFolder = "C:\\Users\\omers\\Desktop\\Nov30\\GrammarExplore";
		String baseFolderPath = "D:\\ChangedFromOrig";
		String baseSetHidden = "C:\\Users\\omers\\Desktop\\Nov30\\ToSetHidden\\GrammerExplore";
		List<String> changedFiles = getFilesInFolder(baseFolderPath, 6);
		// loop on every language folder in D:\Content\Explore except english
		List<String> langFolders = getSubFoldersSimple(baseFolder);
		langFolders.remove("English");
		langFolders.remove("English old");
		langFolders.remove("English_b4_OmerTools");
		// check if file exist in list - if true - delete it and keep its name
		for (int i = 0; i < langFolders.size(); i++) {
			// TODO create folder under baseSetHiddent

			File newDir = new File(baseSetHidden + "\\" + langFolders.get(i));
			if (!newDir.exists()) {
				newDir.mkdir();
			}
			String langFolder = baseFolder + "\\" + langFolders.get(i);
			List<String> langFiles = getFilesInFolder(langFolder, 0);
			for (int j = 0; j < langFiles.size(); j++) {
				if (changedFiles.contains(langFiles.get(j).substring(0, 6))) {
					// System.out.println("File found! " + baseFolder + "\\"
					// + langFolders.get(i) + "\\" + langFiles.get(j));
					File file = new File(baseFolder + "\\" + langFolders.get(i)
							+ "\\" + langFiles.get(j));
					// TODO copy file
					textService.copyFileToFolder(baseFolder + "\\" + langFolders.get(i)+ "\\" + langFiles.get(j), newDir.getAbsolutePath());
					filesToDelete.add(new String[] { baseFolder + "\\"
							+ langFolders.get(i) + "\\" + langFiles.get(j) });
				} else {
					System.out.println("File not deleted");
				}
			}
		}
		System.out.println(filesToDelete.size());
		textService.writeArrayistToCSVFile("files/temp/filesToDelete.csv",
				filesToDelete);
	}

	public void compareOldAndNewContent(String folderName, boolean userCodesList)
			throws Exception {
		String contentFolder = folderName;
		// get old content subfolders into arrayList
		oldContentBaseFolder = oldContentBaseFolder + contentFolder;
		newContentBaseFolder = newContentBaseFolder + contentFolder;
		CFL1BaseFolder = CFL1BaseFolder + contentFolder;

		List<String> oldContentFolders = getSubFolders(oldContentBaseFolder,
				false, true);

		List<String> cfl1ContentFolders = getSubFolders(CFL1BaseFolder, false,
				false);

		// List<String> cfl1ContentFolders =null;
		// get new content subfolsers into arrayList
		List<String> newContentFolders = getSubFolders(newContentBaseFolder,
				false, true);

		int foldersPassed = 0;
		int foldersFailed = 0;
		outerloop: for (int i = 0; i < newContentFolders.size(); i++) {
			// CFL1diff=false;
			// boolean fileExistInCFL1 = false;
			boolean folderPassed = true;
			try {

				report.report("Checking folder: "
						+ newContentFolders.get(i));
				String newContenetfilePath = newContentBaseFolder + "\\"
						+ newContentFolders.get(i) + "\\"
						+ newContentFolders.get(i) + ".js";

				String oldContenetfilePath = oldContentBaseFolder + "\\"
						+ newContentFolders.get(i) + "\\"
						+ newContentFolders.get(i) + ".js";

				// String cfl1ContentFilePath = null;

				// check if file exist in CFL content folders:
				// \\NEWSTORAGE\Sendhere\omers\contentFiles
				// String[] cfl1Contentsegments = null;
				// if (textService.checkIfFileExist(CFL1BaseFolder + "\\"
				// + newContentFolders.get(i) + ".js") == true) {
				//
				// System.out.println("File " + newContentFolders.get(i)
				// + " found in CFL1");
				// fileExistInCFL1 = true;
				// cfl1ContentFilePath = CFL1BaseFolder + "\\"
				// + newContentFolders.get(i) + ".js";
				//
				// String fileContent = textService.getTextFromFile(
				// cfl1ContentFilePath, Charset.defaultCharset());
				//
				// cfl1Contentsegments = textService
				// .getHtmlElementFromHtmlFile(
				// "//span[@class='segment']", fileContent);
				//
				// cfl1Contentsegments = textService
				// .trimLowerCaseAndRemoveChars(cfl1Contentsegments);
				// }
				// if exist, compare CFL1 and CFL2

				boolean fileExist = textService
						.checkIfFileExist(newContenetfilePath);
				String newfileContent = textService.getTextFromFile(
						newContenetfilePath, Charset.defaultCharset());

				String currentFileContent = textService.getTextFromFile(
						oldContenetfilePath, Charset.defaultCharset());

				String[] newContentsegments = textService
						.getHtmlElementFromHtmlFile("//span[@class='segment']",
								newfileContent);
				newContentsegments = textService
						.trimLowerCaseAndRemoveChars(newContentsegments);

				String[] currentContentsegments = textService
						.getHtmlElementFromHtmlFile("//span[@class='segment']",
								currentFileContent);
				currentContentsegments = textService
						.trimLowerCaseAndRemoveChars(currentContentsegments);

				// if (fileExistInCFL1) {
				// compareSegments(cfl1Contentsegments, newContentsegments,
				// newContentFolders, cfl1ContentFolders, i);
				// if (CFL1diff == true) {
				//
				// continue;
				// }
				// // copy file to CFL1 folder
				// }
				// check number of segments
				if (testResultService.assertEquals(newContentsegments.length,
						currentContentsegments.length,
						"Length of segments is not the same")) {
					// compare content of segments
					for (int j = 0; j < newContentsegments[j].length(); j++) {
						// split segment into array
						String[] newSegmentWords = textService
								.splitStringToArray(newContentsegments[j],
										"\\s+");
						String[] currentSegmentWords = textService
								.splitStringToArray(currentContentsegments[j],
										"\\s+");

						if (testResultService.assertEquals(
								newSegmentWords.length,
								currentSegmentWords.length,
								"Number of words in segment text mismatch. ")) {
							// compare text of sub-segments
							boolean wordsMatch = true;
							String newWord = null;
							String currentWord = null;
							for (int k = 0; k < currentSegmentWords.length; k++) {

								if (!currentSegmentWords[k]
										.equals(newSegmentWords[k])) {
									wordsMatch = false;
									newWord = newSegmentWords[k];
									currentWord = currentSegmentWords[k];
								}

							}
							if (wordsMatch == true) {
								// add folder and sub segment to results list
								String[] str = new String[] {
										newContentFolders.get(i),
										String.valueOf(j), "Passed" };

								passedTests++;
								testResults.add(str);
							} else {
								folderPassed = false;
								String[] str = new String[] {
										newContentFolders.get(i),
										String.valueOf(j),
										"Failed",
										"Word mismatch",
										"word in new content is: "
												+ newWord
												+ "; while word in current content is: "
												+ currentWord };
								wordsMisMatch++;
								testResults.add(str);
								textService.copyFileToFolder(
										newContentBaseFolder + "\\"
												+ newContentFolders.get(i)
												+ "\\"
												+ newContentFolders.get(i)
												+ ".js",
										"files/temp/ChangedFromOrig");
								continue;
							}

						} else {
							folderPassed = false;
							// add folder name, and sub segment number to test
							// results
							String[] str = new String[] {

									newContentFolders.get(i),
									String.valueOf(j),
									"Failed",
									"Different number of words in sub-segment",
									"Number of words in new content sub-segment("
											+ newSegmentWords.length
											+ ") is not the same as number of words in current text("
											+ currentSegmentWords.length
											+ ").Text in new content is: "
											+ textService.printStringArray(
													newSegmentWords, " ")
											+ "; while text in current segment is: "
											+ textService.printStringArray(
													currentSegmentWords, " ") };
							numberOfWordsInSubSegmentsNotTheSame++;
							testResults.add(str);
							textService.copyFileToFolder(newContentBaseFolder
									+ "\\" + newContentFolders.get(i) + "\\"
									+ newContentFolders.get(i) + ".js",
									"files/temp/ChangedFromOrig");
							continue outerloop;

						}
						;

					}
				} else {
					folderPassed = false;
					String[] str = new String[] {
							newContentFolders.get(i),
							null,
							"Failed",
							"Different number of segments in file",
							"Number of segments in new content is: "
									+ newContentsegments.length
									+ "; while number of segment in current content is: "
									+ currentContentsegments.length };
					numberOfSegmentsNotTheSame++;
					testResults.add(str);
					textService.copyFileToFolder(newContentBaseFolder + "\\"
							+ newContentFolders.get(i) + "\\"
							+ newContentFolders.get(i) + ".js",
							"files/temp/ChangedFromOrig");
					continue outerloop;

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Problem found while checking folder: "
						+ newContentFolders.get(i));
				System.out.println(e.toString());
				testResultService
						.addFailTest("Problem found while checking folder: "
								+ newContentFolders.get(i) + " " + e.toString());
			}

			// if (copyFolder) {
			// textService.copyFileToFolder(
			// newContentBaseFolder + "\\" + newContentFolders.get(i)
			// + "\\" + newContentFolders.get(i) + ".js",
			// "files/temp/ChangedFromOrig");
			// }

			if (folderPassed == true) {
				textService.copyFileToFolder(
						newContentBaseFolder + "\\" + newContentFolders.get(i)
								+ "\\" + newContentFolders.get(i) + ".js",
						"files/temp/noChange");
			}

		}
		testResults.add(new String[] { "Folder scanned:"
				+ newContentFolders.size() });
		testResults.add(new String[] { "Passed sub-segments:" + passedTests });
		testResults
				.add(new String[] { "Files with diffrent number of segments:"
						+ numberOfSegmentsNotTheSame });
		testResults
				.add(new String[] { "Sub-segment with diffrent number of words:"
						+ numberOfWordsInSubSegmentsNotTheSame });
		testResults.add(new String[] { "Words mismatch:" + wordsMisMatch });

		textService.writeArrayistToCSVFile("files/csvFiles/compareResults"
				+ folderName + ".csv", testResults);
	}

	public void compareSegments(String[] segmentOld, String[] segmentNew,
			List<String> newContentFolders, List<String> oldContentFolders,
			int i) throws Exception {
		if (testResultService.assertEquals(segmentNew.length,
				segmentOld.length, "Length of segments is not the same")) {
			// compare content of segments
			for (int j = 0; j < segmentNew[j].length(); j++) {
				// split segment into array
				String[] newSegmentWords = textService.splitStringToArray(
						segmentNew[j], "\\s+");
				String[] currentSegmentWords = textService.splitStringToArray(
						segmentOld[j], "\\s+");

				if (testResultService.assertEquals(newSegmentWords.length,
						currentSegmentWords.length,
						"Number of words in segment text mismatch. ")) {
					// compare text of sub-segments
					boolean wordsMatch = true;
					String newWord = null;
					String currentWord = null;
					for (int k = 0; k < currentSegmentWords.length; k++) {

						if (!currentSegmentWords[k].equals(newSegmentWords[k])) {
							wordsMatch = false;
							newWord = newSegmentWords[k];
							currentWord = currentSegmentWords[k];
						}

					}
					if (wordsMatch == true) {
						// add folder and sub segment to results list
						String[] str = new String[] { newContentFolders.get(i),
								String.valueOf(j), "Passed" };
						passedTests++;
						// copy to noChangeFolder
						testResults.add(str);
					} else {
						String[] str = new String[] {
								newContentFolders.get(i),
								String.valueOf(j),
								"Failed",
								"CFL1",
								"Word mismatch ",
								"word in new content is: "
										+ newWord
										+ "; while word in current content is: "
										+ currentWord };
						wordsMisMatch++;
						testResults.add(str);
						copyFolder = true;
						CFL1diff = true;
					}

				} else {
					// add folder name, and sub segment number to test
					// results
					String[] str = new String[] {
							newContentFolders.get(i),
							String.valueOf(j),
							"Failed",
							"Different number of words in sub-segment ",
							"CFL1",
							"Number of words in new content sub-segment("
									+ newSegmentWords.length
									+ ") is not the same as number of words in current text("
									+ currentSegmentWords.length
									+ ").Text in new content is: "
									+ textService.printStringArray(
											newSegmentWords, " ")
									+ "; while text in current segment is: "
									+ textService.printStringArray(
											currentSegmentWords, " ") };
					numberOfWordsInSubSegmentsNotTheSame++;
					testResults.add(str);
					copyFolder = true;
					CFL1diff = true;

				}
				;

			}
		} else {
			String[] str = new String[] {
					newContentFolders.get(i),
					null,
					"Failed",
					"Different number of segments in file ",
					"CFL1",
					"Number of segments in new content is: "
							+ segmentNew.length
							+ "; while number of segment in current content is: "
							+ segmentOld.length };
			numberOfSegmentsNotTheSame++;
			testResults.add(str);
			copyFolder = true;
			CFL1diff = true;

		}

		// if (copyFolder) {
		// textService.copyFileToFolder(
		// newContentBaseFolder + "\\" + newContentFolders.get(i)
		// + "\\" + newContentFolders.get(i) + ".js",
		// "files/temp/ChangedFromCFL1");
		// }
	}
}
