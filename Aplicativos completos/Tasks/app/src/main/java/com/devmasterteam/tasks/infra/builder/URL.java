package com.devmasterteam.tasks.infra.builder;

public class URL {

    // URL principal para chamda da API
    private String mServiceUrl;

    /**
     * Construtor privado, classe não precisa ser instaciada
     */
    private URL(Builder builder) {
        this.mServiceUrl = builder.mServiceUrl;
    }

    public String getURL() {
        return mServiceUrl;
    }

    /**
     * Padrão Builder
     */
    public static class Builder {

        private String mServiceUrl;

        /**
         * Construtor
         */
        public Builder(String mainURL) {
            this.mServiceUrl = mainURL;
        }

        /**
         * Adiciona o recurso
         */
        public Builder addResource(String resource) {

            // Certifica de que não existem múltiplas // na URL
            while (resource.endsWith("/"))
                resource = resource.substring(0, resource.length() - 1);

            // Adiciona na URL principal
            this.mServiceUrl = this.mServiceUrl.concat("/" + resource + "/");

            // Retorna instância da classe
            return this;
        }

        /**
         * Retorna URL
         */
        public URL build() {
            return new URL(this);
        }

    }

}
