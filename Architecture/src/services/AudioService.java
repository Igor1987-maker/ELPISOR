package services;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;

import java.io.*;
import java.util.List;

import javax.sound.sampled.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Objects.Recording;

@Service("AudioService")
public class AudioService  {

	// *******************************************************************
	// In order for this service to work you must install VB audio virtual cable
	// located here: V:\Automation\3rd party tools\VBCABLEDriver_Pack42b.zip
	// *******************************************************************
	private final int BUFFER_SIZE = 128000;
	private AudioInputStream audioStream;
	private AudioFormat audioFormat;
	private SourceDataLine sourceLine;

	@Autowired
	DbService dbService;

	@Autowired
	TextService textService;
	
	@Autowired services.Reporter reporter;

	long recoredTime = 6000;
	List<Recording> recordings;

	AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
	TargetDataLine targetline;
	
//	public void sendSoundToVirtualMic(File soundFile, float sampleRate){
//		
//	}

	public void sendSoundToVirtualMic(File soundFile, float sampleRate)
			throws Exception {
		try {
//			reporter.startStep("Using file: "+soundFile +" with sample rete: "+sampleRate);
			audioStream = AudioSystem.getAudioInputStream(soundFile);

		} catch (Exception e) {
			e.printStackTrace();

		}
//		reporter.startLevel("Starting to play file: " + soundFile.getPath()
//				+ " to virtual microphone", EnumReportLevel.CurrentPlace);
		reporter.startStep("Playing file: "+soundFile.getPath().toString());
		// audioFormat = audioStream.getFormat();
		if (sampleRate == 0) {
			audioFormat = getAudioFormat();
		} else {
			audioFormat = getAudioFormat(sampleRate);
		}
		reporter.report("Playing the file with sample rate of: "+audioFormat.getSampleRate());

		DataLine.Info infoIn = new DataLine.Info(SourceDataLine.class,
				audioFormat);

		try {
			Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
			Mixer mixer = null;
			for (int i = 0; i < mixerInfos.length; i++) {
				reporter.report(mixerInfos[i].getName());
				if (mixerInfos[i].getName().equals(
						"CABLE Input (VB-Audio Virtual Cable)")) {
					mixer = AudioSystem.getMixer(mixerInfos[i]);
					break;
				}
			}
			sourceLine = (SourceDataLine) mixer.getLine(infoIn);
			sourceLine.open(audioFormat);
			reporter.report("Started playing the file."+System.currentTimeMillis());
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			// System.exit(1);
		}
		sourceLine.start();
		int nBytesRead = 0;
		byte[] abData = new byte[BUFFER_SIZE];
		while (nBytesRead != -1) {
			try {
				nBytesRead = audioStream.read(abData, 0, abData.length);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (nBytesRead >= 0) {
				@SuppressWarnings("unused")
				int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
			}
		}
		sourceLine.drain();
		sourceLine.close();
		reporter.report("finished playing the file."+System.currentTimeMillis());
//		report.stopLevel();

	}

	public void recoredSound() {

	}

	private AudioFormat getAudioFormat() {
		return getAudioFormat(44100.0F);
	}

	private AudioFormat getAudioFormat(float sampleRate) {
		// float sampleRate = 44100.0F;
		// 8000,11025,16000,22050,44100
		//8000 for recordings with SRI tool!
		int sampleSizeInBits = 16;
		//16 for my recordings!!!!
		// 8,16
		int channels = 1;
		// 1,2
		boolean signed = true;
		//true for my recordings
		// true,false
		boolean bigEndian = false;
		// true,false
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
				bigEndian);

		// return new
		// AudioFormat(javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED,
		// sampleRate, sampleSizeInBits, channels, fr, f, bigEndian)
	}// end getAudioFormat

}
