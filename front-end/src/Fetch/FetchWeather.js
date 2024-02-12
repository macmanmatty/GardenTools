
const baseUrl = 'http://localhost:8080/processWeather';

const fetchWithBody = (method, url, bodyObject) => {
    return fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(bodyObject)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json(); // Assuming the response is JSON
        })
        .catch(error => console.error("Fetching error:", error));
};

export { fetchWithBody };