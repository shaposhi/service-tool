import React, { useState } from 'react';
import { getApiUrl, API_ENDPOINTS } from './utils/api';

export default function ExcelUpload() {
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [result, setResult] = useState('');

  const onFileChange = (e) => {
    const f = e.target.files && e.target.files[0] ? e.target.files[0] : null;
    setFile(f);
  };

  const onUpload = async () => {
    setError('');
    setResult('');
    if (!file) {
      setError('Please select an .xls or .xlsx file');
      return;
    }
    const form = new FormData();
    form.append('file', file);
    setLoading(true);
    try {
      const res = await fetch(getApiUrl(API_ENDPOINTS.EXCEL_UPLOAD), { method: 'POST', body: form });
      if (!res.ok) throw new Error(`Upload failed: ${res.status}`);
      const json = await res.json();
      setResult(JSON.stringify(json, null, 2));
    } catch (e) {
      setError(e.message || 'Upload failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h2 style={{ marginBottom: 16 }}>Excel Upload</h2>
      <div className="ln-row" style={{ alignItems: 'center', gap: 12 }}>
        <input type="file" accept=".xls,.xlsx" onChange={onFileChange} />
        <button className="button" onClick={onUpload} disabled={loading || !file}>
          {loading ? 'Uploading…' : 'Upload'}
        </button>
      </div>

      {error && <div className="ln-error" style={{ marginTop: 12 }}>{error}</div>}

      <div className="card" style={{ marginTop: 16 }}>
        <label>Parsed result (read-only)</label>
        <textarea
          readOnly
          value={result}
          placeholder="Parsed JSON will appear here"
          style={{ width: '100%', height: 320, fontFamily: 'monospace', fontSize: 12 }}
        />
      </div>
    </div>
  );
}


