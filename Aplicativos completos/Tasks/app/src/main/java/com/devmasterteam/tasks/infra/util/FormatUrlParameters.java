package com.devmasterteam.tasks.infra.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUrlParameters {

    /**
     * Construtor privado - Classe mão precisa ser instanciada
     */
    private FormatUrlParameters() {
    }

    /**
     * Formato esperado - String
     * true / false
     */
    public static String formatBoolean(Boolean value) {
        if (value)
            return "true";
        return "false";
    }

    /**
     * Formato esperado
     * yyyy-MM-dd
     */
    public static String formatDate(Date value) {

        if (value == null)
            return "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(value);
    }

}
