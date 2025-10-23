import React, { useState, useRef, useEffect } from 'react';
import './UserMenu.css';

const UserMenu = ({ user, onLogout }) => {
  const [isOpen, setIsOpen] = useState(false);
  const dropdownRef = useRef(null);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  return (
    <div className="user-menu" ref={dropdownRef}>
      <div className="user-menu-trigger" onClick={() => setIsOpen(!isOpen)}>
        <span className="username">{user?.username || 'User'}</span>
        <div className="user-icon">
          <svg viewBox="0 0 24 24" width="24" height="24" stroke="currentColor" fill="none">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
            <circle cx="12" cy="7" r="4" />
          </svg>
        </div>
      </div>
      {isOpen && (
        <div className="dropdown-menu">
          <div className="user-info">
            <strong>{user?.username}</strong>
            <div className="email">{user?.email}</div>
          </div>
          <div className="dropdown-divider"></div>
          <button className="logout-button" onClick={() => {
            onLogout();
            setIsOpen(false);
          }}>
            Logout
          </button>
        </div>
      )}
    </div>
  );
};

export default UserMenu;