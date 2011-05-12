package com.droidwave.stress.entity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public abstract class Persistable {
	Context context;

	public void load(Context context) {
		this.context = context;
		try {
			FileInputStream fIn = context.openFileInput(getFileName());
			InputStreamReader isr = new InputStreamReader(fIn);

			char[] inputBuffer = new char[1024];
			String readString = "";
			while (isr.read(inputBuffer) > 0) {
				readString += new String(inputBuffer);
			}
			JSONObject jsonObject = new JSONObject(readString);
			load(jsonObject);
		} catch (FileNotFoundException e) {
			try {
				load(new JSONObject());// to load the defaults
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void save() {
		// TODO: 2011-05-01 put this into a separate thread to not block the UI
		JSONObject jsonObject = new JSONObject();
		try {
			save(jsonObject);

			String writeString = jsonObject.toString();

			FileOutputStream fOut = context.openFileOutput(getFileName(),
					Context.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);

			osw.write(writeString);
			osw.flush();
			osw.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected abstract void load(JSONObject jsonObject) throws JSONException;

	protected abstract void save(JSONObject jsonObject) throws JSONException;

	protected abstract String getFileName();

}
