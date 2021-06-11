package webfeed.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.Gson;

public class Json<T> {

	private static Gson gson = new Gson();

	final Type type;

	public Json(Type type) {
		this.type = type;
	}

	public T convertToModel(String jsonPayload) {
		if (jsonPayload == null || jsonPayload.isEmpty())
			throw new NullPointerException("JSON cannot be null or empty");
		T model =  gson.fromJson(jsonPayload, type);	
		return model;
	}

	public String convertToPayload(T model) {
		
		if(model == null) {
			throw new NullPointerException("Passed object cannot be null");
		}
		return gson.toJson(model);
	}
	
	public String readJson(BufferedReader body) throws IOException {
		
		StringBuilder buffer = new StringBuilder();
		String payload;
		while ((payload = body.readLine()) != null) {
			buffer.append(payload);
		}
		
		return buffer.toString();
	}
	


}
