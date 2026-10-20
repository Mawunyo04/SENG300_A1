package gocycle.trips;

public class Rider {

    String id;
    String name;
    String email;

    public Rider(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }
}
