package upt.ac.feedback;

public class Version {
    String versionId;
    String versionName;
    String versionPlatform;

    public Version() {

    }

    public Version(String versionId, String versionName, String versionPlatform) {
        this.versionId = versionId;
        this.versionName = versionName;
        this.versionPlatform = versionPlatform;
    }

    public String getVersionId() {
        return versionId;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getVersionPlatform() {
        return versionPlatform;
    }
}
