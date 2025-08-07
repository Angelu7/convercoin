package com.currencyconverter;

import java.io.IOException;
import java.util.*;

/**
 * Clase principal del Convertidor de Monedas
 * Proporciona una interfaz de consola interactiva para realizar conversiones
 * 
 * @author Currency Converter Team
 * @version 1.0.0
 */
public class Main {
    
    // Colores ANSI para mejorar la interfaz de consola
    private static final String RESET = "\033[0m";
    private static final String GREEN = "\033[32m";
    private static final String BLUE = "\033[34m";
    private static final String YELLOW = "\033[33m";
    private static final String RED = "\033[31m";
    private static final String BOLD = "\033[1m";
    
    private static final Scanner scanner = new Scanner(System.in);
    private static final CurrencyConverter converter = new CurrencyConverter();
    
    public static void main(String[] args) {
        mostrarBienvenida();
        
        boolean continuar = true;
        while (continuar) {
            try {
                mostrarMenu();
                int opcion = obtenerOpcionUsuario();
                
                switch (opcion) {
                    case 1:
                        realizarConversion();
                        break;
                    case 2:
                        mostrarMonedasDisponibles();
                        break;
                    case 3:
                        mostrarTasasActuales();
                        break;
                    case 4:
                        mostrarHistorialConversiones();
                        break;
                    case 5:
                        mostrarAyuda();
                        break;
                    case 0:
                        continuar = false;
                        mostrarDespedida();
                        break;
                    default:
                        System.out.println(RED + "❌ Opción no válida. Por favor intente nuevamente." + RESET);
                }
                
                if (continuar) {
                    esperarEnter();
                }
                
            } catch (Exception e) {
                System.err.println(RED + "❌ Error inesperado: " + e.getMessage() + RESET);
                e.printStackTrace();
            }
        }
        
        scanner.close();
    }
    
    /**
     * Muestra el mensaje de bienvenida con información del programa
     */
    private static void mostrarBienvenida() {
        System.out.println(BLUE + BOLD + "\n" + "=".repeat(60));
        System.out.println("🌍 CONVERTIDOR DE MONEDAS v1.0.0");
        System.out.println("=".repeat(60) + RESET);
        System.out.println(GREEN + "¡Bienvenido al convertidor de monedas más completo!");
        System.out.println("Conversiones en tiempo real con tasas de cambio actualizadas" + RESET + "\n");
    }
    
    /**
     * Muestra el menú principal de opciones
     */
    private static void mostrarMenu() {
        System.out.println(BOLD + "\n📋 MENÚ PRINCIPAL:" + RESET);
        System.out.println("1. 💱 Convertir moneda");
        System.out.println("2. 📝 Ver monedas disponibles");
        System.out.println("3. 📊 Ver tasas de cambio actuales");
        System.out.println("4. 📜 Ver historial de conversiones");
        System.out.println("5. ❓ Ayuda");
        System.out.println("0. 🚪 Salir");
        System.out.print(YELLOW + "\n➤ Seleccione una opción: " + RESET);
    }
    
