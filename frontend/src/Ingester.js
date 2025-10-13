import React, { useState } from 'react';
import { getApiUrl, API_ENDPOINTS } from './utils/api';

const Ingester = () => {
  const [mode, setMode] = useState('getByID');
  const [inputId, setInputId] = useState('');
  const [publishInput, setPublishInput] = useState('');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleGetById = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await fetch(getApiUrl(`${API_ENDPOINTS.INGESTER.GET_BY_ID}?id=${encodeURIComponent(inputId)}`));
      const data = await response.json();
      setResult(data);
    } catch (error) {
      setResult({ error: 'Error fetching data' });
    } finally {
      setLoading(false);
    }
  };

  const handlePublish = async () => {
    setLoading(true);
    setError('');
    try {
      // Split input by comma and convert to numbers
      const numbers = publishInput
        .split(',')
        .map(num => num.trim())
        .filter(num => num.length > 0)
        .map(num => {
          const parsed = Number(num);
          if (isNaN(parsed)) throw new Error(`Invalid number: ${num}`);
          return parsed;
        });

      if (numbers.length === 0) {
        throw new Error('Please enter at least one number');
      }

      const response = await fetch(getApiUrl(API_ENDPOINTS.INGESTER.PUBLISH), {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ numbers }),
      });

      if (!response.ok) {
        throw new Error(`Server error: ${response.status}`);
      }

      const data = await response.json();
      setResult(data);
      setError('');
    } catch (error) {
      setError(error.message || 'Error publishing data');
      setResult(null);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card">
      <h2>Ingester</h2>
      <div style={{ display: 'flex', gap: '20px', marginBottom: '20px' }}>
        <label>
          <input
            type="radio"
            name="mode"
            value="publish"
            checked={mode === 'publish'}
            onChange={() => {
              setMode('publish');
              setResult(null);
              setError('');
            }}
          />
          Publish
        </label>
        <label>
          <input
            type="radio"
            name="mode"
            value="getByID"
            checked={mode === 'getByID'}
            onChange={() => {
              setMode('getByID');
              setResult(null);
              setError('');
            }}
          />
          GetByID
        </label>
      </div>
      {mode === 'getByID' && (
        <>
          <div style={{ marginBottom: '10px' }}>
            <input
              type="text"
              placeholder="Enter ID"
              value={inputId}
              onChange={e => setInputId(e.target.value)}
              style={{ marginRight: '10px', width: '200px' }}
            />
            <button
              onClick={handleGetById}
              disabled={loading || !inputId}
            >
              {loading ? 'Loading...' : 'Submit'}
            </button>
          </div>
          {result && (
            <div style={{ marginTop: '20px' }}>
              <label>Result:</label>
              <textarea
                readOnly
                value={JSON.stringify(result, null, 2)}
                rows={6}
                style={{ width: '100%', fontFamily: 'monospace' }}
              />
            </div>
          )}
        </>
      )}
      {mode === 'publish' && (
        <>
          <div style={{ marginBottom: '10px' }}>
            <textarea
              placeholder="Enter numbers (comma-separated)"
              value={publishInput}
              onChange={e => setPublishInput(e.target.value)}
              style={{
                width: '100%',
                height: '150px',
                marginBottom: '10px',
                fontFamily: 'monospace',
                resize: 'vertical',
                minHeight: '100px'
              }}
            />
            <button
              onClick={handlePublish}
              disabled={loading || !publishInput.trim()}
            >
              {loading ? 'Publishing...' : 'Publish'}
            </button>
          </div>
          {error && (
            <div style={{ color: 'red', marginBottom: '10px' }}>
              {error}
            </div>
          )}
          {result && (
            <div style={{ marginTop: '20px' }}>
              <label>Result:</label>
              <textarea
                readOnly
                value={JSON.stringify(result, null, 2)}
                rows={6}
                style={{ width: '100%', fontFamily: 'monospace' }}
              />
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default Ingester;
