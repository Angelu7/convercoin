import fetch from 'node-fetch';

/**
 * Service for handling API calls to exchange rate providers
 */
export class ApiService {
    constructor() {
        this.apiKey = '250d095b89c70a04e229e761';
        this.baseUrl = 'https://v6.exchangerate-api.com/v6';
        this.cache = new Map();
        this.cacheTimeout = 5 * 60 * 1000; // 5 minutes
    }

    /**
     * Gets exchange rate between two currencies
     * @param {string} fromCurrency - Source currency
     * @param {string} toCurrency - Target currency
     * @returns {Promise<Object>} Exchange rate data
     */
    async getExchangeRate(fromCurrency, toCurrency) {
        const cacheKey = `${fromCurrency}-${toCurrency}`;
        
        // Check cache first
        if (this.cache.has(cacheKey)) {
            const cachedData = this.cache.get(cacheKey);
            if (Date.now() - cachedData.timestamp < this.cacheTimeout) {
                return cachedData.data;
            }
        }

        try {
            const url = `${this.baseUrl}/${this.apiKey}/pair/${fromCurrency}/${toCurrency}`;
            
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                    'User-Agent': 'CurrencyConverter/1.0'
                },
                timeout: 10000
            });

            if (!response.ok) {
                throw new Error(`HTTP Error: ${response.status} - ${response.statusText}`);
            }

            const data = await response.json();

            if (data.result === 'error') {
                return {
                    success: false,
                    error: this.getErrorMessage(data['error-type'])
                };
            }

            const result = {
                success: true,
                rate: data.conversion_rate,
                lastUpdate: new Date(data.time_last_update_unix * 1000).toLocaleString()
            };

            // Cache the result
            this.cache.set(cacheKey, {
                data: result,
                timestamp: Date.now()
            });

            return result;

        } catch (error) {
            if (error.name === 'AbortError') {
                return {
                    success: false,
                    error: 'Timeout: La solicitud tardó demasiado en responder'
                };
            }

            return {
                success: false,
                error: `Error de conexión: ${error.message}`
            };
        }
    }

    /**
     * Gets all exchange rates for a base currency
     * @param {string} baseCurrency - Base currency code
     * @returns {Promise<Object>} All exchange rates
     */
    async getAllRates(baseCurrency) {
        try {
            const url = `${this.baseUrl}/${this.apiKey}/latest/${baseCurrency}`;
            
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                    'User-Agent': 'CurrencyConverter/1.0'
                },
                timeout: 10000
            });

            if (!response.ok) {
                throw new Error(`HTTP Error: ${response.status}`);
            }

            const data = await response.json();

            if (data.result === 'error') {
                return {
                    success: false,
                    error: this.getErrorMessage(data['error-type'])
                };
            }

            return {
                success: true,
                baseCurrency: data.base_code,
                rates: data.conversion_rates,
                lastUpdate: new Date(data.time_last_update_unix * 1000).toLocaleString()
            };

        } catch (error) {
            return {
                success: false,
                error: `Error al obtener tasas: ${error.message}`
            };
        }
    }

    /**
     * Translates API error codes to user-friendly messages
     * @param {string} errorType - API error type
     * @returns {string} User-friendly error message
     */
    getErrorMessage(errorType) {
        const errorMessages = {
            'unsupported-code': 'Código de moneda no soportado',
            'malformed-request': 'Solicitud mal formada',
            'invalid-key': 'Clave de API inválida',
            'inactive-account': 'Cuenta de API inactiva',
            'quota-reached': 'Cuota de API alcanzada'
        };

        return errorMessages[errorType] || `Error de API: ${errorType}`;
    }

    /**
     * Clears the cache
     */
    async clearCache() {
        this.cache.clear();
    }

    /**
     * Gets cache statistics
     * @returns {Object} Cache statistics
     */
    getCacheStats() {
        return {
            size: this.cache.size,
            timeout: this.cacheTimeout
        };
    }
}