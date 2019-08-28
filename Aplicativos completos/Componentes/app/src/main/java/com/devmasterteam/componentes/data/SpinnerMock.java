package com.devmasterteam.componentes.data;

import java.util.ArrayList;
import java.util.List;

public class SpinnerMock {

    /**
     * Retorna lista de strings para spinner
     */
    public static List<String> getList() {
        List<String> lst = new ArrayList<>();
        lst.add("Gramas");
        lst.add("Kg");
        lst.add("Arroba");
        lst.add("Tonelada");

        return lst;
    }

}
