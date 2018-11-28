/**
 * Authors: Adrien Allemand, James Smith, Loyse Krug
 *
 * Date: 25.11.2018
 *
 * Objective: class representing a post
 *
 * Comments: -
 *
 * Sources: -
 *
 */

package com.sym.labo02.Object;

public class Post {
    private String title;
    private String content;
    private String date;

    /**
     * Constructor
     * @param title
     * @param content
     * @param date
     */
    public Post(String title, String content, String date){
        this.title = title;
        this.content = content;
        this.date = date;
    }

    /**
     * Set the title
     * @param title, String new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set the content
     * @param content, String new content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Set the date
     * @param date, String new date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the title of the post
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the content of the post
     */
    public String getContent() {
        return content;
    }

    /**
     * @return the date of the post
     */
    public String getDate() {
        return date;
    }
}
