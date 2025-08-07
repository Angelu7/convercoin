package com.currencyconverter;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Servicio para realizar llamadas a la API de tasas de cambio
 * Utiliza OkHttp para manejar las conexiones HTTP de manera eficiente
 * 
 * @author Currency Converter Team
 * @version 1.0.0
 */
public class ApiService {
    
    private static final String API_KEY = "250d095b89c70a04e229e761";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6";
    private static final int TIMEOUT_SECONDS = 10;
    
    private final OkHttpClient httpClient;
    
    /**
     * Constructor que configura el cliente HTTP
     */
    public ApiService() {
        this.httpClient = new OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();
    }
    
    /**
     * Obtiene las tasas de cambio para una moneda base específica
     * 
     * @param monedaBase Código de la moneda base (ej: USD)
     * @return Respuesta JSON con las tasas de cambio
     * @throws IOException Si hay error de conexión o respuesta
     */
    public String obtenerTasasCambio(String monedaBase) throws IOException {
        if (monedaBase == null || monedaBase.trim().isEmpty()) {
            throw new IllegalArgumentException("La moneda base no puede estar vacía");
        }
        
        String url = String.format("%s/%s/latest/%s", BASE_URL, API_KEY, monedaBase.toUpperCase());
        
        Request request = new Request.Builder()
            .url(url)
            .addHeader("Accept", "application/json")
            .addHeader("User-Agent", "CurrencyConverter/1.0")
            .get()
            .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error HTTP: " + response.code() + " - " + response.message());
            }
            
            String responseBody = response.body().string();
            if (responseBody == null || responseBody.trim().isEmpty()) {
                throw new IOException("Respuesta vacía de la API");
            }
            
            return responseBody;
        }
    }
    
    /**
     * Obtiene las tasas de cambio históricas para una fecha específica
     * 
     * @param monedaBase Código de la moneda base
     * @param fecha Fecha en formato YYYY-MM-DD
     * @return Respuesta JSON con las tasas históricas
     * @throws IOException Si hay error de conexión o respuesta
     */
    public String obtenerTasasHistoricas(String monedaBase, String fecha) throws IOException {
        if (monedaBase == null || monedaBase.trim().isEmpty()) {
            throw new IllegalArgumentException("La moneda base no puede estar vacía");
        }
        
        if (fecha == null || fecha.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha no puede estar vacía");
        }
        
        String url = String.format("%s/%s/history/%s/%s", 
            BASE_URL, API_KEY, monedaBase.toUpperCase(), fecha);
        
        Request request = new Request.Builder()
            .url(url)
            .addHeader("Accept", "application/json")
            .addHeader("User-Agent", "CurrencyConverter/1.0")
            .get()
            .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error HTTP: " + response.code() + " - " + response.message());
            }
            
            String responseBody = response.body().string();
            if (responseBody == null || responseBody.trim().isEmpty()) {
                throw new IOException("Respuesta vacía de la API");
            }
            
            return responseBody;
        }
    }
    
    /**
     * Realiza una conversión directa entre dos monedas
     * 
     * @param monedaOrigen Código de la moneda origen
     * @param monedaDestino Código de la moneda destino
     * @param cantidad Cantidad a convertir
     * @return Respuesta JSON con el resultado de la conversión
     * @throws IOException Si hay error de conexión o respuesta
     */
    public String convertirDirecto(String monedaOrigen, String monedaDestino, double cantidad) 
            throws IOException {
        
        if (monedaOrigen == null || monedaOrigen.trim().isEmpty()) {
            throw new IllegalArgumentException("La moneda origen no puede estar vacía");
        }
        
        if (monedaDestino == null || monedaDestino.trim().isEmpty()) {
            throw new IllegalArgumentException("La moneda destino no puede estar vacía");
        }
        
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        
        String url = String.format("%s/%s/pair/%s/%s/%.2f", 
            BASE_URL, API_KEY, 
            monedaOrigen.toUpperCase(), 
            monedaDestino.toUpperCase(), 
            cantidad);
        
        Request request = new Request.Builder()
            .url(url)
            .addHeader("Accept", "application/json")
            .addHeader("User-Agent", "CurrencyConverter/1.0")
            .get()
            .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error HTTP: " + response.code() + " - " + response.message());
            }
            
            String responseBody = response.body().string();
            if (responseBody == null || responseBody.trim().isEmpty()) {
                throw new IOException("Respuesta vacía de la API");
            }
            
            return responseBody;
        }
    }
    
    /**
     * Verifica si la API está disponible
     * 
     * @return true si la API responde correctamente, false en caso contrario
     */
    public boolean verificarConexion() {
        try {
            String respuesta = obtenerTasasCambio("USD");
            return respuesta != null && respuesta.contains("success");
        } catch (IOException e) {
            System.err.println("Error verificando conexión: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene información sobre el estado de la API
     * 
     * @return Información sobre el estado actual de la API
     * @throws IOException Si hay error de conexión
     */
    public String obtenerEstadoApi() throws IOException {
        String url = String.format("%s/%s/status", BASE_URL, API_KEY);
        
        Request request = new Request.Builder()
            .url(url)
            .addHeader("Accept", "application/json")
            .addHeader("User-Agent", "CurrencyConverter/1.0")
            .get()
            .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error HTTP: " + response.code() + " - " + response.message());
            }
            
            return response.body().string();
        }
    }
    
    /**
     * Cierra el cliente HTTP y libera recursos
     */
    public void cerrar() {
        if (httpClient != null) {
            httpClient.dispatcher().executorService().shutdown();
            httpClient.connectionPool().evictAll();
        }
    }
}