package com.example.trustnetwork.service;

import com.example.trustnetwork.domain.Message;
import com.example.trustnetwork.domain.Person;
import com.example.trustnetwork.repository.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@AllArgsConstructor
@Slf4j
@org.springframework.stereotype.Service
public class ServiceBean implements Service {


    private final Repository repository;


    @Override
    public Person create(Person person) {
        Person newPerson = new Person();
        newPerson.setId(person.getId());
        newPerson.setTopics(person.getTopics());

        return repository.save(newPerson);
    }


    @Override
    public HashMap<String, Integer> updateRelations(String id, String relations) {
        Person person = repository.findPersonById(id);
        person.setTrust_connections(relations);
        repository.save(person);
        return getRelationsMapById(id);
    }


    @Override
    public HashSet<String> sendMessage(Message message) {
        //We want to get id of the person, who sends the message
        String fromPersonId = message.getFromPersonId();
        HashSet<String> messageTopics = getTopicsSetFromString(message.getTopics());

        HashSet<String> acceptablePeople = new HashSet<>();


        HashMap<String, Integer> relations = getRelationsMapById(fromPersonId);

        //Выбрать все имена, и проверить есть ли у них мин уровень и топик, собрать в сет acceptable
        for (String currentPerson : relations.keySet()) {
            if (setContainsSet(getTopicsSetFromString(repository.findPersonById(currentPerson).getTopics()), messageTopics) &&
                    relations.get(currentPerson) > message.getMinTrustLevel()) {
                acceptablePeople.add(currentPerson);
            }
        }


        while (true) {
            int start = acceptablePeople.size();
            int end;

            HashSet<String> newAcceptablePeople = new HashSet<>();

            for (String current : acceptablePeople) {

                // It gets the relations of the person with the id of current.
                HashMap<String, Integer> deeperRelations = getRelationsMapById(current);

                // Checking if the person with the id has the required trust level and topics. If yes, it adds the id to
                // the set of acceptable people.
                for (String id : deeperRelations.keySet()) {
                    if (setContainsSet(getTopicsSetFromString(repository.findPersonById(id).getTopics()), messageTopics) &&
                            deeperRelations.get(id) > message.getMinTrustLevel()) {
                        newAcceptablePeople.add(id);
                    }
                }

            }
            acceptablePeople.addAll(newAcceptablePeople);
            end = acceptablePeople.size();
            if (end == start) {
                break;
            }
        }

        sendMessageToSet(message, acceptablePeople);
        return acceptablePeople;
    }


    public HashSet<String> sendMessageBonus(Message message) {

        //We want to get id of the person, who sends the message
        String fromPersonId = message.getFromPersonId();
        HashSet<String> messageTopics = getTopicsSetFromString(message.getTopics());

        HashSet<String> acceptablePeopleByTrustLevel = new HashSet<>();
        HashSet<String> acceptablePeople = new HashSet<>();


        HashMap<String, Integer> relations = getRelationsMapById(fromPersonId);

        // Getting the relations of the person with the id of fromPersonId, and adding the id of the person to the
        // set of acceptable people if the trust level of the person is greater than the minimum trust level of the
        // message.
        for (String currentPerson : relations.keySet()) {
            if (relations.get(currentPerson) > message.getMinTrustLevel()) {
                acceptablePeopleByTrustLevel.add(currentPerson);
            }
        }

        // Getting the relations of the people who are acceptable by trust level.
        while (true) {
            int start = acceptablePeopleByTrustLevel.size();
            int end;

            HashSet<String> newAcceptablePeopleByTrustLevel = new HashSet<>();

            for (String current : acceptablePeopleByTrustLevel) {
                HashMap<String, Integer> deeperRelations = getRelationsMapById(current);

                // It gets the relations of the person with the id of current.
                for (String id : deeperRelations.keySet()) {
                    if (deeperRelations.get(id) > message.getMinTrustLevel()) {
                        newAcceptablePeopleByTrustLevel.add(id);
                    }
                }
            }
            acceptablePeopleByTrustLevel.addAll(newAcceptablePeopleByTrustLevel);
            end = acceptablePeopleByTrustLevel.size();
            if (end == start) {
                break;
            }
        }
        // Getting the topics of the person with the id of id, and if the topics of the person contain all the topics
        // of the message, then it adds the id of the person to the set of acceptable people.
        for (String id : acceptablePeopleByTrustLevel) {
            HashSet<String> topicsOfPerson = getTopicsSetFromString(repository.findPersonById(id).getTopics());
            if (setContainsSet(topicsOfPerson, messageTopics)) {
                acceptablePeople.add(id);
            }
        }

        sendMessageToSet(message, acceptablePeople);
        return acceptablePeople;
    }

    /**
     * It takes a string of comma separated topics and returns a HashSet of those topics
     *
     * @param topics A comma-separated list of topics to subscribe to.
     * @return A HashSet of Strings
     */
    private HashSet<String> getTopicsSetFromString(String topics) {
        String[] array = topics.split(",");
        return new HashSet<>(Arrays.asList(array));
    }

    /**
     * It gets the HashMap<Id,trustLevel> for the given id.
     *
     * @param id The id of the person whose relations we want to get.
     * @return A HashMap with the relations of the person with the given id.
     */
    public HashMap<String, Integer> getRelationsMapById(String id) {

        // It gets the relations of the person with the given id.
        String relations = repository.findPersonById(id).getTrust_connections();
        // Checking if the person has any relations. If not, it returns an empty HashMap.
        if (relations == null) {
            return new HashMap<>();
        }

        // It splits the relations string by commas and puts the result in a list.
        List<String> relationsList = new ArrayList<>(Arrays.asList(relations.split(",")));

        HashMap<String, Integer> relationsMap = new HashMap<>();

        // Parsing the relations string.
        for (String current : relationsList) {
            int level;
            String personName;
            if (current.endsWith("10")) {
                level = 10;
                personName = current.substring(0, current.length() - 2);

            } else {
                level = Integer.parseInt(current.substring(current.length() - 1));
                personName = current.substring(0, current.length() - 1);
            }
            relationsMap.put(personName, level);
        }
        return relationsMap;
    }

    /**
     * If all the elements in set b are in set a, then return true
     *
     * @param a The set of words that are in the sentence
     * @param b the set of words that we want to check if it's a subset of a
     * @return The method returns true if the set a contains all the elements of set b.
     */
    private boolean setContainsSet(HashSet<String> a, HashSet<String> b) {
        boolean contains = true;
        for (String string : b) {
            if (!a.contains(string)) {
                return false;
            }
        }
        return contains;
    }

    /**
     * For each person in the set of acceptable people, set the message to the text of the message.
     *
     * @param message          The message that was sent.
     * @param acceptablePeople A set of people who are allowed to receive the message.
     */
    private void sendMessageToSet(Message message, HashSet<String> acceptablePeople) {
        for (String personName : acceptablePeople) {
            Person person = repository.findPersonById(personName);
            person.setMessage(message.getText());
            repository.save(person);
        }
    }


    //Artur
    //Bonus
    //Оформление кода
    //Написать Readme
    //Разобраться с докером
    //Отправить проект

}
