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

export default function JobInstances() {
  const [id, setId] = useState('');
  const [name, setName] = useState('');
  const [status, setStatus] = useState('');
  const [start, setStart] = useState('');
  const [end, setEnd] = useState('');
  const [results, setResults] = useState([]);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(DEFAULT_PAGE_SIZE);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const hasPagination = useMemo(() => {
    // We will use paginated endpoints by default to keep the UI consistent
    return true;
  }, []);

  const fetchData = async () => {
    setLoading(true);
    setError('');
    try {
      // Check if only ID is specified and no other search parameters
      const hasOnlyId = id.trim() && !name.trim() && !status.trim() && !start.trim() && !end.trim();
      
      let url, res;
      
      if (hasOnlyId) {
        // Use direct GET by ID endpoint
        url = `/api/job-instances/${id.trim()}`;
        res = await fetch(url);
        
        if (res.ok) {
          const singleResult = await res.json();
          setResults([singleResult]);
          setTotalPages(1);
          setTotalElements(1);
        } else if (res.status === 404) {
          // Job instance not found
          setResults([]);
          setTotalPages(0);
          setTotalElements(0);
        } else {
          throw new Error(`Request failed: ${res.status}`);
        }
      } else {
        // Use search endpoint for all other cases
        url = '/api/job-instances';
        let isPaginated = hasPagination;
        const params = new URLSearchParams();
        params.set('page', page.toString());
        params.set('size', size.toString());
        params.set('sortBy', 'created');
        params.set('sortDirection', 'desc');

        // Add search parameters to query string for combination search
        if (id.trim()) {
          params.set('id', id.trim());
        }
        if (name.trim()) {
          params.set('name', name.trim());
        }
        if (status.trim()) {
          params.set('status', status.trim());
        }
        if (start.trim()) {
          params.set('start', start);
        }
        if (end.trim()) {
          params.set('end', end);
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
    const hasOnlyId = id.trim() && !name.trim() && !status.trim() && !start.trim() && !end.trim();
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
    setName('');
    setStatus('');
    setStart('');
    setEnd('');
    setPage(0);
    setError('');
    // Fetch with initial values (no filters)
    fetchData();
  };

  const statusOptions = [
    { label: 'Any', value: '' },
    { label: 'RUNNING', value: 'RUNNING' },
    { label: 'COMPLETED', value: 'COMPLETED' },
    { label: 'FAILED', value: 'FAILED' },
    { label: 'PENDING', value: 'PENDING' },
    { label: 'CANCELLED', value: 'CANCELLED' },
  ];

  return (
    <div>
      <h2 style={{ marginBottom: '16px' }}>Job Instances</h2>

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
            <label>Name</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="e.g. DataSyncJob"
            />
          </div>

          <div className="ln-field">
            <label>Status</label>
            <select value={status} onChange={(e) => setStatus(e.target.value)}>
              {statusOptions.map((o) => (
                <option key={o.label} value={o.value}>{o.label}</option>
              ))}
            </select>
          </div>

          <div className="ln-field">
            <label>Created start</label>
            <input
              type="datetime-local"
              value={start}
              onChange={(e) => setStart(e.target.value)}
            />
          </div>

          <div className="ln-field">
            <label>Created end</label>
            <input
              type="datetime-local"
              value={end}
              onChange={(e) => setEnd(e.target.value)}
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

        {(() => {
          const hasOnlyId = id.trim() && !name.trim() && !status.trim() && !start.trim() && !end.trim();
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
              <th style={{ width: '200px' }}>Name</th>
              <th style={{ width: '120px' }}>Status</th>
              <th style={{ width: '180px' }}>Created</th>
              <th style={{ width: '180px' }}>Updated</th>
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
                <td>{r.name}</td>
                <td>
                  <span style={{
                    padding: '4px 8px',
                    borderRadius: '4px',
                    fontSize: '12px',
                    fontWeight: 'bold',
                    backgroundColor: r.status === 'COMPLETED' ? '#d4edda' : 
                                   r.status === 'FAILED' ? '#f8d7da' :
                                   r.status === 'RUNNING' ? '#d1ecf1' :
                                   r.status === 'PENDING' ? '#fff3cd' : '#e2e3e5',
                    color: r.status === 'COMPLETED' ? '#155724' :
                           r.status === 'FAILED' ? '#721c24' :
                           r.status === 'RUNNING' ? '#0c5460' :
                           r.status === 'PENDING' ? '#856404' : '#6c757d'
                  }}>
                    {r.status}
                  </span>
                </td>
                <td>{formatDate(r.created)}</td>
                <td>{formatDate(r.updated)}</td>
              </tr>
            ))}
          </tbody>
        </table>
        {totalElements > 0 && (
          <div className="ln-footer">
            {(() => {
              const hasOnlyId = id.trim() && !name.trim() && !status.trim() && !start.trim() && !end.trim();
              return hasOnlyId ? '1 result found' : `${totalElements} total`;
            })()}
          </div>
        )}
      </div>
    </div>
  );
}
