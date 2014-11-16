package com.karmelos.kpoll.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.karmelos.kpoll.model.InterestArea;
import com.karmelos.kpoll.model.PollOwner;
import com.karmelos.kpoll.model.PollSurvey;

public class ServletUtil {
	private static final String GOOGLE_SERVER_KEY = "AIzaSyClIxHxErCXCIwzvISTUIQRokhxsBA_ZQI";
	private static final String API_KEY = "AIzaSyClIxHxErCXCIwzvISTUIQRokhxsBA_ZQI";

	public static String post(PollContent content) {
		String serverResponse = null;
		try {

			// 1. URL
			URL url = new URL("https://android.googleapis.com/gcm/send");

			// 2. Open connection
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			// 3. Specify POST method
			conn.setRequestMethod("POST");

			// 4. Set the headers
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "key=" + GOOGLE_SERVER_KEY);

			conn.setDoOutput(true);

			// 5. Add JSON data into POST request body
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String jsonCon = gson.toJson(content);
			// System.out.println(gson.toJson(content));

			// 5.2 Get connection output stream
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

			wr.write(jsonCon.getBytes());

			// 5.4 Send the request
			wr.flush();

			// 5.5 close
			wr.close();

			// 6. Get the response
			int responseCode = conn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				System.out.println("\nSending 'POST' request to URL : " + url);
				System.out.println("Response Code : " + responseCode);

				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				// 7. Print result
				System.out.println(response.toString());
				serverResponse = response.toString();
			} else {
				serverResponse = "error";
			}

		} catch (MalformedURLException e) {
			serverResponse = "error";
		} catch (IOException e) {
			serverResponse = "error";
		}

		return serverResponse;
	}

	public static byte[] generateSalt() {
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[30];
		random.nextBytes(bytes);
		return bytes;
	}

	public static String bytetoString(byte[] input) {
		return org.apache.commons.codec.binary.Base64.encodeBase64String(input);
	}


public static byte[] serialize(Object obj) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ObjectOutputStream os = new ObjectOutputStream(out);
    os.writeObject(obj);
    return out.toByteArray();
}
public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
    ByteArrayInputStream in = new ByteArrayInputStream(data);
    ObjectInputStream is = new ObjectInputStream(in);
    return is.readObject();
}


	public static String md5(String input) {

		String md5 = null;

		if (null == input)
			return null;

		try {

			// Create MessageDigest object for MD5
			MessageDigest digest = MessageDigest.getInstance("MD5");

			// Update input string in message digest
			digest.update(input.getBytes(), 0, input.length());

			// Converts message digest value in base 16 (hex)
			md5 = new BigInteger(1, digest.digest()).toString(16);

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		return md5;
	}
	
 public static String convertObjectToJSonObject(Object objectContent){
	 JSONObject jobject = new JSONObject();
	 //jobject.accumulate(key, value)
	 
		
	return "";
	 
	 
 }
 
 public static String convertListToJSonArray(List<Object> objectList){
	 JsonArray arrayJson = new JsonArray();
//	 arrayJson.
//	 GsonBuilder builder = new GsonBuilder();
//		Gson gson = builder.create();
//		String jsonCon = gson.toJson(objectContent);;
	 return "";
 }
 public static List<InterestArea> splitString(String interestAreasId) {
		List<InterestArea> listed = new ArrayList<>();
		// make the interestAreas comma seperated
		String[] str = interestAreasId.split(",");
		for (int i = 0; i < str.length; i++) {
			long interestId = Long.valueOf(str[i]).longValue();
			InterestArea iA = new InterestArea();
			iA.setId(interestId);
			listed.add(iA);
		}

		return listed;

	}

	public static void post(String apiKey, PollContent content,PrintWriter out) {

		try {

			// 1. URL
			URL url = new URL("https://android.googleapis.com/gcm/send");

			// 2. Open connection
		
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			// 3. Specify POST method
			conn.setRequestMethod("POST");

			// 4. Set the headers
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "key=" + GOOGLE_SERVER_KEY);

			conn.setDoOutput(true);

			// 5. Add JSON data into POST request body
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String jsonCon = gson.toJson(content);
			// System.out.println(gson.toJson(content));

			// 5.2 Get connection output stream
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

			wr.write(jsonCon.getBytes());

			// 5.4 Send the request
			wr.flush();

			// 5.5 close
			wr.close();

			// 6. Get the response
			int responseCode = conn.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// 7. Print result
			System.out.println(response.toString());

		}  catch (Exception e) {
			out.print("Error: Cant Reach  GCMServer");
		}
		
	}
}