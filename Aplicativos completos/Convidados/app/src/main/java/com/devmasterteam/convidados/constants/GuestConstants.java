package com.devmasterteam.convidados.constants;

public class GuestConstants {

    /**
     * Construtor privado impede que classe seja instanciada
     */
    private GuestConstants() {
    }

    /**
     * Usado para passagem de parâmetro entre activities
     */
    public static class BundleConstants {
        public static final String GUEST_ID = "guest_id";
    }

    /**
     * Usado para confirmação do usuário
     */
    public static class CONFIRMATION {
        public static final int PRESENT = 1;
        public static final int ABSENT = 2;
        public static final int NOT_CONFIRMED = 3;
    }

}
