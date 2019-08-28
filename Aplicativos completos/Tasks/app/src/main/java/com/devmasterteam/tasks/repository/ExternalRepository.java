package com.devmasterteam.tasks.repository;

import android.content.Context;

import com.devmasterteam.tasks.constants.TaskConstants;
import com.devmasterteam.tasks.entity.APIResponseEntity;
import com.devmasterteam.tasks.entity.ParameterEntity;
import com.devmasterteam.tasks.infra.util.NetworkUtil;
import com.devmasterteam.tasks.infra.exception.InternetNotAvailableException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;

public class ExternalRepository {

    // Contexto da activity
    private Context mContext;

    // Construtor
    public ExternalRepository(Context context) {
        this.mContext = context;
    }

    /**
     * Executa chamada a API
     */
    public APIResponseEntity execute(ParameterEntity param) throws InternetNotAvailableException {

        // Verifica se está conectado na internet
        if (!NetworkUtil.isConnectionAvailable(this.mContext)) {
            throw new InternetNotAvailableException();
        }

        InputStream inputStream;
        APIResponseEntity response;
        HttpURLConnection conn;

        try {

            URL url;

            if (param.getMethod().equals(TaskConstants.METHOD.GET))
                url = new URL(param.getUrl() + getQuery(param.getParameters(), param.getMethod()));
            else
                url = new URL(param.getUrl());

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(100000);
            conn.setConnectTimeout(150000);
            conn.setRequestMethod(param.getMethod());
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setUseCaches(false);

            // Faz tratamento para headers
            if (param.getHeaderParameters() != null) {
                Iterator it = param.getHeaderParameters().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    conn.setRequestProperty(pair.getKey().toString(), pair.getValue().toString());
                    it.remove();
                }
            }

            // Tratamento para os verbos que mandam dados no corpo
            if (!param.getMethod().equals(TaskConstants.METHOD.GET)) {

                String query = getQuery(param.getParameters(), param.getMethod());
                byte[] postDataBytes = query.getBytes("UTF-8");
                int postDataLength = postDataBytes.length;

                // Faz tratamento para parâmetros de body
                conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));

                conn.setDoOutput(true);
                conn.getOutputStream().write(postDataBytes);
            }

            // Starts the query
            conn.connect();

            // Caso seja erro 404, não existe input strem fazendo com que caia sempre na exception
            if (conn.getResponseCode() != TaskConstants.STATUS_CODE.SUCCESS) {

                // Lê conteúdo
                inputStream = conn.getErrorStream();
                response = new APIResponseEntity(getStringFromInputStream(inputStream), conn.getResponseCode());

            } else {

                // Lê conteúdo
                inputStream = conn.getInputStream();

                // Monta a classe de resposta da API
                response = new APIResponseEntity(getStringFromInputStream(inputStream), conn.getResponseCode());
            }

            inputStream.close();
            conn.disconnect();

        } catch (Exception e) {
            response = new APIResponseEntity("", TaskConstants.STATUS_CODE.NOT_FOUND);
        }

        // Retorna entidade preenchida
        return response;

    }

    /**
     * Faz a conversão do retorno do webservice para string
     */
    private String getStringFromInputStream(InputStream is) {

        if (is == null)
            return "";

        BufferedReader br;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();

        } catch (IOException e) {
            return "";
        }

        return sb.toString();
    }

    /**
     * Monta a query string
     */
    private String getQuery(AbstractMap<String, String> params, String method) throws UnsupportedEncodingException {

        if (params == null)
            return "";

        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> e : params.entrySet()) {
            if (first) {
                if (method.equals(TaskConstants.METHOD.GET)) {
                    result.append("?");
                }
                first = false;
            } else {
                result.append("&");
            }

            result.append(URLEncoder.encode(e.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(e.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}