    /**
     * Obtiene y valida la opción seleccionada por el usuario
     */
    private static int obtenerOpcionUsuario() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * Realiza el proceso de conversión de moneda
     */
    private static void realizarConversion() {
        System.out.println(BLUE + BOLD + "\n💱 CONVERSIÓN DE MONEDA" + RESET);
        System.out.println("-".repeat(30));
        
        try {
            // Obtener moneda origen
            System.out.print("Ingrese la moneda origen (ej: USD): ");
            String monedaOrigen = scanner.nextLine().trim().toUpperCase();
            
            if (!esMonedasValida(monedaOrigen)) {
                System.out.println(RED + "❌ Moneda origen no válida o no soportada." + RESET);
                mostrarMonedasDisponibles();
                return;
            }
            
            // Obtener moneda destino
            System.out.print("Ingrese la moneda destino (ej: EUR): ");
            String monedaDestino = scanner.nextLine().trim().toUpperCase();
            
            if (!esMonedasValida(monedaDestino)) {
                System.out.println(RED + "❌ Moneda destino no válida o no soportada." + RESET);
                mostrarMonedasDisponibles();
                return;
            }
            
            if (monedaOrigen.equals(monedaDestino)) {
                System.out.println(YELLOW + "⚠️ Las monedas origen y destino son iguales." + RESET);
                return;
            }
            
            // Obtener cantidad
            System.out.print("Ingrese la cantidad a convertir: ");
            double cantidad;
            try {
                cantidad = Double.parseDouble(scanner.nextLine().trim());
                if (cantidad < 0) {
                    System.out.println(RED + "❌ La cantidad no puede ser negativa." + RESET);
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "❌ Cantidad no válida. Ingrese un número válido." + RESET);
                return;
            }
            
            // Realizar conversión
            System.out.println(BLUE + "\n🔄 Obteniendo tasas de cambio actualizadas..." + RESET);
            ConversionResult resultado = converter.convertir(monedaOrigen, monedaDestino, cantidad);
            
            // Mostrar resultado
            mostrarResultadoConversion(resultado);
            
        } catch (IOException e) {
            System.err.println(RED + "❌ Error de conexión: " + e.getMessage() + RESET);
        } catch (Exception e) {
            System.err.println(RED + "❌ Error en la conversión: " + e.getMessage() + RESET);
        }
    }
    
    /**
     * Verifica si una moneda está en la lista de monedas soportadas
     */
    private static boolean esMonedasValida(String moneda) {
        return CurrencyConverter.MONEDAS_SOPORTADAS.containsKey(moneda);
    }
    
    /**
     * Muestra el resultado de una conversión
     */
    private static void mostrarResultadoConversion(ConversionResult resultado) {
        System.out.println(GREEN + BOLD + "\n✅ CONVERSIÓN EXITOSA" + RESET);
        System.out.println("=".repeat(40));
        System.out.printf("💰 Cantidad original: %.2f %s (%s)%n", 
            resultado.getCantidadOriginal(),
            resultado.getMonedaOrigen(),
            CurrencyConverter.MONEDAS_SOPORTADAS.get(resultado.getMonedaOrigen()));
        System.out.printf("💱 Tasa de cambio: 1 %s = %.6f %s%n", 
            resultado.getMonedaOrigen(),
            resultado.getTasaCambio(),
            resultado.getMonedaDestino());
        System.out.printf("💵 Resultado: " + GREEN + BOLD + "%.2f %s (%s)" + RESET + "%n",
            resultado.getCantidadConvertida(),
            resultado.getMonedaDestino(),
            CurrencyConverter.MONEDAS_SOPORTADAS.get(resultado.getMonedaDestino()));
        System.out.printf("🕒 Fecha de conversión: %s%n", resultado.getFechaConversion());
        System.out.println("=".repeat(40));
    }
    
    /**
     * Muestra todas las monedas disponibles
     */
    private static void mostrarMonedasDisponibles() {
        System.out.println(BLUE + BOLD + "\n📝 MONEDAS DISPONIBLES" + RESET);
        System.out.println("-".repeat(30));
        
        CurrencyConverter.MONEDAS_SOPORTADAS.forEach((codigo, nombre) -> {
            System.out.printf("• %s - %s%n", codigo, nombre);
        });
        
        System.out.println(YELLOW + "\n💡 Tip: Use los códigos de 3 letras para realizar conversiones" + RESET);
    }
    
