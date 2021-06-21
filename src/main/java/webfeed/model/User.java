package webfeed.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.api.services.oauth2.model.Userinfo;
import com.google.cloud.datastore.Entity;

public class User {
	
	public static final String KIND = "User";
	
	private Long id;
	public static final String ID = "id";
	
	private String email;
	public static final String EMAIL = "email";
	
	private String name;
	public static final String NAME = "name";
	
	private String pictureLink;
	public static final String PICTURE_LINK = "pictureLink";
	
	public static final String IS_ACTIVE = "isActive";
	
	public static final String CREATED_DATE = "createdDate";

	public static final String API_TOKEN = "apiToken";

	public static final String SESSION_ID = "sessionId";
	
	public String apiToken;
	
	public String sessionId;
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User() {
		
	}
	
	public User(Userinfo userinfo) {

		this.setEmail(userinfo.getEmail());
		this.setName(userinfo.getName());
		this.setPictureLink(userinfo.getPicture());
		
	}
	
	public User(Entity entity) {
		this.setEmail(entity.getString("email"));
		this.setName(entity.getString("name"));
		this.setPictureLink(entity.getString("pictureLink"));
		this.setId(entity.getKey().getId());
	}
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPictureLink() {
		return pictureLink;
	}

	public void setPictureLink(String pictureLink) {
		this.pictureLink = pictureLink;
	}
	
	public String getApiToken() {
		return this.apiToken;
	}
	
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	
	public String generateApiToken(String sessionId) throws NoSuchAlgorithmException {
		
		MessageDigest md  = MessageDigest.getInstance("MD5");
		
		byte[] messageDigest = md.digest(sessionId.getBytes());
		
		BigInteger no = new BigInteger(1, messageDigest);
		  
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
		
	}

}
