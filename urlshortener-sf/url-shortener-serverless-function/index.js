/**
 * Basit URL kısaltma fonksiyonu
 * Base64 + SHA-256 kullanır
 */
const crypto = require('crypto');

exports.shortenUrl = (req, res) => {
  const longUrl = req.body.url;

  if (!longUrl) {
    return res.status(400).json({ error: 'Missing "url" in request body' });
  }

  const hash = crypto.createHash('sha256').update(longUrl).digest();
  const shortCode = hash.toString('base64url').substring(0, 8);

  res.status(200).json({
    longUrl: longUrl,
    shortUrl: shortCode,
  });
};