    /**
     * Muestra las tasas de cambio actuales
     */
    private static void mostrarTasasActuales() {
        System.out.println(BLUE + BOLD + "\n📊 TASAS DE CAMBIO ACTUALES (BASE: USD)" + RESET);
        System.out.println("-".repeat(40));
        
        try {
            System.out.println(BLUE + "🔄 Obteniendo tasas actualizadas..." + RESET);
            Map<String, Double> tasas = converter.obtenerTasasActuales();
            
            System.out.println(GREEN + "\n✅ Tasas obtenidas exitosamente:" + RESET);
            tasas.entrySet().stream()
                .filter(entry -> CurrencyConverter.MONEDAS_SOPORTADAS.containsKey(entry.getKey()))
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String moneda = entry.getKey();
                    Double tasa = entry.getValue();
                    String nombre = CurrencyConverter.MONEDAS_SOPORTADAS.get(moneda);
                    System.out.printf("• 1 USD = %.6f %s (%s)%n", tasa, moneda, nombre);
                });
                
        } catch (IOException e) {
            System.err.println(RED + "❌ Error de conexión: " + e.getMessage() + RESET);
        } catch (Exception e) {
            System.err.println(RED + "❌ Error obteniendo tasas: " + e.getMessage() + RESET);
        }
    }
    
    /**
     * Muestra el historial de conversiones realizadas
     */
    private static void mostrarHistorialConversiones() {
        System.out.println(BLUE + BOLD + "\n📜 HISTORIAL DE CONVERSIONES" + RESET);
        System.out.println("-".repeat(35));
        
        List<ConversionResult> historial = converter.obtenerHistorial();
        
        if (historial.isEmpty()) {
            System.out.println(YELLOW + "📋 No hay conversiones en el historial." + RESET);
            System.out.println("💡 Realice algunas conversiones para ver el historial aquí.");
            return;
        }
        
        for (int i = historial.size() - 1; i >= Math.max(0, historial.size() - 10); i--) {
            ConversionResult resultado = historial.get(i);
            System.out.printf("%d. %.2f %s → %.2f %s (Tasa: %.6f) - %s%n",
                historial.size() - i,
                resultado.getCantidadOriginal(),
                resultado.getMonedaOrigen(),
                resultado.getCantidadConvertida(),
                resultado.getMonedaDestino(),
                resultado.getTasaCambio(),
                resultado.getFechaConversion());
        }
        
        if (historial.size() > 10) {
            System.out.println(YELLOW + "\n... mostrando las últimas 10 conversiones" + RESET);
        }
    }
    
    /**
     * Muestra información de ayuda
     */
    private static void mostrarAyuda() {
        System.out.println(BLUE + BOLD + "\n❓ AYUDA - CONVERTIDOR DE MONEDAS" + RESET);
        System.out.println("=".repeat(45));
        
        System.out.println(GREEN + "\n🎯 Cómo usar el convertidor:" + RESET);
        System.out.println("1. Seleccione la opción '1' para convertir moneda");
        System.out.println("2. Ingrese el código de la moneda origen (ej: USD)");
        System.out.println("3. Ingrese el código de la moneda destino (ej: EUR)");
        System.out.println("4. Ingrese la cantidad a convertir");
        System.out.println("5. ¡Obtenga el resultado en tiempo real!");
        
        System.out.println(YELLOW + "\n💡 Consejos útiles:" + RESET);
        System.out.println("• Use códigos de 3 letras (USD, EUR, BRL, etc.)");
        System.out.println("• Las tasas se actualizan en tiempo real");
        System.out.println("• Puede ver el historial de sus conversiones");
        System.out.println("• Use números decimales con punto (ej: 123.45)");
        
        System.out.println(BLUE + "\n🌐 Monedas más populares:" + RESET);
        System.out.println("• USD - Dólar Estadounidense");
        System.out.println("• EUR - Euro");
        System.out.println("• BRL - Real Brasileño");
        System.out.println("• ARS - Peso Argentino");
        System.out.println("• COP - Peso Colombiano");
        System.out.println("• MXN - Peso Mexicano");
        
        System.out.println(GREEN + "\n📞 Soporte:" + RESET);
        System.out.println("En caso de problemas, verifique su conexión a internet.");
    }
    
    /**
     * Muestra mensaje de despedida
     */
    private static void mostrarDespedida() {
        System.out.println(GREEN + BOLD + "\n🌟 ¡Gracias por usar el Convertidor de Monedas!" + RESET);
        System.out.println(BLUE + "💰 Esperamos haber sido útiles en sus conversiones." + RESET);
        System.out.println(YELLOW + "🔄 ¡Vuelva pronto para más conversiones actualizadas!" + RESET);
        System.out.println(BLUE + BOLD + "=".repeat(50) + RESET + "\n");
    }
    
    /**
     * Pausa la ejecución hasta que el usuario presione Enter
     */
    private static void esperarEnter() {
        System.out.print(YELLOW + "\n⏸️  Presione Enter para continuar..." + RESET);
        scanner.nextLine();
        System.out.print("\033[H\033[2J"); // Limpiar pantalla
        System.out.flush();
    }
}