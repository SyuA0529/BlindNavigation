package com.dku.blindnavigation.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class AudioRecordHelper {

    private static final String TAG = "AudioRecordHelper";

    private static final String FILE_NAME = "BlindNavigationVoice";
    private static final String RAW_EXT = ".raw";
    private static final String WAV_EXT = ".wav";

    private final int bpp = 16;
    private final int sampleRate = 44100;
    private static final int CHANNEL = AudioFormat.CHANNEL_IN_STEREO;
    private static final int ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord record = null;
    private final int bufferSize;
    private Thread recordingThread;
    private boolean isRecording = false;
    private final Application mApplication;

    public AudioRecordHelper(Application application) {
        bufferSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        mApplication = application;
    }

    public void onRecord() {
        if (Objects.isNull(recordingThread)) {
            startRecording();
            return;
        }

        stopRecording();
        writeWavFile();
    }

    public boolean hasRecordedData() {
        File recordedFile = new File(getFilePath(false));
        return recordedFile.exists() && recordedFile.canRead() && recordedFile.canWrite();
    }

    public byte[] getRecordedData() {
        File recordedFile = new File(getFilePath(false));
        byte[] bytes = new byte[(int) recordedFile.length()];
        try (FileInputStream fis = new FileInputStream(recordedFile)) {
            fis.read(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        recordedFile.delete();
        return bytes;
    }

    private void stopRecording() {
        isRecording = false;
        record.stop();
        record.release();
        recordingThread = null;
    }

    @SuppressLint("MissingPermission")
    private void startRecording() {
        record = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, CHANNEL, ENCODING, bufferSize);
        isRecording = true;
        record.startRecording();
        recordingThread = new Thread(this::writeRawData);
        recordingThread.start();
    }

    private String getFilePath(boolean isRaw) {
        String fileName = FILE_NAME + (isRaw ? RAW_EXT : WAV_EXT);
        return new File(ContextCompat.getExternalCacheDirs(mApplication)[0], fileName).getPath();
    }

    private void writeRawData() {
        byte[] readData = new byte[bufferSize];
        try (FileOutputStream fos = new FileOutputStream(getFilePath(true))) {
            while (isRecording) {
                if (record.read(readData, 0, bufferSize) == AudioRecord.ERROR_INVALID_OPERATION) {
                    continue;
                }
                fos.write(readData);
            }
        } catch (IOException exception) {
            Log.d(TAG, "writeRawData: Error");
            exception.printStackTrace();
        }
    }

    private void writeWavFile() {
        try (FileInputStream fis = new FileInputStream(getFilePath(true));
             FileOutputStream fos = new FileOutputStream(getFilePath(false))) {
            byte[] data = new byte[bufferSize];
            int channels = 2;
            long byteRate = bpp * sampleRate * channels / 8;
            long totalAudioLen = fis.getChannel().size();
            long totalDataLen = totalAudioLen + 36;
            writeWavHeader(fos, totalAudioLen, totalDataLen, channels, byteRate);
            while (fis.read(data) != -1) {
                fos.write(data);
            }
        } catch (IOException exception) {
            Log.d(TAG, "writeWavFile: Error");
            exception.printStackTrace();
        }
    }

    private void writeWavHeader(FileOutputStream fos, long totalAudioLen, long totalDataLen, int channels, long byteRate) {
        try {
            byte[] header = new byte[44];
            header[0] = 'R'; // RIFF/WAVE header
            header[1] = 'I';
            header[2] = 'F';
            header[3] = 'F';
            header[4] = (byte) (totalDataLen & 0xff);
            header[5] = (byte) ((totalDataLen >> 8) & 0xff);
            header[6] = (byte) ((totalDataLen >> 16) & 0xff);
            header[7] = (byte) ((totalDataLen >> 24) & 0xff);
            header[8] = 'W';
            header[9] = 'A';
            header[10] = 'V';
            header[11] = 'E';
            header[12] = 'f'; // 'fmt ' chunk
            header[13] = 'm';
            header[14] = 't';
            header[15] = ' ';
            header[16] = 16; // 4 bytes: size of 'fmt ' chunk
            header[17] = 0;
            header[18] = 0;
            header[19] = 0;
            header[20] = 1; // format = 1
            header[21] = 0;
            header[22] = (byte) channels;
            header[23] = 0;
            header[24] = (byte) ((long) sampleRate & 0xff);
            header[25] = (byte) (((long) sampleRate >> 8) & 0xff);
            header[26] = (byte) (((long) sampleRate >> 16) & 0xff);
            header[27] = (byte) (((long) sampleRate >> 24) & 0xff);
            header[28] = (byte) (byteRate & 0xff);
            header[29] = (byte) ((byteRate >> 8) & 0xff);
            header[30] = (byte) ((byteRate >> 16) & 0xff);
            header[31] = (byte) ((byteRate >> 24) & 0xff);
            header[32] = (byte) (2 * 16 / 8); // block align
            header[33] = 0;
            header[34] = bpp; // bits per sample
            header[35] = 0;
            header[36] = 'd';
            header[37] = 'a';
            header[38] = 't';
            header[39] = 'a';
            header[40] = (byte) (totalAudioLen & 0xff);
            header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
            header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
            header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
            fos.write(header, 0, 44);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
