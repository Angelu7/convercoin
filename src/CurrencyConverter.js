import { ApiService } from './ApiService.js';

/**
 * Main currency converter class
 */
export class CurrencyConverter {
    constructor() {
        this.apiService = new ApiService();
        this.supportedCurrencies = [
            { code: 'USD', name: 'Dólar Estadounidense', flag: '🇺🇸' },
            { code: 'EUR', name: 'Euro', flag: '🇪🇺' },
            { code: 'GBP', name: 'Libra Esterlina', flag: '🇬🇧' },
            { code: 'JPY', name: 'Yen Japonés', flag: '🇯🇵' },
            { code: 'AUD', name: 'Dólar Australiano', flag: '🇦🇺' },
            { code: 'CAD', name: 'Dólar Canadiense', flag: '🇨🇦' },
            { code: 'CHF', name: 'Franco Suizo', flag: '🇨🇭' },
            { code: 'CNY', name: 'Yuan Chino', flag: '🇨🇳' },
            { code: 'SEK', name: 'Corona Sueca', flag: '🇸🇪' },
            { code: 'NZD', name: 'Dólar Neozelandés', flag: '🇳🇿' },
            { code: 'MXN', name: 'Peso Mexicano', flag: '🇲🇽' },
            { code: 'SGD', name: 'Dólar de Singapur', flag: '🇸🇬' },
            { code: 'HKD', name: 'Dólar de Hong Kong', flag: '🇭🇰' },
            { code: 'NOK', name: 'Corona Noruega', flag: '🇳🇴' },
            { code: 'BRL', name: 'Real Brasileño', flag: '🇧🇷' },
            { code: 'ARS', name: 'Peso Argentino', flag: '🇦🇷' },
            { code: 'COP', name: 'Peso Colombiano', flag: '🇨🇴' },
            { code: 'CLP', name: 'Peso Chileno', flag: '🇨🇱' },
            { code: 'PEN', name: 'Sol Peruano', flag: '🇵🇪' },
            { code: 'UYU', name: 'Peso Uruguayo', flag: '🇺🇾' }
        ];
    }

    /**
     * Converts currency from one to another
     * @param {string} fromCurrency - Source currency code
     * @param {string} toCurrency - Target currency code
     * @param {number} amount - Amount to convert
     * @returns {Promise<ConversionResult>} Conversion result
     */
    async convert(fromCurrency, toCurrency, amount) {
        try {
            // Validate currencies
            if (!this.isCurrencySupported(fromCurrency)) {
                return {
                    success: false,
                    error: `Moneda no soportada: ${fromCurrency}`,
                    fromCurrency,
                    toCurrency,
                    amount
                };
            }

            if (!this.isCurrencySupported(toCurrency)) {
                return {
                    success: false,
                    error: `Moneda no soportada: ${toCurrency}`,
                    fromCurrency,
                    toCurrency,
                    amount
                };
            }

            // Validate amount
            if (typeof amount !== 'number' || amount <= 0) {
                return {
                    success: false,
                    error: 'La cantidad debe ser un número positivo',
                    fromCurrency,
                    toCurrency,
                    amount
                };
            }

            // Get exchange rate
            const exchangeData = await this.apiService.getExchangeRate(fromCurrency, toCurrency);
            
            if (!exchangeData.success) {
                return {
                    success: false,
                    error: exchangeData.error,
                    fromCurrency,
                    toCurrency,
                    amount
                };
            }

            const exchangeRate = exchangeData.rate;
            const convertedAmount = amount * exchangeRate;

            return {
                success: true,
                fromCurrency,
                toCurrency,
                amount,
                convertedAmount,
                exchangeRate,
                lastUpdate: exchangeData.lastUpdate
            };

        } catch (error) {
            return {
                success: false,
                error: `Error en la conversión: ${error.message}`,
                fromCurrency,
                toCurrency,
                amount
            };
        }
    }

    /**
     * Checks if a currency is supported
     * @param {string} currencyCode - Currency code to check
     * @returns {boolean} True if supported
     */
    isCurrencySupported(currencyCode) {
        return this.supportedCurrencies.some(
            currency => currency.code === currencyCode.toUpperCase()
        );
    }

    /**
     * Gets list of supported currencies
     * @returns {Array} Array of supported currencies
     */
    getSupportedCurrencies() {
        return [...this.supportedCurrencies];
    }

    /**
     * Clears the API cache
     */
    async clearCache() {
        await this.apiService.clearCache();
    }
}