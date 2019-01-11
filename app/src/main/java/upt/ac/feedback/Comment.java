package upt.ac.feedback;

public class Comment {
    private String commentId;
    private String commentString;
    private int commentRating;

    public Comment(){
        // very important to retrieve the comment
    }

    public Comment(String commentId, String commentString, int commentRating) {
        this.commentId = commentId;
        this.commentString = commentString;
        this.commentRating = commentRating;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getCommentString() {
        return commentString;
    }

    public int getCommentRating() {
        return commentRating;
    }
}
