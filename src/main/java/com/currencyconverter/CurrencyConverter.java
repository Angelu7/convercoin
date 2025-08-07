package com.currencyconverter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Clase principal para realizar conversiones de moneda
 * Gestiona las consultas a la API y el caching de tasas de cambio
 * 
 * @author Currency Converter Team
 * @version 1.0.0
 */
public class CurrencyConverter {
    
    /**
     * Monedas soportadas por el convertidor
     * Mapa de código de moneda -> nombre completo
     */
    public static final Map<String, String> MONEDAS_SOPORTADAS = Map.of(
        "USD", "Dólar Estadounidense",
        "EUR", "Euro",
        "BRL", "Real Brasileño", 
        "ARS", "Peso Argentino",
        "COP", "Peso Colombiano",
        "MXN", "Peso Mexicano",
        "GBP", "Libra Esterlina",
        "JPY", "Yen Japonés",
        "CAD", "Dólar Canadiense",
        "CHF", "Franco Suizo"
    );
    
    private final ApiService apiService;
    private final ObjectMapper objectMapper;
    private final List<ConversionResult> historialConversiones;
    private final Map<String, Double> cacheTaskas;
    private final DateTimeFormatter dateFormatter;
    private long ultimaActualizacionCache;
    
    // Cache expira después de 5 minutos
    private static final long DURACION_CACHE_MS = 5 * 60 * 1000;
    
    /**
     * Constructor que inicializa el convertidor
     */
    public CurrencyConverter() {
        this.apiService = new ApiService();
        this.objectMapper = new ObjectMapper();
        this.historialConversiones = new ArrayList<>();
        this.cacheTaskas = new ConcurrentHashMap<>();
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        this.ultimaActualizacionCache = 0;
    }
    
    /**
     * Realiza una conversión de moneda
     * 
     * @param monedaOrigen Código de la moneda origen (ej: USD)
     * @param monedaDestino Código de la moneda destino (ej: EUR)
     * @param cantidad Cantidad a convertir
     * @return Resultado de la conversión
     * @throws IOException Si hay error de conexión con la API
     * @throws IllegalArgumentException Si los parámetros son inválidos
     */
    public ConversionResult convertir(String monedaOrigen, String monedaDestino, double cantidad) 
            throws IOException, IllegalArgumentException {
        
        // Validar parámetros
        validarParametrosConversion(monedaOrigen, monedaDestino, cantidad);
        
        // Obtener tasa de cambio
        double tasaCambio = obtenerTasaCambio(monedaOrigen, monedaDestino);
        
        // Calcular conversión
        double cantidadConvertida = cantidad * tasaCambio;
        
        // Crear resultado
        ConversionResult resultado = new ConversionResult(
            monedaOrigen,
            monedaDestino,
            cantidad,
            cantidadConvertida,
            tasaCambio,
            LocalDateTime.now().format(dateFormatter)
        );
        
        // Guardar en historial
        historialConversiones.add(resultado);
        
        return resultado;
    }
    
    /**
     * Obtiene la tasa de cambio entre dos monedas
     * 
     * @param monedaOrigen Moneda origen
     * @param monedaDestino Moneda destino
     * @return Tasa de cambio
     * @throws IOException Si hay error de conexión
     */
    public double obtenerTasaCambio(String monedaOrigen, String monedaDestino) throws IOException {
        // Si es la misma moneda, la tasa es 1
        if (monedaOrigen.equals(monedaDestino)) {
            return 1.0;
        }
        
        // Obtener todas las tasas actualizadas
        Map<String, Double> tasas = obtenerTasasActuales();
        
        // Calcular tasa de cambio indirecta a través de USD
        if (!monedaOrigen.equals("USD")) {
            // Convertir origen a USD, luego USD a destino
            double tasaOrigenUSD = 1.0 / tasas.get(monedaOrigen);
            double tasaUSDDestino = tasas.get(monedaDestino);
            return tasaOrigenUSD * tasaUSDDestino;
        } else {
            // Conversión directa desde USD
            return tasas.get(monedaDestino);
        }
    }
    
