import React, { useEffect, useState } from 'react';

const DEFAULT_PAGE_SIZE = 10;

export default function ColumnMappings() {
  const [jsonPath, setJsonPath] = useState('');
  const [mainColumn, setMainColumn] = useState('');
  const [alternateColumns, setAlternateColumns] = useState('');
  // create/edit form state
  const [formJsonPath, setFormJsonPath] = useState('');
  const [formMainColumn, setFormMainColumn] = useState('');
  const [formAlternateColumns, setFormAlternateColumns] = useState('');
  const [editingId, setEditingId] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [results, setResults] = useState([]);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(DEFAULT_PAGE_SIZE);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [saveLoading, setSaveLoading] = useState(false);
  const [saveError, setSaveError] = useState('');
  const [saveSuccess, setSaveSuccess] = useState('');

  const fetchData = async () => {
    setLoading(true);
    setError('');
    try {
      let url = '/api/column-mappings';
      const params = new URLSearchParams();
      params.set('page', page.toString());
      params.set('size', size.toString());
      params.set('sortBy', 'id');
      params.set('sortDirection', 'desc');

      if (jsonPath.trim()) {
        url += '/search/json-path';
        params.set('q', jsonPath.trim());
      } else if (mainColumn.trim()) {
        url += '/search/main-column';
        params.set('q', mainColumn.trim());
      } else if (alternateColumns.trim()) {
        url += '/search/alternate-columns';
        params.set('q', alternateColumns.trim());
      } else {
        url += '/paginated';
      }

      const res = await fetch(`${url}?${params.toString()}`);
      if (!res.ok) throw new Error(`Request failed: ${res.status}`);
      const pageData = await res.json();
      setResults(pageData.content || []);
      setTotalPages(pageData.totalPages || 0);
      setTotalElements(pageData.totalElements || 0);
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
    fetchData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    fetchData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page, size]);

  const onSubmit = (e) => {
    e.preventDefault();
    setPage(0);
    fetchData();
  };

  const resetForm = () => {
    setEditingId(null);
    setFormJsonPath('');
    setFormMainColumn('');
    setFormAlternateColumns('');
    setSaveError('');
    setSaveSuccess('');
  };

  const startCreate = () => {
    resetForm();
    setIsModalOpen(true);
  };

  const startEdit = (row) => {
    setEditingId(row.id);
    setFormJsonPath(row.jsonPath || '');
    setFormMainColumn(row.mainColumnName || '');
    setFormAlternateColumns(row.alternateColumnNames || '');
    setSaveError('');
    setSaveSuccess('');
    setIsModalOpen(true);
  };

  const onSave = async (e) => {
    e.preventDefault();
    setSaveLoading(true);
    setSaveError('');
    setSaveSuccess('');
    try {
      const payload = {
        jsonPath: formJsonPath,
        mainColumnName: formMainColumn,
        alternateColumnNames: formAlternateColumns,
      };
      const opts = {
        method: editingId == null ? 'POST' : 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      };
      const url = editingId == null
        ? '/api/column-mappings'
        : `/api/column-mappings/${editingId}`;
      const res = await fetch(url, opts);
      if (!res.ok) throw new Error(`Save failed: ${res.status}`);
      setSaveSuccess(editingId == null ? 'Created successfully' : 'Updated successfully');
      resetForm();
      setIsModalOpen(false);
      // refresh list
      fetchData();
    } catch (err) {
      setSaveError(err.message || 'Failed to save');
    } finally {
      setSaveLoading(false);
    }
  };

  return (
    <div>
      <h2 style={{ marginBottom: '16px' }}>Column Mappings</h2>

      <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: 8 }}>
        <button className="button" type="button" onClick={startCreate}>New Mapping</button>
      </div>

      <form className="ln-form" onSubmit={onSubmit}>
        <div className="ln-row">
          <div className="ln-field">
            <label>JSON path contains</label>
            <input
              type="text"
              value={jsonPath}
              onChange={(e) => setJsonPath(e.target.value)}
              placeholder="e.g. $.user.id"
            />
          </div>

          <div className="ln-field">
            <label>Main column contains</label>
            <input
              type="text"
              value={mainColumn}
              onChange={(e) => setMainColumn(e.target.value)}
              placeholder="e.g. USER_ID"
            />
          </div>

          <div className="ln-field">
            <label>Alternate columns contains</label>
            <input
              type="text"
              value={alternateColumns}
              onChange={(e) => setAlternateColumns(e.target.value)}
              placeholder="e.g. UID,USR_ID"
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
              <th>JSON Path</th>
              <th>Main Column</th>
              <th>Alternate Column Names</th>
              <th style={{ width: '120px' }}></th>
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
                <td style={{ textAlign: 'left' }}>{r.jsonPath}</td>
                <td>{r.mainColumnName}</td>
                <td>{r.alternateColumnNames}</td>
                <td>
                  <button className="button" type="button" onClick={() => startEdit(r)} disabled={loading}>Edit</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        {totalElements > 0 && (
          <div className="ln-footer">{totalElements} total</div>
        )}
      </div>

      {isModalOpen && (
        <div className="modal-overlay" onClick={() => { if (!saveLoading) setIsModalOpen(false); }}>
          <div className="modal" role="dialog" aria-modal="true" onClick={(e) => e.stopPropagation()}>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
              <h3 style={{ margin: 0 }}>{editingId == null ? 'Create New Mapping' : `Edit Mapping #${editingId}`}</h3>
              <button className="button" type="button" onClick={() => setIsModalOpen(false)} disabled={saveLoading}>Close</button>
            </div>
            {saveError && <div className="ln-error" style={{ marginTop: 8 }}>{saveError}</div>}
            {saveSuccess && <div style={{ marginTop: 8, color: '#065f46', background: '#d1fae5', border: '1px solid #a7f3d0', padding: '8px 12px', borderRadius: 6 }}>{saveSuccess}</div>}
            <form className="ln-form" onSubmit={onSave}>
              <div className="ln-row">
                <div className="ln-field">
                  <label>JSON path</label>
                  <input
                    type="text"
                    value={formJsonPath}
                    onChange={(e) => setFormJsonPath(e.target.value)}
                    placeholder="$.path.to.field"
                    required
                  />
                </div>

                <div className="ln-field">
                  <label>Main column name</label>
                  <input
                    type="text"
                    value={formMainColumn}
                    onChange={(e) => setFormMainColumn(e.target.value)}
                    placeholder="MAIN_COLUMN"
                    required
                  />
                </div>

                <div className="ln-field">
                  <label>Alternate column names</label>
                  <input
                    type="text"
                    value={formAlternateColumns}
                    onChange={(e) => setFormAlternateColumns(e.target.value)}
                    placeholder="ALT1,ALT2"
                  />
                </div>

                <div className="ln-actions">
                  <button className="button" type="submit" disabled={saveLoading}>
                    {saveLoading ? 'Saving…' : (editingId == null ? 'Create' : 'Update')}
                  </button>
                </div>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}


