package capstone.skini.security.response;

public interface OAuth2Response {
    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();
}
