package com.example.aperobox.Model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Panier {
    private Map<Box, Integer> boxes;

    public Panier()
    {
        boxes = new HashMap<>();
    }

    public Map<Box, Integer> getBox()
    {
        return boxes;
    }

    public void setBox(Map<Box, Integer> box)
    {
        this.boxes = box;
    }

    public void deleteAll()
    {
        boxes.clear();
    }

    public void addBox(Box box, int quantite)
    {
        /*for (Iterator<Map.Entry<Box, Integer>> it = boxes.entrySet().iterator(); it.hasNext();)
        {
            ;
        }*/
        boxes.put(box, quantite);
    }
}
