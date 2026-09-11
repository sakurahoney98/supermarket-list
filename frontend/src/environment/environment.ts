export const environment = {
  apiUrl: typeof window !== 'undefined' && window.location.hostname
    ? `http://${window.location.hostname}:8080`
    : 'http://localhost:8080'
};