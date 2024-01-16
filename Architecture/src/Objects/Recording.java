package Objects;

import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class Recording extends BasicObject{
	
	
	private  File recordingFile;
	private List<File>soundFiles=new  ArrayList<File>();
	private List <Integer> SL=new  ArrayList<Integer>();
	private List <String[]>WL=new ArrayList<String[]>();
	
	
	public Recording(){};
	
//	public Recording(String id, File file, int expectedScore,
//			String[] wordsScores, long recordingLength) {
////		super();
//		this.id=id;
//		this.recordingFile = file;
//		this.SL = expectedScore;
//		this.WL = wordsScores;
//	
//	}
	
	
	public List <File> getFiles() {
		return soundFiles;
	}
	public void setRecordingFiles(List<File>files) {
		this.soundFiles = files;
	}
	public List <Integer> getSL() {
		return SL;
	}
	public void setSL(List <Integer> expectedScore) {
		this.SL = expectedScore;
	}
	public List <String[]> getWL() {
		return WL;
	}
	public void setWL(List <String[]> wordsScores) {
		this.WL = wordsScores;
	}
	

}
