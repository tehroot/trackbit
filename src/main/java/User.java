public class User {

    public String email;
    public byte[] hash;
    public String password_salt;
    public String session_id;

    public User(){}

    public User(String email, byte[] hash, String password_salt, String session_id){
        this.email = email;
        this.hash = hash;
        this.password_salt = password_salt;
        this.session_id = session_id;
    }

    public void set_email(String email){ this.email = email; }

    public String get_email(){  return email; }

    public void set_hash(byte[] hash){ this.hash = hash; }

    public byte[] get_hash(){ return hash; }

    public void set_session_id(String session_id){ this.session_id = session_id; }

    public String get_session_id(){ return session_id; }
}