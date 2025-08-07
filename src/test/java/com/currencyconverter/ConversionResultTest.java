package com.currencyconverter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase ConversionResult
 * 
 * @author Currency Converter Team
 * @version 1.0.0
 */
class ConversionResultTest {
    
    private ConversionResult resultado;
    
    @BeforeEach
    void setUp() {
        resultado = new ConversionResult(
            "USD", "EUR", 100.0, 85.0, 0.85, "01/01/2024 12:00:00"
        );
    }
    
    @Test
    @DisplayName("Getters devuelven valores correctos")
    void testGetters() {
        assertEquals("USD", resultado.getMonedaOrigen());
        assertEquals("EUR", resultado.getMonedaDestino());
        assertEquals(100.0, resultado.getCantidadOriginal(), 0.0001);
        assertEquals(85.0, resultado.getCantidadConvertida(), 0.0001);
        assertEquals(0.85, resultado.getTasaCambio(), 0.0001);
        assertEquals("01/01/2024 12:00:00", resultado.getFechaConversion());
    }
    
    @Test
    @DisplayName("Cálculo de porcentaje de diferencia")
    void testPorcentajeDiferencia() {
        double porcentaje = resultado.getPorcentajeDiferencia();
        assertEquals(-15.0, porcentaje, 0.0001);
    }
    
    @Test
    @DisplayName("Conversión no favorable con tasa menor a 1")
    void testConversionNoFavorable() {
        assertFalse(resultado.esConversionFavorable());
    }
    
    @Test
    @DisplayName("Conversión favorable con tasa mayor a 1")
    void testConversionFavorable() {
        ConversionResult resultadoFavorable = new ConversionResult(
            "EUR", "USD", 100.0, 120.0, 1.2, "01/01/2024 12:00:00"
        );
        assertTrue(resultadoFavorable.esConversionFavorable());
    }
    
    @Test
    @DisplayName("Conversión neutral con tasa igual a 1")
    void testConversionNeutral() {
        ConversionResult resultadoNeutral = new ConversionResult(
            "USD", "USD", 100.0, 100.0, 1.0, "01/01/2024 12:00:00"
        );
        assertFalse(resultadoNeutral.esConversionFavorable());
        assertEquals(0.0, resultadoNeutral.getPorcentajeDiferencia(), 0.0001);
    }
    
    @Test
    @DisplayName("Resumen contiene información básica")
    void testObtenerResumen() {
        String resumen = resultado.obtenerResumen();
        assertTrue(resumen.contains("100.00 USD"));
        assertTrue(resumen.contains("85.00 EUR"));
        assertTrue(resumen.contains("0.850000"));
        assertTrue(resumen.contains("01/01/2024 12:00:00"));
    }
    
    @Test
    @DisplayName("Resumen detallado contiene información completa")
    void testObtenerResumenDetallado() {
        String resumenDetallado = resultado.obtenerResumenDetallado();
        assertTrue(resumenDetallado.contains("RESULTADO DE CONVERSIÓN"));
        assertTrue(resumenDetallado.contains("USD"));
        assertTrue(resumenDetallado.contains("EUR"));
        assertTrue(resumenDetallado.contains("100.00"));
        assertTrue(resumenDetallado.contains("85.00"));
        assertTrue(resumenDetallado.contains("0.850000"));
    }
    
    @Test
    @DisplayName("ToString devuelve resumen")
    void testToString() {
        String toString = resultado.toString();
        String resumen = resultado.obtenerResumen();
        assertEquals(resumen, toString);
    }
    
    @Test
    @DisplayName("Equals compara correctamente")
    void testEquals() {
        ConversionResult resultado2 = new ConversionResult(
            "USD", "EUR", 100.0, 85.0, 0.85, "01/01/2024 12:00:00"
        );
        
        ConversionResult resultadoDiferente = new ConversionResult(
            "USD", "EUR", 200.0, 170.0, 0.85, "01/01/2024 12:00:00"
        );
        
        assertEquals(resultado, resultado2);
        assertNotEquals(resultado, resultadoDiferente);
        assertEquals(resultado, resultado); // reflexividad
        assertNotEquals(resultado, null);
        assertNotEquals(resultado, "string");
    }
    
    @Test
    @DisplayName("HashCode es consistente")
    void testHashCode() {
        ConversionResult resultado2 = new ConversionResult(
            "USD", "EUR", 100.0, 85.0, 0.85, "01/01/2024 12:00:00"
        );
        
        assertEquals(resultado.hashCode(), resultado2.hashCode());
        assertEquals(resultado.hashCode(), resultado.hashCode()); // consistencia
    }
}