package com.devmasterteam.tasks.repository;

import com.devmasterteam.tasks.entity.PriorityEntity;

import java.util.HashMap;
import java.util.List;

public class PriorityCache {

    private static HashMap<Integer, String> mPriorityCache = new HashMap<>();

    /**
     * Classe não permite ser instanciada
     */
    private PriorityCache() {
    }

    /**
     * Atualiza valor do cache
     */
    public static void setCache(List<PriorityEntity> list) {
        for (PriorityEntity item : list) {
            mPriorityCache.put(item.getId(), item.getDescription());
        }
    }

    /**
     * Obtém a descrição da prioridade
     */
    public static String getPriorityDescription(int id) {
        return mPriorityCache.get(id);
    }

}
