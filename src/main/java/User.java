public class User {

    public String email;
    public byte[] hash;
    public byte[] password_salt;
    // include token in object format? I don't really know what to include here for that, or whether I need to include anything beyond the user's email in their actual object form..

    public User(){}

    public User(String email){
        this.email = email;
    }

    public User(String email, byte[] hash, byte[] password_salt){
        this.email = email;
        this.hash = hash;
        this.password_salt = password_salt;
    }

    public void set_email(String email){ this.email = email; }

    public String get_email(){  return email; }

    public void set_hash(byte[] hash){ this.hash = hash; }

    public byte[] get_hash(){ return hash; }
}