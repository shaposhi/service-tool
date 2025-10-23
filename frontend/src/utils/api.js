const API_BASE_URL = '/service-tool';

export const getApiUrl = (endpoint) => {
    // Remove leading slash if present to avoid double slashes
    const cleanEndpoint = endpoint.startsWith('/') ? endpoint.slice(1) : endpoint;
    return `${API_BASE_URL}/${cleanEndpoint}`;
};

// Common API endpoints
export const API_ENDPOINTS = {
    HELLO: '/api/hello',
    LOG_NOTIFICATIONS: '/api/log-notifications',
    JOB_INSTANCES: '/api/job-instances',
    JOB_LOG_ENTRIES: '/api/job-log-entries',
    COLUMN_MAPPINGS: '/api/column-mappings',
    EXCEL_UPLOAD: '/api/excel/upload',
    INGESTER: {
        GET_BY_ID: '/api/ingester/getById',
        PUBLISH: '/api/ingester/publish'
    },
    USER: {
        CURRENT: '/api/user/current',
        LOGOUT: '/api/user/logout'
    }
};