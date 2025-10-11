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

export default function JobLogEntries() {
  const [id, setId] = useState('');
  const [jobInstanceId, setJobInstanceId] = useState('');
  const [jobName, setJobName] = useState('');
  const [type, setType] = useState('');
  const [status, setStatus] = useState('');
  const [results, setResults] = useState([]);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(DEFAULT_PAGE_SIZE);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [selectedContent, setSelectedContent] = useState('');
  const [modalTitle, setModalTitle] = useState('');
  const [copySuccess, setCopySuccess] = useState(false);

  const hasPagination = useMemo(() => {
    // We will use paginated endpoints by default to keep the UI consistent
    return true;
  }, []);

  const fetchData = async () => {
    setLoading(true);
    setError('');
    try {
      // Check if only ID is specified and no other search parameters
      const hasOnlyId = id.trim() && !jobInstanceId.trim() && !jobName.trim() && !type.trim() && !status.trim();
      
      let url, res;
      
      if (hasOnlyId) {
        // Use direct GET by ID endpoint
        url = `/api/job-log-entries/${id.trim()}`;
        res = await fetch(url);
        
        if (res.ok) {
          const singleResult = await res.json();
          setResults([singleResult]);
          setTotalPages(1);
          setTotalElements(1);
        } else if (res.status === 404) {
          // Job log entry not found
          setResults([]);
          setTotalPages(0);
          setTotalElements(0);
        } else {
          throw new Error(`Request failed: ${res.status}`);
        }
      } else {
        // Use search endpoint for all other cases
        url = '/api/job-log-entries';
        let isPaginated = hasPagination;
        const params = new URLSearchParams();
        params.set('page', page.toString());
        params.set('size', size.toString());
        params.set('sortBy', 'eventTs');
        params.set('sortDirection', 'desc');

        // Add search parameters to query string for combination search
        if (id.trim()) {
          params.set('id', id.trim());
        }
        if (jobInstanceId.trim()) {
          params.set('jobInstanceId', jobInstanceId.trim());
        }
        if (jobName.trim()) {
          params.set('jobName', jobName.trim());
        }
        if (type.trim()) {
          params.set('type', type.trim());
        }
        if (status.trim()) {
          params.set('status', status.trim());
        }

        // Use the general search endpoint that supports multiple parameters
        url += isPaginated ? '/search/paginated' : '/search';

        const finalUrl = `${url}?${params.toString()}`;
        res = await fetch(finalUrl);
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

  // When page/size changes, refetch with same filters (but not for ID-only searches)
  useEffect(() => {
    const hasOnlyId = id.trim() && !jobInstanceId.trim() && !jobName.trim() && !type.trim() && !status.trim();
    if (!hasOnlyId) {
      fetchData();
    }
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
    setId('');
    setJobInstanceId('');
    setJobName('');
    setType('');
    setStatus('');
    setPage(0);
    setError('');
    // Fetch with initial values (no filters)
    fetchData();
  };

  const openModal = (content, title) => {
    setSelectedContent(content || 'No content available');
    setModalTitle(title);
    setShowModal(true);
    setCopySuccess(false);
  };

  const closeModal = () => {
    setShowModal(false);
    setSelectedContent('');
    setModalTitle('');
    setCopySuccess(false);
  };

  const copyToClipboard = async () => {
    try {
      await navigator.clipboard.writeText(selectedContent);
      setCopySuccess(true);
      setTimeout(() => setCopySuccess(false), 2000);
    } catch (err) {
      console.error('Failed to copy text: ', err);
      // Fallback for older browsers
      const textArea = document.createElement('textarea');
      textArea.value = selectedContent;
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

  const typeOptions = [
    { label: 'Any', value: '' },
    { label: 'START', value: 'START' },
    { label: 'PROCESS', value: 'PROCESS' },
    { label: 'END', value: 'END' },
  ];

  const statusOptions = [
    { label: 'Any', value: '' },
    { label: 'SUCCESS', value: 'SUCCESS' },
    { label: 'ERROR', value: 'ERROR' },
    { label: 'WARNING', value: 'WARNING' },
  ];

  return (
    <div>
      <h2 style={{ marginBottom: '16px' }}>Job Log Entries</h2>

      <form className="ln-form" onSubmit={onSubmit}>
        <div className="ln-row">
          <div className="ln-field">
            <label>ID</label>
            <input
              type="text"
              value={id}
              onChange={(e) => setId(e.target.value)}
              placeholder="e.g. 1"
            />
          </div>

          <div className="ln-field">
            <label>Job Instance ID</label>
            <input
              type="text"
              value={jobInstanceId}
              onChange={(e) => setJobInstanceId(e.target.value)}
              placeholder="e.g. 1"
            />
          </div>

          <div className="ln-field">
            <label>Job Name</label>
            <input
              type="text"
              value={jobName}
              onChange={(e) => setJobName(e.target.value)}
              placeholder="e.g. DataSyncJob"
            />
          </div>

          <div className="ln-field">
            <label>Type</label>
            <select value={type} onChange={(e) => setType(e.target.value)}>
              {typeOptions.map((o) => (
                <option key={o.label} value={o.value}>{o.label}</option>
              ))}
            </select>
          </div>

          <div className="ln-field">
            <label>Status</label>
            <select value={status} onChange={(e) => setStatus(e.target.value)}>
              {statusOptions.map((o) => (
                <option key={o.label} value={o.value}>{o.label}</option>
              ))}
            </select>
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

        {(() => {
          const hasOnlyId = id.trim() && !jobInstanceId.trim() && !jobName.trim() && !type.trim() && !status.trim();
          return !hasOnlyId && (
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
          );
        })()}
      </form>

      {error && (
        <div className="ln-error">{error}</div>
      )}

      <div className="card" style={{ overflowX: 'auto' }}>
        <table className="ln-table">
          <thead>
            <tr>
              <th style={{ width: '80px' }}>ID</th>
              <th style={{ width: '120px' }}>Job Instance ID</th>
              <th style={{ width: '180px' }}>Job Name</th>
              <th style={{ width: '100px' }}>Type</th>
              <th style={{ width: '120px' }}>Status</th>
              <th style={{ width: '180px' }}>Event Time</th>
              <th style={{ width: '200px' }}>Description</th>
              <th style={{ width: '100px' }}>Record ID</th>
              <th style={{ width: '120px' }}>Payload</th>
              <th style={{ width: '120px' }}>Stacktrace</th>
            </tr>
          </thead>
          <tbody>
            {results.length === 0 && (
              <tr>
                <td colSpan={10} style={{ textAlign: 'center', padding: '16px' }}>
                  {loading ? 'Loading…' : 'No results'}
                </td>
              </tr>
            )}
            {results.map((r) => (
              <tr key={r.id}>
                <td>{r.id}</td>
                <td>{r.jobInstanceId}</td>
                <td>{r.jobName}</td>
                <td>
                  <span style={{
                    padding: '4px 8px',
                    borderRadius: '4px',
                    fontSize: '12px',
                    fontWeight: 'bold',
                    backgroundColor: r.type === 'START' ? '#d1ecf1' : 
                                   r.type === 'PROCESS' ? '#d4edda' : '#fff3cd',
                    color: r.type === 'START' ? '#0c5460' :
                           r.type === 'PROCESS' ? '#155724' : '#856404'
                  }}>
                    {r.type}
                  </span>
                </td>
                <td>
                  <span style={{
                    padding: '4px 8px',
                    borderRadius: '4px',
                    fontSize: '12px',
                    fontWeight: 'bold',
                    backgroundColor: r.status === 'SUCCESS' ? '#d4edda' : 
                                   r.status === 'ERROR' ? '#f8d7da' : '#fff3cd',
                    color: r.status === 'SUCCESS' ? '#155724' :
                           r.status === 'ERROR' ? '#721c24' : '#856404'
                  }}>
                    {r.status}
                  </span>
                </td>
                <td>{formatDate(r.eventTs)}</td>
                <td style={{ maxWidth: '200px', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                  {r.description}
                </td>
                <td>{r.recordId}</td>
                <td style={{ textAlign: 'left', maxWidth: 120 }}>
                  {r.payload ? (
                    <div 
                      style={{ 
                        cursor: 'pointer', 
                        overflow: 'hidden', 
                        textOverflow: 'ellipsis', 
                        whiteSpace: 'nowrap',
                        color: '#007bff',
                        textDecoration: 'underline'
                      }}
                      onClick={() => openModal(r.payload, 'Payload')}
                      title="Click to view full payload"
                    >
                      {r.payload.length > 20 ? `${r.payload.substring(0, 20)}...` : r.payload}
                    </div>
                  ) : (
                    <span style={{ color: '#6c757d', fontStyle: 'italic' }}>No payload</span>
                  )}
                </td>
                <td style={{ textAlign: 'left', maxWidth: 120 }}>
                  {r.stacktrace ? (
                    <div 
                      style={{ 
                        cursor: 'pointer', 
                        overflow: 'hidden', 
                        textOverflow: 'ellipsis', 
                        whiteSpace: 'nowrap',
                        color: '#007bff',
                        textDecoration: 'underline'
                      }}
                      onClick={() => openModal(r.stacktrace, 'Stacktrace')}
                      title="Click to view full stacktrace"
                    >
                      {r.stacktrace.length > 20 ? `${r.stacktrace.substring(0, 20)}...` : r.stacktrace}
                    </div>
                  ) : (
                    <span style={{ color: '#6c757d', fontStyle: 'italic' }}>No stacktrace</span>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        {totalElements > 0 && (
          <div className="ln-footer">
            {(() => {
              const hasOnlyId = id.trim() && !jobInstanceId.trim() && !jobName.trim() && !type.trim() && !status.trim();
              return hasOnlyId ? '1 result found' : `${totalElements} total`;
            })()}
          </div>
        )}
      </div>

      {/* Modal for viewing payload and stacktrace */}
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
              <h3 style={{ margin: 0, color: '#333' }}>{modalTitle}</h3>
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
                  onClick={closeModal}
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
              {selectedContent}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
