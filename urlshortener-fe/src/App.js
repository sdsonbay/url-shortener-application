import React, { Component } from 'react';
import './App.css';
import shortenerService from './services/ShortenerService';
import descriptionService from './services/DescriptionService';

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      url: '',
      shortUrl: '',
      description: '',
      error: '',
      loading: false
    };
  }

  handleUrlChange = (e) => {
    this.setState({ url: e.target.value });
  }

  handleSubmit = async (e) => {
    e.preventDefault();
    this.setState({ loading: true, error: '' });

    try {
      const shortUrl = await shortenerService.shortenUrl(this.state.url);
      this.setState({ shortUrl: `${shortUrl}` });
    } catch (err) {
      this.setState({ error: err.message });
    } finally {
      this.setState({ loading: false });
    }
  }

  handleGetDescription = async () => {
    this.setState({ loading: true, error: '' });
    try {
      const description = await descriptionService.getDesc();
      this.setState({ description: description.description || '' });
    } catch (err) {
      this.setState({ error: err.message });
    } finally {
      this.setState({ loading: false });
    }
  }

  handleCopy = () => {
    navigator.clipboard.writeText(this.state.shortUrl);
  }

  render() {
    const { url, shortUrl, error, loading } = this.state;

    return (
      <div className="App">
        <div className="container">
          <h1>URL Shortener</h1>
          
          <form onSubmit={this.handleSubmit}>
            <input
              type="url"
              value={url}
              onChange={this.handleUrlChange}
              placeholder="Enter URL here"
              required
            />
            <button type="submit" disabled={loading}>
              {loading ? 'Shortening...' : 'Shorten URL'}
            </button>
          </form>

          {error && <p className="error">{error}</p>}

          {shortUrl && (
            <div className="result">
              <p>Shortened URL:</p>
              <div className="short-url">
                <input
                  type="text"
                  value={shortUrl}
                  readOnly
                />
                <button onClick={this.handleCopy}>Copy</button>
              </div>
            </div>
          )}
          <div className="description-section">
            <button 
              onClick={this.handleGetDescription} 
              disabled={loading}
            >
              {loading ? 'Loading...' : 'Get Description'}
            </button>
            {this.state.description && (
              <div className="description-result">
                <p>Description:</p>
                <p>{this.state.description}</p>
              </div>
            )}
          </div>
        </div>
      </div>
    );
  }
}

export default App;