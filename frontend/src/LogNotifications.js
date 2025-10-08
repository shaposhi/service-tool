import React, { useEffect, useMemo, useState } from 'react';

const DEFAULT_PAGE_SIZE = 10;

function formatDate(iso) {
  try {
    const d = new Date(iso);
    if (Number.isNaN(d.getTime())) return iso;
    return d.toLocaleString();
  } catch (e) {
    return iso;
  }
}

export default function LogNotifications() {
  const [partyId, setPartyId] = useState('');
  const [success, setSuccess] = useState('');
  const [start, setStart] = useState('');
  const [end, setEnd] = useState('');
  const [source, setSource] = useState('');
  const [cMode, setCMode] = useState('');
  const [results, setResults] = useState([]);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(DEFAULT_PAGE_SIZE);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [selectedStackTrace, setSelectedStackTrace] = useState('');
  const [copySuccess, setCopySuccess] = useState(false);

  const hasPagination = useMemo(() => {
    // We will use paginated endpoints by default to keep the UI consistent
    return true;
  }, []);

  const fetchData = async () => {
    setLoading(true);
    setError('');
    try {
      let url = '/api/log-notifications';
      let isPaginated = hasPagination;
      const params = new URLSearchParams();
      params.set('page', page.toString());
      params.set('size', size.toString());
      params.set('sortBy', 'receivedTime');
      params.set('sortDirection', 'desc');

      // Add search parameters to query string for combination search
      if (partyId.trim()) {
        params.set('partyId', partyId.trim());
      }
      if (success.trim()) {
        params.set('success', success.trim());
      }
      if (start.trim()) {
        params.set('start', start);
      }
      if (end.trim()) {
        params.set('end', end);
      }
      if (source.trim()) {
        params.set('source', source.trim());
      }
      if (cMode.trim()) {
        params.set('cMode', cMode.trim());
      }

      // Use the general search endpoint that supports multiple parameters
      url += isPaginated ? '/search/paginated' : '/search';

      const finalUrl = `${url}?${params.toString()}`;
      const res = await fetch(finalUrl);
      if (!res.ok) throw new Error(`Request failed: ${res.status}`);

      if (isPaginated) {
        const pageData = await res.json();
        setResults(pageData.content || []);
        setTotalPages(pageData.totalPages || 0);
        setTotalElements(pageData.totalElements || 0);
      } else {
        const list = await res.json();
        setResults(Array.isArray(list) ? list : []);
        setTotalPages(1);
        setTotalElements(Array.isArray(list) ? list.length : 0);
      }
    } catch (e) {
      setError(e.message || 'Unknown error');
      setResults([]);
      setTotalPages(0);
      setTotalElements(0);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    // Initial fetch
    fetchData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // When page/size changes, refetch with same filters
  useEffect(() => {
    fetchData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page, size]);

  const onSubmit = (e) => {
    e.preventDefault();
    // Reset to first page on new search
    setPage(0);
    fetchData();
  };

  const onClear = () => {
    // Reset all search fields to initial state
    setPartyId('');
    setSuccess('');
    setStart('');
    setEnd('');
    setSource('');
    setCMode('');
    setPage(0);
    setError('');
    // Fetch with initial values (no filters)
    fetchData();
  };

  const openStackTraceModal = (stackTrace) => {
    setSelectedStackTrace(stackTrace || 'No stack trace available');
    setShowModal(true);
    setCopySuccess(false);
  };

  const closeStackTraceModal = () => {
    setShowModal(false);
    setSelectedStackTrace('');
    setCopySuccess(false);
  };

  const copyToClipboard = async () => {
    try {
      await navigator.clipboard.writeText(selectedStackTrace);
      setCopySuccess(true);
      setTimeout(() => setCopySuccess(false), 2000);
    } catch (err) {
      console.error('Failed to copy text: ', err);
      // Fallback for older browsers
      const textArea = document.createElement('textarea');
      textArea.value = selectedStackTrace;
      document.body.appendChild(textArea);
      textArea.select();
      try {
        document.execCommand('copy');
        setCopySuccess(true);
        setTimeout(() => setCopySuccess(false), 2000);
      } catch (fallbackErr) {
        console.error('Fallback copy failed: ', fallbackErr);
      }
      document.body.removeChild(textArea);
    }
  };

  const successOptions = [
    { label: 'Any', value: '' },
    { label: 'Successful', value: 'true' },
    { label: 'Failed', value: 'false' },
  ];

  return (
    <div>
      <h2 style={{ marginBottom: '16px' }}>Log Notifications</h2>

      <form className="ln-form" onSubmit={onSubmit}>
        <div className="ln-row">
          <div className="ln-field">
            <label>Party ID</label>
            <input
              type="text"
              value={partyId}
              onChange={(e) => setPartyId(e.target.value)}
              placeholder="e.g. 1001"
            />
          </div>

          <div className="ln-field">
            <label>Success</label>
            <select value={success} onChange={(e) => setSuccess(e.target.value)}>
              {successOptions.map((o) => (
                <option key={o.label} value={o.value}>{o.label}</option>
              ))}
            </select>
          </div>
          <div className="ln-field">
            <label>Received start</label>
            <input
              type="datetime-local"
              value={start}
              onChange={(e) => setStart(e.target.value)}
            />
          </div>

          <div className="ln-field">
            <label>Received end</label>
            <input
              type="datetime-local"
              value={end}
              onChange={(e) => setEnd(e.target.value)}
            />
          </div>


          <div className="ln-field">
            <label>Source</label>
            <input
              type="text"
              value={source}
              onChange={(e) => setSource(e.target.value)}
              placeholder="Component/service"
            />
          </div>

          <div className="ln-field">
            <label>cMode</label>
            <input
              type="text"
              value={cMode}
              onChange={(e) => setCMode(e.target.value)}
              placeholder="Communication mode"
            />
          </div>

          <div className="ln-actions">
            <button className="button" type="button" onClick={onClear} disabled={loading}>
              Clear
            </button>
            <button className="button" type="submit" disabled={loading}>
              {loading ? 'Searching…' : 'Search'}
            </button>
          </div>
        </div>

        <div className="ln-row">
          <div className="ln-field small">
            <label>Page size</label>
            <select value={size} onChange={(e) => setSize(Number(e.target.value))}>
              {[10, 20, 50].map((s) => (
                <option key={s} value={s}>{s}</option>
              ))}
            </select>
          </div>

          <div className="ln-pagination">
            <button
              type="button"
              className="button"
              disabled={loading || page <= 0}
              onClick={() => setPage((p) => Math.max(0, p - 1))}
            >
              ◀ Prev
            </button>
            <span style={{ margin: '0 8px' }}>
              Page {totalPages === 0 ? 0 : page + 1} of {totalPages}
            </span>
            <button
              type="button"
              className="button"
              disabled={loading || page + 1 >= totalPages}
              onClick={() => setPage((p) => p + 1)}
            >
              Next ▶
            </button>
          </div>
        </div>
      </form>

      {error && (
        <div className="ln-error">{error}</div>
      )}

      <div className="card" style={{ overflowX: 'auto' }}>
        <table className="ln-table">
          <thead>
            <tr>
              <th style={{ width: '80px' }}>ID</th>
              <th style={{ width: '200px' }}>Received</th>
              <th style={{ width: '100px' }}>cMode</th>
              <th style={{ width: '120px' }}>Party ID</th>
              <th style={{ width: '160px' }}>Source</th>
              <th style={{ width: '140px' }}>Completed</th>
              <th style={{ width: '140px' }}>Last Update</th>
              <th style={{ width: '120px' }}>Success</th>
              <th>Stack Trace</th>
            </tr>
          </thead>
          <tbody>
            {results.length === 0 && (
              <tr>
                <td colSpan={5} style={{ textAlign: 'center', padding: '16px' }}>
                  {loading ? 'Loading…' : 'No results'}
                </td>
              </tr>
            )}
            {results.map((r) => (
              <tr key={r.id}>
                <td>{r.id}</td>
                <td>{formatDate(r.receivedTime)}</td>
                <td>{r.cMode}</td>
                <td>{r.partyId}</td>
                <td>{r.source}</td>
                <td>{formatDate(r.completedTime)}</td>
                <td>{formatDate(r.lastUpdateTime)}</td>
                <td>{String(r.succesfullyProcessed)}</td>
                <td style={{ textAlign: 'left', maxWidth: 420 }}>
                  {r.stackTrace ? (
                    <div 
                      style={{ 
                        cursor: 'pointer', 
                        overflow: 'hidden', 
                        textOverflow: 'ellipsis', 
                        whiteSpace: 'nowrap',
                        color: '#007bff',
                        textDecoration: 'underline'
                      }}
                      onClick={() => openStackTraceModal(r.stackTrace)}
                      title="Click to view full stack trace"
                    >
                      {r.stackTrace.length > 50 ? `${r.stackTrace.substring(0, 50)}...` : r.stackTrace}
                    </div>
                  ) : (
                    <span style={{ color: '#6c757d', fontStyle: 'italic' }}>No stack trace</span>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        {totalElements > 0 && (
          <div className="ln-footer">{totalElements} total</div>
        )}
      </div>

      {/* Stack Trace Modal */}
      {showModal && (
        <div style={{
          position: 'fixed',
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
          backgroundColor: 'rgba(0, 0, 0, 0.5)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          zIndex: 1000
        }}>
          <div style={{
            backgroundColor: 'white',
            borderRadius: '8px',
            padding: '20px',
            maxWidth: '80%',
            maxHeight: '80%',
            width: '600px',
            boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
            display: 'flex',
            flexDirection: 'column'
          }}>
            <div style={{
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
              marginBottom: '16px',
              borderBottom: '1px solid #dee2e6',
              paddingBottom: '12px'
            }}>
              <h3 style={{ margin: 0, color: '#333' }}>Stack Trace</h3>
              <div style={{ display: 'flex', gap: '8px' }}>
                <button
                  onClick={copyToClipboard}
                  style={{
                    backgroundColor: copySuccess ? '#28a745' : '#007bff',
                    color: 'white',
                    border: 'none',
                    borderRadius: '4px',
                    padding: '6px 12px',
                    cursor: 'pointer',
                    fontSize: '12px',
                    display: 'flex',
                    alignItems: 'center',
                    gap: '4px'
                  }}
                >
                  {copySuccess ? '✓ Copied!' : '📋 Copy'}
                </button>
                <button
                  onClick={closeStackTraceModal}
                  style={{
                    backgroundColor: '#6c757d',
                    color: 'white',
                    border: 'none',
                    borderRadius: '4px',
                    padding: '6px 12px',
                    cursor: 'pointer',
                    fontSize: '12px'
                  }}
                >
                  ✕ Close
                </button>
              </div>
            </div>
            <div style={{
              flex: 1,
              overflow: 'auto',
              backgroundColor: '#f8f9fa',
              border: '1px solid #dee2e6',
              borderRadius: '4px',
              padding: '12px',
              fontFamily: 'monospace',
              fontSize: '12px',
              lineHeight: '1.4',
              whiteSpace: 'pre-wrap',
              wordBreak: 'break-word'
            }}>
              {selectedStackTrace}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}


