# 🌍 Convertidor de Monedas Dinámico

Un convertidor de monedas profesional desarrollado en Node.js que obtiene tasas de cambio en tiempo real a través de la API de ExchangeRate-API.

## 🚀 Características

- **💱 Conversión en Tiempo Real**: Tasas de cambio actualizadas automáticamente
- **🌐 20+ Monedas Soportadas**: Incluye las principales monedas mundiales
- **📊 Interfaz Interactiva**: Menú de consola colorizado y fácil de usar
- **📈 Historial de Conversiones**: Registro de todas las conversiones realizadas
- **⚡ Sistema de Caché**: Optimización de consultas para mejor rendimiento
- **🛡️ Manejo de Errores**: Validación robusta y manejo de excepciones
- **🧪 Pruebas Unitarias**: Suite completa de tests

## 📋 Requisitos del Sistema

- **Node.js**: Versión 18.0.0 o superior
- **npm**: Incluido con Node.js
- **Conexión a Internet**: Para obtener tasas de cambio actualizadas

## 🔧 Instalación

1. **Clonar el repositorio**:
```bash
git clone https://github.com/tu-usuario/currency-converter.git
cd currency-converter
```

2. **Instalar dependencias**:
```bash
npm install
```

3. **Ejecutar la aplicación**:
```bash
npm start
```

## 💰 Monedas Soportadas

| Código | Moneda | País/Región |
|--------|--------|-------------|
| USD 🇺🇸 | Dólar Estadounidense | Estados Unidos |
| EUR 🇪🇺 | Euro | Unión Europea |
| GBP 🇬🇧 | Libra Esterlina | Reino Unido |
| JPY 🇯🇵 | Yen Japonés | Japón |
| AUD 🇦🇺 | Dólar Australiano | Australia |
| CAD 🇨🇦 | Dólar Canadiense | Canadá |
| CHF 🇨🇭 | Franco Suizo | Suiza |
| CNY 🇨🇳 | Yuan Chino | China |
| BRL 🇧🇷 | Real Brasileño | Brasil |
| ARS 🇦🇷 | Peso Argentino | Argentina |
| COP 🇨🇴 | Peso Colombiano | Colombia |
| CLP 🇨🇱 | Peso Chileno | Chile |
| PEN 🇵🇪 | Sol Peruano | Perú |
| MXN 🇲🇽 | Peso Mexicano | México |
| ... | Y más | |

## 🎯 Guía de Uso

### Menú Principal

Al ejecutar la aplicación, verás el siguiente menú:

```
🌍 CONVERTIDOR DE MONEDAS DINÁMICO 💱
============================================================
Tasas de cambio actualizadas en tiempo real
Powered by ExchangeRate-API

📋 MENÚ PRINCIPAL:
1. 💱 Convertir monedas
2. 📊 Ver monedas disponibles
3. 📈 Ver historial de conversiones
4. 🔄 Actualizar tasas de cambio
5. ❌ Salir
```

### Ejemplo de Conversión

```
💱 CONVERSIÓN DE MONEDAS
------------------------------
Moneda origen (ej: USD): USD
Moneda destino (ej: EUR): EUR
Cantidad a convertir: 100

🔄 Obteniendo tasas de cambio...

✅ CONVERSIÓN EXITOSA:
💰 100 USD = 85.2340 EUR
📊 Tasa de cambio: 1 USD = 0.852340 EUR
🕐 Última actualización: 12/15/2024, 10:30:45 AM
```

## 🏗️ Arquitectura del Proyecto

```
currency-converter/
├── src/
│   ├── index.js              # Aplicación principal y menú
│   ├── CurrencyConverter.js  # Lógica de conversión
│   ├── ApiService.js         # Servicio de API
│   └── test/
│       ├── CurrencyConverter.test.js
│       └── ApiService.test.js
├── package.json
├── README.md
└── LICENSE
```

### Componentes Principales

- **`index.js`**: Interfaz de usuario y menú interactivo
- **`CurrencyConverter.js`**: Lógica principal de conversión y validación
- **`ApiService.js`**: Manejo de llamadas a la API y caché

## 🧪 Ejecutar Pruebas

```bash
# Ejecutar todas las pruebas
npm test

# Ejecutar en modo desarrollo (con watch)
npm run dev
```

## 🔑 Configuración de API

El proyecto utiliza la API de ExchangeRate-API con la clave: `250d095b89c70a04e229e761`

### Límites de la API
- **1,500 solicitudes gratuitas por mes**
- **Actualizaciones cada 24 horas**
- **Soporte para 160+ monedas**

## 🚨 Manejo de Errores

La aplicación maneja varios tipos de errores:

- **Errores de Red**: Timeout, conexión perdida
- **Errores de API**: Clave inválida, cuota excedida
- **Errores de Validación**: Monedas no soportadas, cantidades inválidas
- **Errores del Sistema**: Problemas de memoria, archivos

## 🎨 Características Técnicas

### Sistema de Caché
- **Duración**: 5 minutos por consulta
- **Beneficios**: Reduce llamadas a la API y mejora rendimiento
- **Gestión**: Limpieza automática y manual

### Validación de Entrada
- **Monedas**: Verificación contra lista de monedas soportadas
- **Cantidades**: Validación de números positivos
- **Formato**: Normalización automática de códigos de moneda

### Optimizaciones
- **Fetch con Timeout**: Evita bloqueos por conexiones lentas
- **Manejo de Memoria**: Gestión eficiente del caché
- **Error Recovery**: Reintentos automáticos en caso de fallos temporales

## 📊 Ejemplos de Uso

### Conversión Básica
```javascript
// Convertir 100 USD a EUR
const result = await converter.convert('USD', 'EUR', 100);
console.log(`${result.amount} ${result.fromCurrency} = ${result.convertedAmount} ${result.toCurrency}`);
```

### Verificar Moneda Soportada
```javascript
if (converter.isCurrencySupported('BTC')) {
    console.log('Bitcoin es soportado');
} else {
    console.log('Bitcoin no está en la lista');
}
```

## 🤝 Contribuir

1. **Fork** el proyecto
2. **Crear** una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. **Commit** tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. **Push** a la rama (`git push origin feature/AmazingFeature`)
5. **Abrir** un Pull Request

## 📝 Changelog

### v1.0.0 (2024-12-15)
- ✅ Implementación inicial
- ✅ Soporte para 20+ monedas
- ✅ Sistema de caché
- ✅ Interfaz de consola interactiva
- ✅ Suite de pruebas unitarias
- ✅ Documentación completa

## 🐛 Solución de Problemas

### Error: "Moneda no soportada"
- Verifica que el código de moneda sea válido (ej: USD, EUR)
- Consulta la lista de monedas disponibles en el menú

### Error: "Error de conexión"
- Verifica tu conexión a internet
- La API puede estar temporalmente no disponible

### Error: "Cuota de API alcanzada"
- Se han agotado las 1,500 solicitudes mensuales gratuitas
- Espera al próximo mes o considera actualizar el plan de API

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo [LICENSE](LICENSE) para más detalles.

## 👨‍💻 Autor

**Currency Converter Team**
- GitHub: [@tu-usuario](https://github.com/tu-usuario)
- Email: tu-email@ejemplo.com

## 🙏 Agradecimientos

- [ExchangeRate-API](https://exchangerate-api.com/) por proporcionar las tasas de cambio
- Comunidad de Node.js por las herramientas y librerías
- Contribuidores del proyecto

---

⭐ **¡Si te gusta este proyecto, dale una estrella en GitHub!** ⭐