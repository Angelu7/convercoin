import { CurrencyConverter } from './CurrencyConverter.js';
import { createInterface } from 'readline';

/**
 * Main application class for the Currency Converter
 */
class CurrencyConverterApp {
    constructor() {
        this.converter = new CurrencyConverter();
        this.rl = createInterface({
            input: process.stdin,
            output: process.stdout
        });
        this.conversionHistory = [];
    }

    /**
     * Starts the application
     */
    async start() {
        console.log('\n' + '='.repeat(60));
        console.log('🌍 CONVERTIDOR DE MONEDAS DINÁMICO 💱');
        console.log('='.repeat(60));
        console.log('Tasas de cambio actualizadas en tiempo real');
        console.log('Powered by ExchangeRate-API\n');

        await this.showMainMenu();
    }

    /**
     * Displays the main menu
     */
    async showMainMenu() {
        while (true) {
            console.log('\n📋 MENÚ PRINCIPAL:');
            console.log('1. 💱 Convertir monedas');
            console.log('2. 📊 Ver monedas disponibles');
            console.log('3. 📈 Ver historial de conversiones');
            console.log('4. 🔄 Actualizar tasas de cambio');
            console.log('5. ❌ Salir');

            const choice = await this.getUserInput('\n👉 Selecciona una opción (1-5): ');

            switch (choice.trim()) {
                case '1':
                    await this.performConversion();
                    break;
                case '2':
                    await this.showAvailableCurrencies();
                    break;
                case '3':
                    this.showConversionHistory();
                    break;
                case '4':
                    await this.updateExchangeRates();
                    break;
                case '5':
                    console.log('\n👋 ¡Gracias por usar el Convertidor de Monedas!');
                    this.rl.close();
                    return;
                default:
                    console.log('\n❌ Opción inválida. Por favor, selecciona una opción del 1 al 5.');
            }
        }
    }

    /**
     * Performs currency conversion
     */
    async performConversion() {
        try {
            console.log('\n💱 CONVERSIÓN DE MONEDAS');
            console.log('-'.repeat(30));

            const fromCurrency = await this.getUserInput('Moneda origen (ej: USD): ');
            const toCurrency = await this.getUserInput('Moneda destino (ej: EUR): ');
            const amountStr = await this.getUserInput('Cantidad a convertir: ');

            const amount = parseFloat(amountStr);
            if (isNaN(amount) || amount <= 0) {
                console.log('❌ Por favor, ingresa una cantidad válida mayor a 0.');
                return;
            }

            console.log('\n🔄 Obteniendo tasas de cambio...');
            const result = await this.converter.convert(fromCurrency.toUpperCase(), toCurrency.toUpperCase(), amount);

            if (result.success) {
                console.log('\n✅ CONVERSIÓN EXITOSA:');
                console.log(`💰 ${amount} ${fromCurrency.toUpperCase()} = ${result.convertedAmount.toFixed(4)} ${toCurrency.toUpperCase()}`);
                console.log(`📊 Tasa de cambio: 1 ${fromCurrency.toUpperCase()} = ${result.exchangeRate.toFixed(6)} ${toCurrency.toUpperCase()}`);
                console.log(`🕐 Última actualización: ${result.lastUpdate}`);

                // Add to history
                this.conversionHistory.push({
                    from: fromCurrency.toUpperCase(),
                    to: toCurrency.toUpperCase(),
                    amount: amount,
                    result: result.convertedAmount,
                    rate: result.exchangeRate,
                    timestamp: new Date().toLocaleString()
                });
            } else {
                console.log(`❌ Error en la conversión: ${result.error}`);
            }
        } catch (error) {
            console.log(`❌ Error inesperado: ${error.message}`);
        }
    }

    /**
     * Shows available currencies
     */
    async showAvailableCurrencies() {
        console.log('\n💰 MONEDAS DISPONIBLES:');
        console.log('-'.repeat(40));
        
        const currencies = this.converter.getSupportedCurrencies();
        currencies.forEach(currency => {
            console.log(`${currency.code} - ${currency.name} ${currency.flag}`);
        });
        
        console.log(`\n📊 Total: ${currencies.length} monedas disponibles`);
    }

    /**
     * Shows conversion history
     */
    showConversionHistory() {
        console.log('\n📈 HISTORIAL DE CONVERSIONES:');
        console.log('-'.repeat(50));

        if (this.conversionHistory.length === 0) {
            console.log('📭 No hay conversiones en el historial.');
            return;
        }

        this.conversionHistory.forEach((conversion, index) => {
            console.log(`${index + 1}. ${conversion.amount} ${conversion.from} → ${conversion.result.toFixed(4)} ${conversion.to}`);
            console.log(`   📊 Tasa: ${conversion.rate.toFixed(6)} | 🕐 ${conversion.timestamp}`);
            console.log('');
        });
    }

    /**
     * Updates exchange rates
     */
    async updateExchangeRates() {
        console.log('\n🔄 Actualizando tasas de cambio...');
        try {
            await this.converter.clearCache();
            console.log('✅ Tasas de cambio actualizadas correctamente.');
        } catch (error) {
            console.log(`❌ Error al actualizar: ${error.message}`);
        }
    }

    /**
     * Gets user input
     */
    getUserInput(question) {
        return new Promise((resolve) => {
            this.rl.question(question, resolve);
        });
    }
}

// Start the application
const app = new CurrencyConverterApp();
app.start().catch(console.error);