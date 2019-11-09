package com.example.aperobox.Model;

import java.util.HashMap;
import java.util.Map;

public class Panier {
    private Map<Box, Integer> box;

    public Panier()
    {
        box = new HashMap<>();
    }

    public Map<Box, Integer> getBox()
    {
        return box;
    }

    public void setBox(Map<Box, Integer> box)
    {
        this.box = box;
    }

    public void deleteAll()
    {
        box.clear();
    }
}
