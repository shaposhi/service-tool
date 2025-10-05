import React, { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);

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
        <p>Java 17 + Spring Boot + React</p>
      </header>
      
      <main className="main-content">
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
      </main>
    </div>
  );
}

export default App;

