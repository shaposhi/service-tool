import React, { useState, useEffect } from 'react';
import './App.css';
import LogNotifications from './LogNotifications';
import JobInstances from './JobInstances';
import JobLogEntries from './JobLogEntries';
import ColumnMappings from './ColumnMappings';
import ExcelUpload from './ExcelUpload';
import UserMenu from './UserMenu';
import { getApiUrl, API_ENDPOINTS } from './utils/api';
import Ingester from './Ingester';

function App() {
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [activeTab, setActiveTab] = useState('home');
  const [user, setUser] = useState(null);

  const fetchMessage = async () => {
    setLoading(true);
    try {
      const response = await fetch(getApiUrl(API_ENDPOINTS.HELLO));
      const data = await response.json();
      setMessage(data.message);
    } catch (error) {
      setMessage('Error connecting to backend');
      console.error('Error:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchUser = async () => {
    try {
      const response = await fetch(getApiUrl(API_ENDPOINTS.USER.CURRENT));
      if (response.ok) {
        const userData = await response.json();
        setUser(userData);
      }
    } catch (error) {
      console.error('Error fetching user:', error);
    }
  };

  const handleLogout = async () => {
    try {
      const response = await fetch(getApiUrl(API_ENDPOINTS.USER.LOGOUT), { 
        method: 'POST' 
      });
      if (response.ok) {
        // In a real application, you might want to clear local storage, cookies, etc.
        window.location.reload();
      }
    } catch (error) {
      console.error('Error logging out:', error);
    }
  };

  useEffect(() => {
    fetchMessage();
    fetchUser();
  }, []);

  return (
    <div className="App">
      <header className="header">
        <h1>Service Tools Application</h1>
        <UserMenu user={user} onLogout={handleLogout} />
      </header>

      <nav className="tabs">
        <button
          className={`tab ${activeTab === 'home' ? 'active' : ''}`}
          onClick={() => setActiveTab('home')}
        >
          Home
        </button>
        <button
          className={`tab ${activeTab === 'log' ? 'active' : ''}`}
          onClick={() => setActiveTab('log')}
        >
          Log Notifications
        </button>
        <button
          className={`tab ${activeTab === 'jobs' ? 'active' : ''}`}
          onClick={() => setActiveTab('jobs')}
        >
          Job Instances
        </button>
        <button
          className={`tab ${activeTab === 'logs' ? 'active' : ''}`}
          onClick={() => setActiveTab('logs')}
        >
          Job Log Entries
        </button>
        <button
          className={`tab ${activeTab === 'mappings' ? 'active' : ''}`}
          onClick={() => setActiveTab('mappings')}
        >
          Column Mappings
        </button>
        <button
          className={`tab ${activeTab === 'excel' ? 'active' : ''}`}
          onClick={() => setActiveTab('excel')}
        >
          Excel Upload
        </button>
        <button
          className={`tab ${activeTab === 'ingester' ? 'active' : ''}`}
          onClick={() => setActiveTab('ingester')}
        >
          Ingester
        </button>
      </nav>

      <main className="main-content">
        {activeTab === 'home' && (
          <>
            <div className="card">
              <h2>Welcome to Service Tools</h2>
              <p>Dashboard will be here!!!</p>
              <ul style={{ textAlign: 'left', display: 'inline-block' }}>
                <li>Stats will be here!!!</li>
                <li>Usefull links will be here!!!</li>
                <li>TBD will be here!!!</li>
              </ul>

              <div style={{ marginTop: '30px' }}>
                <button
                  className="button"
                  onClick={fetchMessage}
                  disabled={loading}
                >
                  {loading ? 'Loading...' : 'Test Backend Connection'}
                </button>
              </div>

              {message && (
                <div style={{
                  marginTop: '20px',
                  padding: '15px',
                  backgroundColor: '#f8f9fa',
                  borderRadius: '4px',
                  border: '1px solid #dee2e6'
                }}>
                  <strong>Backend Response:</strong> {message}
                </div>
              )}
            </div>
          </>
        )}

        {activeTab === 'log' && (
          <div className="card">
            <LogNotifications />
          </div>
        )}

        {activeTab === 'jobs' && (
          <div className="card">
            <JobInstances />
          </div>
        )}

        {activeTab === 'logs' && (
          <div className="card">
            <JobLogEntries />
          </div>
        )}

        {activeTab === 'mappings' && (
          <div className="card">
            <ColumnMappings />
          </div>
        )}

        {activeTab === 'excel' && (
          <div className="card">
            <ExcelUpload />
          </div>
        )}

        {activeTab === 'ingester' && (
          <Ingester />
        )}
      </main>
    </div>
  );
}

export default App;