    /**
     * Obtiene todas las tasas de cambio actuales
     * Utiliza cache para reducir llamadas a la API
     * 
     * @return Mapa de moneda -> tasa de cambio (base USD)
     * @throws IOException Si hay error de conexión
     */
    public Map<String, Double> obtenerTasasActuales() throws IOException {
        long tiempoActual = System.currentTimeMillis();
        
        // Verificar si el cache sigue válido
        if (tiempoActual - ultimaActualizacionCache < DURACION_CACHE_MS && !cacheTaskas.isEmpty()) {
            return new HashMap<>(cacheTaskas);
        }
        
        try {
            // Obtener datos de la API
            String respuestaJson = apiService.obtenerTasasCambio("USD");
            
            // Parsear JSON
            JsonNode rootNode = objectMapper.readTree(respuestaJson);
            
            // Verificar si la respuesta es exitosa
            if (!"success".equals(rootNode.get("result").asText())) {
                throw new IOException("Error en la respuesta de la API: " + 
                    rootNode.get("error-type").asText());
            }
            
            // Extraer tasas de cambio
            JsonNode conversionRates = rootNode.get("conversion_rates");
            Map<String, Double> tasasActualizadas = new HashMap<>();
            
            // Procesar solo las monedas soportadas
            MONEDAS_SOPORTADAS.keySet().forEach(moneda -> {
                if (conversionRates.has(moneda)) {
                    tasasActualizadas.put(moneda, conversionRates.get(moneda).asDouble());
                }
            });
            
            // Actualizar cache
            cacheTaskas.clear();
            cacheTaskas.putAll(tasasActualizadas);
            ultimaActualizacionCache = tiempoActual;
            
            return tasasActualizadas;
            
        } catch (Exception e) {
            // Si hay error y tenemos cache, usar cache aunque esté expirado
            if (!cacheTaskas.isEmpty()) {
                System.err.println("⚠️ Usando tasas en cache debido a error de conexión: " + e.getMessage());
                return new HashMap<>(cacheTaskas);
            }
            throw new IOException("Error obteniendo tasas de cambio: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene el historial de conversiones realizadas
     * 
     * @return Lista de resultados de conversiones anteriores
     */
    public List<ConversionResult> obtenerHistorial() {
        return new ArrayList<>(historialConversiones);
    }
    
    /**
     * Limpia el historial de conversiones
     */
    public void limpiarHistorial() {
        historialConversiones.clear();
    }
    
    /**
     * Limpia el cache de tasas de cambio
     */
    public void limpiarCache() {
        cacheTaskas.clear();
        ultimaActualizacionCache = 0;
    }
    
    /**
     * Obtiene información sobre el estado del cache
     * 
     * @return Información del cache
     */
    public String obtenerInfoCache() {
        if (cacheTaskas.isEmpty()) {
            return "Cache vacío";
        }
        
        long tiempoTranscurrido = (System.currentTimeMillis() - ultimaActualizacionCache) / 1000;
        long tiempoRestante = Math.max(0, (DURACION_CACHE_MS / 1000) - tiempoTranscurrido);
        
        return String.format("Cache con %d monedas, válido por %d segundos más", 
            cacheTaskas.size(), tiempoRestante);
    }
    
    /**
     * Valida los parámetros de entrada para una conversión
     */
    private void validarParametrosConversion(String monedaOrigen, String monedaDestino, double cantidad) {
        if (monedaOrigen == null || monedaOrigen.trim().isEmpty()) {
            throw new IllegalArgumentException("La moneda origen no puede estar vacía");
        }
        
        if (monedaDestino == null || monedaDestino.trim().isEmpty()) {
            throw new IllegalArgumentException("La moneda destino no puede estar vacía");
        }
        
        if (!MONEDAS_SOPORTADAS.containsKey(monedaOrigen.toUpperCase())) {
            throw new IllegalArgumentException("Moneda origen no soportada: " + monedaOrigen);
        }
        
        if (!MONEDAS_SOPORTADAS.containsKey(monedaDestino.toUpperCase())) {
            throw new IllegalArgumentException("Moneda destino no soportada: " + monedaDestino);
        }
        
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        
        if (Double.isNaN(cantidad) || Double.isInfinite(cantidad)) {
            throw new IllegalArgumentException("La cantidad debe ser un número válido");
        }
    }
}