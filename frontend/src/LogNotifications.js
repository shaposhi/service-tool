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
      params.set('sortBy', 'receivedTime');
      params.set('sortDirection', 'desc');

      if (partyId.trim()) {
        url += `/party/${encodeURIComponent(partyId.trim())}` + (isPaginated ? '/paginated' : '');
      } else if (success.trim()) {
        url += `/success/${encodeURIComponent(success.trim())}` + (isPaginated ? '/paginated' : '');
      } else if (start.trim() && end.trim()) {
        url += `/received-range` + (isPaginated ? '/paginated' : '');
        params.set('start', start);
        params.set('end', end);
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
                <td style={{ textAlign: 'left', maxWidth: 420, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{r.stackTrace}</td>
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


