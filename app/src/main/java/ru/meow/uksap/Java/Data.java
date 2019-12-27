package ru.meow.uksap.Java;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


class Data implements Constant {

    private Map<Integer, String> mapGroup;

    String[] createGroup() {
        mapGroup = new LinkedHashMap<>(getCells().get(rowGroup).size()); //int - column number in the table, String - group name
        for (int i = 0; i < getCells().get(rowGroup).size(); i++) {
            if (!getCells().get(rowGroup).get(i).contains("-")) continue;
            mapGroup.put(i, getCells().get(rowGroup).get(i));
        }

        return mapGroup.values().toArray(new String[mapGroup.size()]);
    }

    public Map<Integer, String> getMapGroup() {
        return mapGroup;
    }

    public void setMapGroup(Map<Integer, String> mapGroup) {
        this.mapGroup = mapGroup;
    }

    List<List<String>> cells;
    public List<List<String>> getCells() {
        return cells;
    }

    public void setCells(List<List<String>> cells) {
        this.cells = cells;
    }
}
