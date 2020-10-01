public class Post {
    private long id;
    private String title;
    private String body;

    long getId() {
        return id;
    }

    public Post setId(long id) {
        this.id = id;
        return this;
    }

    String getTitle() {
        return title;
    }

    Post setTitle(String title) {
        this.title = title;
        return this;
    }

    String getBody() {
        return body;
    }

    Post setBody(String body) {
        this.body = body;
        return this;
    }

}
