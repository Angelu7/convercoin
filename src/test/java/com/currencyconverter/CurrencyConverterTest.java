package com.currencyconverter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

/**
 * Pruebas unitarias para la clase CurrencyConverter
 * 
 * @author Currency Converter Team
 * @version 1.0.0
 */
class CurrencyConverterTest {
    
    private CurrencyConverter converter;
    
    @BeforeEach
    void setUp() {
        converter = new CurrencyConverter();
    }
    
    @Test
    @DisplayName("Conversión de misma moneda debe devolver tasa 1.0")
    void testConversionMismaMoneda() throws IOException {
        double tasa = converter.obtenerTasaCambio("USD", "USD");
        assertEquals(1.0, tasa, 0.0001);
    }
    
    @Test
    @DisplayName("Validación de moneda origen vacía")
    void testMonedaOrigenVacia() {
        assertThrows(IllegalArgumentException.class, () -> {
            converter.convertir("", "USD", 100);
        });
    }
    
    @Test
    @DisplayName("Validación de moneda destino vacía")
    void testMonedaDestinoVacia() {
        assertThrows(IllegalArgumentException.class, () -> {
            converter.convertir("USD", "", 100);
        });
    }
    
    @Test
    @DisplayName("Validación de cantidad negativa")
    void testCantidadNegativa() {
        assertThrows(IllegalArgumentException.class, () -> {
            converter.convertir("USD", "EUR", -100);
        });
    }
    
    @Test
    @DisplayName("Validación de moneda no soportada")
    void testMonedaNoSoportada() {
        assertThrows(IllegalArgumentException.class, () -> {
            converter.convertir("XYZ", "USD", 100);
        });
    }
    
    @Test
    @DisplayName("Historial inicialmente vacío")
    void testHistorialInicialmenteVacio() {
        assertTrue(converter.obtenerHistorial().isEmpty());
    }
    
    @Test
    @DisplayName("Limpieza de historial")
    void testLimpiarHistorial() throws IOException {
        // Realizar una conversión para agregar al historial
        try {
            converter.convertir("USD", "EUR", 100);
            assertFalse(converter.obtenerHistorial().isEmpty());
            
            converter.limpiarHistorial();
            assertTrue(converter.obtenerHistorial().isEmpty());
        } catch (IOException e) {
            // Si no hay conexión, el test pasa
            System.out.println("Test omitido por falta de conexión: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Cache inicialmente vacío")
    void testCacheInicialmenteVacio() {
        String infoCache = converter.obtenerInfoCache();
        assertEquals("Cache vacío", infoCache);
    }
    
    @Test
    @DisplayName("Limpieza de cache")
    void testLimpiarCache() {
        converter.limpiarCache();
        String infoCache = converter.obtenerInfoCache();
        assertEquals("Cache vacío", infoCache);
    }
    
    @Test
    @DisplayName("Todas las monedas soportadas están definidas")
    void testMonedasSoportadas() {
        assertFalse(CurrencyConverter.MONEDAS_SOPORTADAS.isEmpty());
        assertTrue(CurrencyConverter.MONEDAS_SOPORTADAS.containsKey("USD"));
        assertTrue(CurrencyConverter.MONEDAS_SOPORTADAS.containsKey("EUR"));
        assertTrue(CurrencyConverter.MONEDAS_SOPORTADAS.containsKey("BRL"));
        
        // Verificar que todos los valores no son null
        CurrencyConverter.MONEDAS_SOPORTADAS.values().forEach(nombre -> {
            assertNotNull(nombre);
            assertFalse(nombre.trim().isEmpty());
        });
    }
}