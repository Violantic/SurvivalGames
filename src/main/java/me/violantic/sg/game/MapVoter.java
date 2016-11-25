package me.violantic.sg.game;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Ethan on 11/10/2016.
 */
public class MapVoter {

    private String name;

    private java.util.Map<String, Integer> values;
    private java.util.Map<UUID, String> voters;

    public MapVoter(String name, Map<String, Integer> maps) {
        this.values = maps;
        this.voters = new HashMap<UUID, String>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public java.util.Map<String, Integer> getValues() {
        return values;
    }

    public void setValues(Map<String, Integer> values) {
        this.values = values;
    }

    public Map<UUID, String> getVoters() {
        return voters;
    }

    public void setVoters(Map<UUID, String> voters) {
        this.voters = voters;
    }

    public boolean hasVoted(UUID user) {
        return voters.containsKey(user);
    }

    public void addVote(UUID user, String map) {
        if(!hasVoted(user)) {
            values.put(map, values.get(map)+1);
        }
        voters.put(user, map);
    }

    public void removeVote(UUID user) {
        if(hasVoted(user)) {
            String map = voters.get(user);
            values.put(map, values.get(map)-1);
        }
    }

    public String getWinner() {
        Map.Entry<String, Integer> maxEntry = null;
        for(Map.Entry<String, Integer> entry : getValues().entrySet()) {
            if(maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) >= 0) {
                maxEntry = entry;
            }
        }

        return maxEntry != null ? maxEntry.getKey() : null;
    }


}
