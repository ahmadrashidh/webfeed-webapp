package webfeed.utility;

import java.io.IOException;

import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;

public class AccessSecretVersion {

	private AccessSecretVersion() {
	}

	public static String accessSecretVersion() throws IOException {

		String projectId = "ahmad-intern";
		String secretId = "AUTH_KEY";
		String versionId = "1";
		return accessSecretVersion(projectId, secretId, versionId);

	}

	public static String accessSecretVersion(String projectId, String secretId, String versionId) throws IOException {

		try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {

			SecretVersionName secretVersionName = SecretVersionName.of(projectId, secretId, versionId);

			AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);

			return response.getPayload().getData().toStringUtf8();

		}
	}

}
