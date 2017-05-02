package com.wadezhang.milkbottle.post_detail;

import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.post.Post;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class Comment extends BmobObject {

    private Comment referenceComment;
    private Post post;
    private String content;
    private User author;

    public Comment getReferenceComment() {
        return referenceComment;
    }

    public void setReferenceComment(Comment referenceComment) {
        this.referenceComment = referenceComment;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
