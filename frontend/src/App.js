import React, { useState, useEffect } from 'react';
import './App.css';
import LogNotifications from './LogNotifications';
import ColumnMappings from './ColumnMappings';

function App() {
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [activeTab, setActiveTab] = useState('home');

  const fetchMessage = async () => {
    setLoading(true);
    try {
      const response = await fetch('/api/hello');
      const data = await response.json();
      setMessage(data.message);
    } catch (error) {
      setMessage('Error connecting to backend');
      console.error('Error:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMessage();
  }, []);

  return (
    <div className="App">
      <header className="header">
        <h1>Service Tools Application</h1>
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
          className={`tab ${activeTab === 'mappings' ? 'active' : ''}`}
          onClick={() => setActiveTab('mappings')}
        >
          Column Mappings
        </button>
      </nav>

      <main className="main-content">
        {activeTab === 'home' && (
          <>
            <div className="card">
              <h2>Welcome to Service Tools</h2>
              <p>This is a modern web application built with:</p>
              <ul style={{ textAlign: 'left', display: 'inline-block' }}>
                <li>Java 17</li>
                <li>Spring Boot 3.2.0</li>
                <li>React 18</li>
                <li>Maven for build management</li>
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

            <div className="card">
              <h3>Features</h3>
              <p>✅ Maven build integration</p>
              <p>✅ React UI served from Spring Boot static folder</p>
              <p>✅ Hot reloading during development</p>
              <p>✅ Production-ready build process</p>
            </div>
          </>
        )}

        {activeTab === 'log' && (
          <div className="card">
            <LogNotifications />
          </div>
        )}

        {activeTab === 'mappings' && (
          <div className="card">
            <ColumnMappings />
          </div>
        )}
      </main>
    </div>
  );
}

export default App;

