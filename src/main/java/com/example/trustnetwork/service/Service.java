package com.example.trustnetwork.service;


import com.example.trustnetwork.domain.Message;
import com.example.trustnetwork.domain.Person;

import java.util.HashMap;
import java.util.HashSet;

public interface Service {
    /**
     * The function creates a new person object, sets the id and topics of the new person object to the id and topics of
     * the person object passed in as a parameter, and then saves the new person object to the database
     *
     * @param person The person object that is passed in from the request body.
     * @return A new person object is being returned.
     */
    Person create(Person person);

    /**
     * This function takes in a person's id and a string of relations, and updates the person's relations in the database
     *
     * @param id        the id of the person whose relations you want to update
     * @param relations a string of comma-separated values, where each value is a person's id and the number of degrees of
     *                  separation between the person and the person with the id of id.
     * @return A HashMap of the person's relations.
     */
    HashMap<String, Integer> updateRelations(String id, String relations);

    /**
     * It takes a message, gets the relations map of the sender, and sends the message to all the people in the relations
     * map
     *
     * @param message The message to be sent.
     * @return A HashSet of Strings.
     */
    HashSet<String> sendMessage(Message message);

    /**
     * It sends the message to all people, who has the message topics and TrustLevel, higher than needed
     * (The algorithm searches through all relations of primary person with minTrustLevel, ignores people
     * who are connected, but have no topics, and sends the message to people that have the topics, and have relations
     * higher than needed with primary person)
     *
     * @param message The message that is sent.
     * @return The method returns a set of people who are acceptable by the message.
     */
    HashSet<String> sendMessageBonus(Message message);
}
