export async function fetchWithRedirect(input, init) {
  const response = await fetch(input, { ...init, redirect: 'manual' });
  if (response.status === 302) {
    const location = response.headers.get('Location');
    if (location) {
      window.location.href = location;
      return null;
    }
  }
  return response;
}
