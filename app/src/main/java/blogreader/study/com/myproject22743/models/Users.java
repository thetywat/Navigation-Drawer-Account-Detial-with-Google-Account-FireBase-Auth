package blogreader.study.com.myproject22743.models;

/**
 * Created by PK2028 on 4/20/2018.
 */

public class Users {
    private String user;
    private String email;
    private String photoUrl;
    private String Uid;


    public Users(String user) {
        this.user = user;
    }

    public Users(String user, String email, String photoUrl, String uid) {
        this.user = user;
        this.email = email;
        this.photoUrl = photoUrl;
        Uid = uid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }


}
