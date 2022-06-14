package com.github.arpitkb.service.utils;


import com.github.arpitkb.service.model.Stats;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class StatsRepository {
    static Map<String, Stats> mp = new ConcurrentHashMap<>();
    public StatsRepository(){}

    public List<Stats> getAll(){
        return new ArrayList<>(mp.values());
    }

    public void updateStats(ArrayList<Stats> more){
        more.forEach(stat->{
            mp.put(stat.getName(),stat);
        });
    }

}
