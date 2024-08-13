package bgPass;

import java.util.Arrays;
import java.util.Objects;

public class passValues {
    private String username;
    private String password;
    private String title;
    private String notes;
    private String lastUpdated;
    private String category;
    private String encryptedData;
    private int keyVersion;

    public passValues(String title, String username, String password, String notes, String lastUpdated, String category) {
        this.title = title;
        this.username = username;
        this.password = password;
        this.notes = notes;
        this.lastUpdated = lastUpdated;
        this.category = category;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setCategoryDirectly(String encryptedCategory) {
        this.category = encryptedCategory;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(byte[] encryptedData) {
        this.encryptedData = Arrays.toString(encryptedData);
    }

    public int getKeyVersion() {
        return keyVersion;
    }

    public void setKeyVersion(int keyVersion) {
        this.keyVersion = keyVersion;
    }

    @Override
    public String toString() {
        return "passValues{" +
                ", title='" + title + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        passValues that = (passValues) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, title);
    }
}
