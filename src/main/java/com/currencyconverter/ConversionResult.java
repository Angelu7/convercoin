package com.currencyconverter;

/**
 * Clase que representa el resultado de una conversión de moneda
 * Almacena todos los datos relevantes de una operación de conversión
 * 
 * @author Currency Converter Team
 * @version 1.0.0
 */
public class ConversionResult {
    
    private final String monedaOrigen;
    private final String monedaDestino;
    private final double cantidadOriginal;
    private final double cantidadConvertida;
    private final double tasaCambio;
    private final String fechaConversion;
    
    /**
     * Constructor para crear un resultado de conversión
     * 
     * @param monedaOrigen Código de la moneda origen
     * @param monedaDestino Código de la moneda destino
     * @param cantidadOriginal Cantidad original a convertir
     * @param cantidadConvertida Resultado de la conversión
     * @param tasaCambio Tasa de cambio utilizada
     * @param fechaConversion Fecha y hora de la conversión
     */
    public ConversionResult(String monedaOrigen, String monedaDestino, 
                          double cantidadOriginal, double cantidadConvertida, 
                          double tasaCambio, String fechaConversion) {
        this.monedaOrigen = monedaOrigen;
        this.monedaDestino = monedaDestino;
        this.cantidadOriginal = cantidadOriginal;
        this.cantidadConvertida = cantidadConvertida;
        this.tasaCambio = tasaCambio;
        this.fechaConversion = fechaConversion;
    }
    
    /**
     * @return Código de la moneda origen
     */
    public String getMonedaOrigen() {
        return monedaOrigen;
    }
    
    /**
     * @return Código de la moneda destino
     */
    public String getMonedaDestino() {
        return monedaDestino;
    }
    
    /**
     * @return Cantidad original a convertir
     */
    public double getCantidadOriginal() {
        return cantidadOriginal;
    }
    
    /**
     * @return Resultado de la conversión
     */
    public double getCantidadConvertida() {
        return cantidadConvertida;
    }
    
    /**
     * @return Tasa de cambio utilizada
     */
    public double getTasaCambio() {
        return tasaCambio;
    }
    
    /**
     * @return Fecha y hora de la conversión
     */
    public String getFechaConversion() {
        return fechaConversion;
    }
    
    /**
     * Calcula el porcentaje de diferencia entre las monedas
     * 
     * @return Porcentaje de diferencia
     */
    public double getPorcentajeDiferencia() {
        if (tasaCambio == 1.0) {
            return 0.0;
        }
        return ((tasaCambio - 1.0) / 1.0) * 100;
    }
    
    /**
     * Verifica si la conversión es favorable (tasa > 1)
     * 
     * @return true si la conversión es favorable, false en caso contrario
     */
    public boolean esConversionFavorable() {
        return tasaCambio > 1.0;
    }
    
    /**
     * Obtiene un resumen textual de la conversión
     * 
     * @return Resumen en texto de la conversión
     */
    public String obtenerResumen() {
        return String.format("%.2f %s = %.2f %s (Tasa: %.6f) el %s",
            cantidadOriginal, monedaOrigen,
            cantidadConvertida, monedaDestino,
            tasaCambio, fechaConversion);
    }
    
    /**
     * Obtiene un resumen detallado de la conversión
     * 
     * @return Resumen detallado con información adicional
     */
    public String obtenerResumenDetallado() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESULTADO DE CONVERSIÓN ===\n");
        sb.append(String.format("Moneda Origen: %s (%s)\n", 
            monedaOrigen, CurrencyConverter.MONEDAS_SOPORTADAS.get(monedaOrigen)));
        sb.append(String.format("Moneda Destino: %s (%s)\n", 
            monedaDestino, CurrencyConverter.MONEDAS_SOPORTADAS.get(monedaDestino)));
        sb.append(String.format("Cantidad Original: %.2f\n", cantidadOriginal));
        sb.append(String.format("Cantidad Convertida: %.2f\n", cantidadConvertida));
        sb.append(String.format("Tasa de Cambio: %.6f\n", tasaCambio));
        sb.append(String.format("Fecha: %s\n", fechaConversion));
        
        if (Math.abs(getPorcentajeDiferencia()) > 0.01) {
            sb.append(String.format("Diferencia: %.2f%%\n", getPorcentajeDiferencia()));
        }
        
        sb.append("===============================");
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return obtenerResumen();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ConversionResult that = (ConversionResult) obj;
        return Double.compare(that.cantidadOriginal, cantidadOriginal) == 0 &&
               Double.compare(that.cantidadConvertida, cantidadConvertida) == 0 &&
               Double.compare(that.tasaCambio, tasaCambio) == 0 &&
               monedaOrigen.equals(that.monedaOrigen) &&
               monedaDestino.equals(that.monedaDestino) &&
               fechaConversion.equals(that.fechaConversion);
    }
    
    @Override
    public int hashCode() {
        int result = monedaOrigen.hashCode();
        result = 31 * result + monedaDestino.hashCode();
        result = 31 * result + Double.hashCode(cantidadOriginal);
        result = 31 * result + Double.hashCode(cantidadConvertida);
        result = 31 * result + Double.hashCode(tasaCambio);
        result = 31 * result + fechaConversion.hashCode();
        return result;
    }
}