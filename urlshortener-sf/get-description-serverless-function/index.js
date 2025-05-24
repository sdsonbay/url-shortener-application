/**
 * SWE590 projesi için basit bir HTTP Cloud Function
 * @param {Object} req
 * @param {Object} res
 */
exports.projectInfo = (req, res) => {
  res.status(200).send("This is a URL shortener application prepared for SWE590.");
};