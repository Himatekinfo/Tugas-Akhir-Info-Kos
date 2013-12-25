package eto.riswan.rumahsewa.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import android.os.Environment;
import android.util.Log;

public class FileLogging {
	private static final String fileName = "RumahSewa.log";

	public static void log(String text) {
		File root = Environment.getExternalStorageDirectory();
		File file = new File(root, "eto." + FileLogging.fileName);
		try {
			if (root.canWrite()) {
				FileWriter filewriter = new FileWriter(file, true);
				BufferedWriter out = new BufferedWriter(filewriter);
				out.write(Calendar.getInstance().getTime().toString() + " -- " + text + "\n");
				out.close();
			} else
				Log.e("Application", "Could not write to file.");
		} catch (IOException e) {
			Log.e("Application", "Error writing file. Detail: " + e.getMessage());
		}
	}
}
