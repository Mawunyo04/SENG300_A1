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

    String getEmail(){
        return email;
    }

    String getId(){
        return id;
    }

    String getName(){
        return name;
    }
}
