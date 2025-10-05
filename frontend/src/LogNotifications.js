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
  const [message, setMessage] = useState('');
  const [level, setLevel] = useState('');
  const [source, setSource] = useState('');
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
      let url = '/api/log-notifications';
      let isPaginated = hasPagination;
      const params = new URLSearchParams();
      params.set('page', page.toString());
      params.set('size', size.toString());
      params.set('sortBy', 'timestamp');
      params.set('sortDirection', 'desc');

      if (message.trim()) {
        url += '/search' + (isPaginated ? '/paginated' : '');
        params.set('message', message.trim());
      } else if (level.trim()) {
        url += `/level/${encodeURIComponent(level.trim())}` + (isPaginated ? '/paginated' : '');
      } else if (source.trim()) {
        url += `/source/${encodeURIComponent(source.trim())}` + (isPaginated ? '/paginated' : '');
      } else {
        url += isPaginated ? '/paginated' : '';
      }

      const finalUrl = isPaginated ? `${url}?${params.toString()}` : url;
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

  const levels = ['ERROR', 'WARN', 'INFO', 'DEBUG', 'TRACE'];

  return (
    <div>
      <h2 style={{ marginBottom: '16px' }}>Log Notifications</h2>

      <form className="ln-form" onSubmit={onSubmit}>
        <div className="ln-row">
          <div className="ln-field">
            <label>Message contains</label>
            <input
              type="text"
              value={message}
              onChange={(e) => setMessage(e.target.value)}
              placeholder="Search text"
            />
          </div>

          <div className="ln-field">
            <label>Level</label>
            <select value={level} onChange={(e) => setLevel(e.target.value)}>
              <option value="">Any</option>
              {levels.map((l) => (
                <option key={l} value={l}>{l}</option>
              ))}
            </select>
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

          <div className="ln-actions">
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
              <th style={{ width: '200px' }}>Timestamp</th>
              <th style={{ width: '100px' }}>Level</th>
              <th style={{ width: '160px' }}>Source</th>
              <th>Message</th>
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
                <td>{formatDate(r.timestamp)}</td>
                <td>{r.level}</td>
                <td>{r.source}</td>
                <td style={{ textAlign: 'left' }}>{r.message}</td>
              </tr>
            ))}
          </tbody>
        </table>
        {totalElements > 0 && (
          <div className="ln-footer">{totalElements} total</div>
        )}
      </div>
    </div>
  );
}


