package login;

public class userValue {
    private String username;
    private String password;
    private String encryptedData;
    private int keyVersion;

    userValue(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){return username;}

    public String getPassword(){return password;}

    public void setUsername(String username){this.username = username;}

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEncryptedData(){
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData){
        this.encryptedData = encryptedData;
    }

    public void setKeyVersion(int newKeyVersion) {
        this.keyVersion = newKeyVersion;
    }
}
