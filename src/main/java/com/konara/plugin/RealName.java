package com.konara.plugin;

import com.konara.plugin.element.Group;

import java.util.HashMap;
import java.util.Map;

public class RealName {
    public Map<String, Group> groups = new HashMap<>();
    public void addGroup(String GroupID,String DefaultPrefix){
        groups.put(GroupID,new Group(DefaultPrefix));
    }
}
