class DescriptionService {
    constructor() {
      this.API_URL = 'http://34.57.181.117/api/v1/url';
    }
  
    async getDesc() {
      try {
        const response = await fetch(`${this.API_URL}/desc`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
          }
        });
    
        if (!response.ok) {
          throw new Error('Description alınamadı');
        }
    
        const data = await response.text();
        return { description: data };
      } catch (error) {
        throw new Error('Description servisi hatası: ' + error.message);
      }
    }
  }
  
  const descriptionService = new DescriptionService();
  export default descriptionService;