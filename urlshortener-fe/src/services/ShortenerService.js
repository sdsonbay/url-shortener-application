class ShortenerService {
  constructor() {
    this.API_URL = 'http://34.57.181.117/api/v1/url';
  }

  async shortenUrl(url) {
    const response = await fetch(`${this.API_URL}/shorten`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ url: url }),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'An error occured!');
    }

    const data = await response.json();
    return data.shortUrl;
  }
}

const shortenerService = new ShortenerService();
export default shortenerService;