/**
 * Authors: Adrien Allemand, James Smith, Loyse Krug
 *
 * Date: 25.11.2018
 *
 * Objective: Class representing an author
 *
 * Comments: -
 *
 * Sources: -
 *
 */

package com.sym.labo02.Object;

public class Author {
    private int id;
    private String firstName;
    private String lastName;

    /**
     * Contructor
     * @param id, int, id of the author
     * @param firstName, String, first name of the author
     * @param lastName, String last name of the author
     */
    public Author(int id, String firstName, String lastName){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Set the id of the author
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set the firstName of the author
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Set the lastName of the author
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the id of the author
     */
    public int getId() {
        return id;
    }

    /**
     * @return the author's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return the author's last name
     */
    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
