import { test, describe } from 'node:test';
import assert from 'node:assert';
import { CurrencyConverter } from '../CurrencyConverter.js';

describe('CurrencyConverter', () => {
    let converter;

    test('should initialize correctly', () => {
        converter = new CurrencyConverter();
        assert.ok(converter);
        assert.ok(converter.getSupportedCurrencies().length > 0);
    });

    test('should validate supported currencies', () => {
        converter = new CurrencyConverter();
        assert.strictEqual(converter.isCurrencySupported('USD'), true);
        assert.strictEqual(converter.isCurrencySupported('EUR'), true);
        assert.strictEqual(converter.isCurrencySupported('INVALID'), false);
    });

    test('should return error for unsupported currency', async () => {
        converter = new CurrencyConverter();
        const result = await converter.convert('INVALID', 'USD', 100);
        assert.strictEqual(result.success, false);
        assert.ok(result.error.includes('no soportada'));
    });

    test('should return error for invalid amount', async () => {
        converter = new CurrencyConverter();
        const result = await converter.convert('USD', 'EUR', -100);
        assert.strictEqual(result.success, false);
        assert.ok(result.error.includes('positivo'));
    });

    test('should get supported currencies list', () => {
        converter = new CurrencyConverter();
        const currencies = converter.getSupportedCurrencies();
        assert.ok(Array.isArray(currencies));
        assert.ok(currencies.length >= 6);
        assert.ok(currencies.some(c => c.code === 'USD'));
        assert.ok(currencies.some(c => c.code === 'EUR'));
    });
});