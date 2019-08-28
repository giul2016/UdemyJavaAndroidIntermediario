package com.devmasterteam.convidados.constants;

/**
 * Todas as constantes utilizadas no banco de dados
 * Tabelas, Colunas
 * */
public class DataBaseConstants {

    /**
     * Não deve ser instanciada
     */
    private DataBaseConstants() {
    }

    /**
     * Tabelas disponíveis no banco de dados com suas colunas
     */
    public static class GUEST {
        public static final String TABLE_NAME = "Guest";

        public static class COLUMNS {
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String PRESENCE = "presence";
        }
    }

}
