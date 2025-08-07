import { test, describe } from 'node:test';
import assert from 'node:assert';
import { ApiService } from '../ApiService.js';

describe('ApiService', () => {
    let apiService;

    test('should initialize correctly', () => {
        apiService = new ApiService();
        assert.ok(apiService);
        assert.ok(apiService.apiKey);
        assert.ok(apiService.baseUrl);
    });

    test('should translate error messages correctly', () => {
        apiService = new ApiService();
        assert.strictEqual(
            apiService.getErrorMessage('unsupported-code'),
            'Código de moneda no soportado'
        );
        assert.strictEqual(
            apiService.getErrorMessage('invalid-key'),
            'Clave de API inválida'
        );
    });

    test('should manage cache correctly', () => {
        apiService = new ApiService();
        const stats = apiService.getCacheStats();
        assert.strictEqual(stats.size, 0);
        assert.ok(stats.timeout > 0);
    });

    test('should clear cache', async () => {
        apiService = new ApiService();
        await apiService.clearCache();
        const stats = apiService.getCacheStats();
        assert.strictEqual(stats.size, 0);
    });
});